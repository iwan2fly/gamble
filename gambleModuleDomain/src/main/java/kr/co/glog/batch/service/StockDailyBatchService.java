package kr.co.glog.batch.service;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
import kr.co.glog.external.datagokr.fsc.GetStockPriceInfo;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDailyBatchService {

    private final GetStockPriceInfo getStockPriceInfo;          // 일별 주식 시세 - 금융위원회
    private final StockDao stockDao;
    private final StockDailyService stockDailyService;
    private final StockDailyDao stockDailyDao;


    /**
     * KRX의 경우, 2020.01.02 가 가져올 수 있는 가장 옛날 데이터
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
            ArrayList<GetStockPriceInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );

            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 모든 날짜 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 조회하자
            for ( GetStockPriceInfoResult samsungResult : samsungList ) {

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

                ArrayList<GetStockPriceInfoResult> list = getStockPriceInfo.getStockPriceInfoList( document );

                // StockDaily 형태로 변환
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 변환 시작 : " + time);
                for ( GetStockPriceInfoResult result : list ) {
                    StockDaily stockDaily = stockDailyService.convertToStockDailyFromFscStockInfo( result );
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
     * KRX 데이터로, 최근 10일 데이터 업서트
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
            ArrayList<GetStockPriceInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );

            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 10일 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 조회하자
            for ( GetStockPriceInfoResult samsungResult : samsungList ) {

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

                ArrayList<GetStockPriceInfoResult> list = getStockPriceInfo.getStockPriceInfoList( document );

                // StockDaily 형태로 변환
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 변환 시작 : " + time);
                for ( GetStockPriceInfoResult result : list ) {
                    StockDaily stockDaily = stockDailyService.convertToStockDailyFromFscStockInfo( result );
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
     *  다음 페이지에서 코스피 코스닥 전 종목 조회해서 upsert
     */
    public void upsertDailyStockDataBatchFromDaumDaily() {

        String batch = "[BATCH] 일별 KOSPI, KOSDAQ, KONEX 종목 업데이트 - ";

        long startTime = System.currentTimeMillis();
        log.debug(batch + "시작 : " + startTime);

        // 페이징 파라미터, 종목코드로 정렬
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex("stockCode");

        // 코스피 전종목 조회
        StockParam stockParam = new StockParam();
        stockParam.setMarketCode("kospi");
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> kospiList = stockDao.getStockList(stockParam);

        // 코스닥
        stockParam.setMarketCode("kosdaq");
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> kosdaqList = stockDao.getStockList(stockParam);

        // 코넥스
        stockParam.setMarketCode("konex");
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> konexList = stockDao.getStockList(stockParam);

        ArrayList<StockResult> stockList = new ArrayList<StockResult>();
        stockList.addAll(kospiList);
        stockList.addAll(kosdaqList);
        stockList.addAll(konexList);


        for (StockResult stockResult : stockList) {

            // 일별데이터 업서트 최대 3번 시도
            boolean isSuccess = false;
            for (int i = 0; i < 3; i++) {

                try {
                    stockDailyService.upsertStockDailyFromDaumDaily( stockResult.getStockCode() );
                    isSuccess = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                }

                if (isSuccess) break;
            }

            if (!isSuccess) {
                long exceptionTime = System.currentTimeMillis();
                log.debug( batch + "에러 : " + exceptionTime);
                throw new ApplicationRuntimeException(stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 일별데이터 업서트 3회 시도 실패로 배치처리를 중지합니다.");
            }
        }
    }


    public void upsertDailyStockDataBatchFromDaumInvestor() {

        String batch = "[BATCH] 일별/투자자별 KOSPI, KOSDAQ, KONEX 종목 업데이트 - ";

        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );

        // 코스피 전종목 조회
        StockParam stockParam = new StockParam();
        stockParam.setMarketCode("kospi");
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> kospiList = stockDao.getStockList(stockParam);

        // 코스닥
        stockParam.setMarketCode("kosdaq");
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> kosdaqList = stockDao.getStockList(stockParam);

        // 코넥스
        stockParam.setMarketCode("konex");
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> konexList = stockDao.getStockList(stockParam);

        ArrayList<StockResult> stockList = new ArrayList<StockResult>();
        stockList.addAll( kospiList );
        stockList.addAll( kosdaqList );
        stockList.addAll( konexList );


        for ( StockResult stockResult : stockList ) {

            // 투자자별데이터 업서트 최대 3번 시도
            boolean isSuccess = false;
            for ( int i = 0; i < 3; i++ ) {

                try {
                    stockDailyService.upsertStockDailyFromDaumInvestor( stockResult.getStockCode() );
                    isSuccess = true;
                } catch ( Exception e ) {
                    e.printStackTrace();
                    isSuccess = false;
                }

                if ( isSuccess ) break;
            }

            if (!isSuccess) {
                long exceptionTime = System.currentTimeMillis();
                log.debug(batch + "에러 : " + exceptionTime);
                throw new ApplicationRuntimeException(stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 일별데이터 업서트 3회 시도 실패로 배치처리를 중지합니다.");
            }
        }
    }
}
