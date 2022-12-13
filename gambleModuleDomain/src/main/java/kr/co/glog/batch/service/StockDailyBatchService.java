package kr.co.glog.batch.service;

import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.datagokr.fsc.GetStockPriceInfo;
import kr.co.glog.external.datagokr.fsc.model.GetPriceStockInfoResult;
import kr.co.glog.external.daumFinance.DaumDailyIndexScrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDailyBatchService {

    private final GetStockPriceInfo getStockPriceInfo;
    private final StockDailyService stockDailyService;
    private final StockDailyDao stockDailyDao;

    /**
     *  2022.11.19 : 현재 마지막 날짜의 데이터가 언제 만들어지는 지 알 수 없음
     *             : 거래일 다음날 오전 9시경에는 생성되어 있지 않는 거 같음
     */
    public void upsertFromKrxDataByDate( String yyyymmdd ) {

        String batch = "[BATCH] 일별 종목 거래데이터 업데이트 - 금융위원회 주식정보";

        long startTime = System.currentTimeMillis();
        log.debug( batch + "시작 : " + startTime);

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        try {

            // 한 방에 전체 목록 가져옴
            Document document = getStockPriceInfo.getDocument( "", "", yyyymmdd, "", "", 1, 10000 );
            ArrayList<GetPriceStockInfoResult> list = getStockPriceInfo.getStockPriceInfoList( document );

            // StockDaily 형태로 변환
            for ( GetPriceStockInfoResult result : list ) {
                StockDaily stockDaily = stockDailyService.getStockDailyFromFscStockInfo( result );
                stockDailyList.add( stockDaily );
            }

            // 변환된 녀석을 저장
            for ( StockDaily stockDaily : stockDailyList ) {
                stockDailyDao.saveStockDaily( stockDaily );
            }

        } catch ( Exception e ) {
            long exceptionTime = System.currentTimeMillis();
            log.debug( batch + "에러 : " + exceptionTime);
        } finally {
            long endTime = System.currentTimeMillis();
            log.debug( batch + "종료 : " + endTime);
        }

    }

    /**
     * 2020.01.02 가 가져올 수 있는 가장 옛날 데이터
     * 2020.01.02 부터 모든 데이터를 가져와서 업데이트 해보자
     *
     */
    public void upsertFromKrxDataAll() {

        String batch = "[BATCH] 일별 종목 거래데이터 전체 업데이트 - 금융위원회 주식정보";

        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 모든 날짜 목록 확보시작 : " + time);

            // 삼성전자 데이터로, 모든 날짜 목록을 만들자
            Document document = getStockPriceInfo.getDocument( "", "KR7005930003", "", "20200124", "20210107", 1, 10000 );
            // Document document = getStockPriceInfo.getDocument( "", "KR7005930003", "20220124", "", "", 1, 10000 );
            ArrayList<GetPriceStockInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );

            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 모든 날짜 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 조회하자
            for ( GetPriceStockInfoResult samsungResult : samsungList ) {

                // 목록 초기화
                ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 조회 시작 : " + time);

                // 특정 날짜의 전체 종목 데이터 가져옴
                int failCount = 0;
                while ( true ) {
                    try {
                        document = getStockPriceInfo.getDocument("", "", samsungResult.getBasDt(), "", "", 1, 10000);
                        break;
                    } catch (NetworkCommunicationFailureException ncfe) {
                        failCount++;
                        Thread.sleep( 5000 );
                        if ( failCount >= 3 ) throw ncfe;
                    }
                }

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 조회 종료 : " + time);

                ArrayList<GetPriceStockInfoResult> list = getStockPriceInfo.getStockPriceInfoList( document );

                // StockDaily 형태로 변환
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 변환 시작 : " + time);
                for ( GetPriceStockInfoResult result : list ) {
                    StockDaily stockDaily = stockDailyService.getStockDailyFromFscStockInfo( result );
                    stockDailyList.add( stockDaily );
                }
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 변환 종료 : " + time);

                // 변환된 녀석을 저장
                for ( StockDaily stockDaily : stockDailyList ) {
                    time = System.currentTimeMillis();
                    log.debug( batch + "특정종목 (" + stockDaily.getStockName() + ") 데이터 저장 시작 : " + time);
                    stockDailyDao.saveStockDaily( stockDaily );
                    time = System.currentTimeMillis();
                    log.debug( batch + "특정종목 (" + stockDaily.getStockName() + ") 데이터 저장 종료 : " + time);
                }
            }

        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug( batch + "종료 : " + time);
        }

    }



    /**
     * 최근 10일 데이터 업서트
     */
    public void upsertFromKrxData10() {

        String batch = "[BATCH] 일별 종목 거래데이터 최근 10일 업데이트 - 금융위원회 주식정보";

        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 최근 10일 목록 확보시작 : " + time);
            String endDate = DateUtil.getToday();
            String beginDate = DateUtil.addDate( endDate, "D", "yyyyMMdd", -10 );


            // 삼성전자 데이터로, 모든 최근 10일 목록을 만들자
            Document document = getStockPriceInfo.getDocument( "", "KR7005930003", "", beginDate, endDate, 1, 10000 );
            ArrayList<GetPriceStockInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );

            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 10일 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 조회하자
            for ( GetPriceStockInfoResult samsungResult : samsungList ) {

                // 목록 초기화
                ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 조회 시작 : " + time);

                // 특정 날짜의 전체 종목 데이터 가져옴
                int failCount = 0;
                while ( true ) {
                    try {
                        document = getStockPriceInfo.getDocument("", "", samsungResult.getBasDt(), "", "", 1, 10000);
                        break;
                    } catch (NetworkCommunicationFailureException ncfe) {
                        failCount++;
                        Thread.sleep( 5000 );
                        if ( failCount >= 3 ) throw ncfe;
                    }
                }

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 조회 종료 : " + time);

                ArrayList<GetPriceStockInfoResult> list = getStockPriceInfo.getStockPriceInfoList( document );

                // StockDaily 형태로 변환
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 변환 시작 : " + time);
                for ( GetPriceStockInfoResult result : list ) {
                    StockDaily stockDaily = stockDailyService.getStockDailyFromFscStockInfo( result );
                    stockDailyList.add( stockDaily );
                }
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 변환 종료 : " + time);

                // 변환된 녀석을 저장
                for ( StockDaily stockDaily : stockDailyList ) {
                    time = System.currentTimeMillis();
                    log.debug( batch + "특정종목 (" + stockDaily.getStockName() + ") 데이터 저장 시작 : " + time);
                    stockDailyDao.saveStockDaily( stockDaily );
                    time = System.currentTimeMillis();
                    log.debug( batch + "특정종목 (" + stockDaily.getStockName() + ") 데이터 저장 종료 : " + time);
                }
            }

        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug( batch + "종료 : " + time);
        }

    }
}
