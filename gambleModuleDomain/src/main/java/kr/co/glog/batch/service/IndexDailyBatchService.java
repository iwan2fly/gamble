package kr.co.glog.batch.service;

import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.IndexDailyService;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.external.datagokr.fsc.GetMarketIndexInfo;
import kr.co.glog.external.datagokr.fsc.model.GetMarketIndexInfoResult;
import kr.co.glog.external.datagokr.fsc.model.GetMarketIndexInfoResult;
import kr.co.glog.external.daumFinance.DaumDailyIndexScrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDailyBatchService {

    private final GetMarketIndexInfo getMarketIndexInfo;          // 일별 지수 시세 - 금융위원회
    private final IndexDailyService indexDailyService;
    private final IndexDailyDao indexDailyDao;

    private final DaumDailyIndexScrapper daumDailyIndexScrapper;

    public void upsertFromKrxDataByDate( String yyyymmdd, String marketCode ) {

        String batch = "[BATCH] 일별 종목 거래데이터 업데이트 - 금융위원회 지수정보";

        long startTime = System.currentTimeMillis();
        log.debug( batch + "시작 : " + startTime);

        ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
        try {

            String indexName = marketCode.equals( MarketCode.kospi ) ? "코스피" : "코스닥";

            // 한 방에 전체 목록 가져옴
            Document document = getMarketIndexInfo.getDocument( indexName, yyyymmdd, "", "", 1, 10000 );
            ArrayList<GetMarketIndexInfoResult> list = getMarketIndexInfo.getMarketIndexInfoList( document );

            // Index 형태로 변환
            for ( GetMarketIndexInfoResult result : list ) {
                IndexDaily indexDaily = indexDailyService.getIndexkDailyFromFscMarketInfo( marketCode, result );
                indexDailyList.add( indexDaily );
            }

            // 변환된 녀석을 저장
            for ( IndexDaily indexDaily : indexDailyList ) {
                indexDailyDao.saveIndexDaily( indexDaily );
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
    public void upsertFromKrxDataAll( String marketCode ) {

        String batch = "[BATCH] 일별 지수 데이터 전체 업데이트 - 금융위원회 지수정보";

        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            String indexName = marketCode.equals( MarketCode.kospi ) ? "코스피" : "코스닥";

            time = System.currentTimeMillis();
            log.debug( batch + "데이터 가져오기 : " + time);

            Document document = getMarketIndexInfo.getDocument( indexName, "", "", "20221231", 1, 10000 );
            ArrayList<GetMarketIndexInfoResult> marketIndexList = getMarketIndexInfo.getMarketIndexInfoList( document );
            ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
            for ( GetMarketIndexInfoResult result : marketIndexList ) {
                IndexDaily indexDaily = indexDailyService.getIndexkDailyFromFscMarketInfo( marketCode, result );
                indexDailyList.add( indexDaily );
            }

            time = System.currentTimeMillis();
            log.debug( batch + "데이터 가져오기 완료 : " + time);

            // 변환된 녀석을 저장
            for ( IndexDaily indexDaily : indexDailyList ) {
                time = System.currentTimeMillis();
                log.debug( batch + "특정종목 (" + indexDaily.getTradeDate() + ") 데이터 저장 시작 : " + time);
                indexDailyDao.saveIndexDaily( indexDaily );
                time = System.currentTimeMillis();
                log.debug( batch + "특정종목 (" + indexDaily.getTradeDate() + ") 데이터 저장 종료 : " + time);
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
    public void upsertFromKrxData10( String marketCode ) {

        String batch = "[BATCH] 일별 지수 데이터 전체 업데이트 - 금융위원회 지수정보";

        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            String indexName = marketCode.equals( MarketCode.kospi ) ? "코스피" : "코스닥";
            String endDate = DateUtil.getToday();
            String beginDate = DateUtil.addDate( endDate, "D", "yyyyMMdd", -10 );

            time = System.currentTimeMillis();
            log.debug( batch + "데이터 가져오기 : " + time);

            Document document = getMarketIndexInfo.getDocument( indexName, "", beginDate, endDate, 1, 10000 );
            ArrayList<GetMarketIndexInfoResult> marketIndexList = getMarketIndexInfo.getMarketIndexInfoList( document );
            ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
            for ( GetMarketIndexInfoResult result : marketIndexList ) {
                IndexDaily indexDaily = indexDailyService.getIndexkDailyFromFscMarketInfo( marketCode, result );
                indexDailyList.add( indexDaily );
            }

            time = System.currentTimeMillis();
            log.debug( batch + "데이터 가져오기 완료 : " + time);

            // 변환된 녀석을 저장
            for ( IndexDaily indexDaily : indexDailyList ) {
                time = System.currentTimeMillis();
                log.debug( batch + "특정종목 (" + indexDaily.getTradeDate() + ") 데이터 저장 시작 : " + time);
                indexDailyDao.saveIndexDaily( indexDaily );
                time = System.currentTimeMillis();
                log.debug( batch + "특정종목 (" + indexDaily.getTradeDate() + ") 데이터 저장 종료 : " + time);
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
     *  다음 페이지에서 코스피 코스닥 조회해서 upsert
     */
    public void upsertDailyData() {

        String batch = "[BATCH] 일별 KOSPI, KOSDAQ 지수 업데이트 - ";

        long startTime = System.currentTimeMillis();
        log.debug( batch + "시작 : " + startTime);

        try {
            daumDailyIndexScrapper.upsertDailyIndex("kospi");
            daumDailyIndexScrapper.upsertDailyIndex("kosdaq");
        } catch ( InterruptedException ie ) {
            long exceptionTime = System.currentTimeMillis();
            log.debug( batch + "에러 : " + exceptionTime);
        } finally {
            long endTime = System.currentTimeMillis();
            log.debug( batch + "종료 : " + endTime);
        }

    }
}
