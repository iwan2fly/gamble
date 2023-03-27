package kr.co.glog.batch.service;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.DailyRankStockService;
import kr.co.glog.domain.service.PeriodRankStockService;
import kr.co.glog.domain.service.StatStockService;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.*;
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
    private final StatStockService statStockService;
    private final IndexDailyDao indexDailyDao;
    private final DailyRankStockService dailyRankStockService;
    private final PeriodRankStockService periodRankStockService;


    /**
     * KRX의 경우, 2020.01.02 가 가져올 수 있는 가장 옛날 데이터
     * 2020.01.02 부터 모든 데이터를 가져와서 업데이트 해보자
     *
     */
    public void upsertFromKrxDataAll() {

        String batch = "[BATCH] 일별 종목 거래데이터 전체 업데이트 - 금융위원회 주식정보";
        String startDate = "20200101";
        String endDate = DateUtil.getToday();

        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 날짜 목록 확보시작 : " + time);

            // 삼성전자 데이터로, 모든 날짜 목록을 만들자
            Document document = getStockPriceInfo.getDocument( "", "005930", "", startDate, endDate, 1, 20000 );
            ArrayList<GetStockPriceInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );
            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로  날짜 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 처리하자
            for ( GetStockPriceInfoResult samsungResult : samsungList ) {

                // 목록 초기화
                ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 처리 시작 : " + time);
                stockDailyService.upsertDailyDateFromKrx( samsungResult.getBasDt() );
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 처리 종료 : " + time);
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
            Document document = getStockPriceInfo.getDocument( "", "005930", "", beginDate, endDate, 1, 10 );
            ArrayList<GetStockPriceInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );

            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 10일 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 처리하자
            for ( GetStockPriceInfoResult samsungResult : samsungList ) {

                // 목록 초기화
                ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 처리 시작 : " + time);
                stockDailyService.upsertDailyDateFromKrx( samsungResult.getBasDt() );
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 처리 종료 : " + time);
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
     * KRX 데이터로, 특정월 데이터 업서트
     */
    public void upsertFromKrxDataMonthOf( String yearMonth ) {

        String batch = "[BATCH] 일별 종목 거래데이터 " + yearMonth + "월 업데이트 - 금융위원회 주식정보";

        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 " + yearMonth + "월 목록 확보시작 : " + time);
            String startDate = yearMonth + "01";
            String endDate = yearMonth + "31";


            // 삼성전자 데이터로, 모든 최근 10일 목록을 만들자
            Document document = getStockPriceInfo.getDocument( "", "005930", "", startDate, endDate, 1, 40 );
            ArrayList<GetStockPriceInfoResult> samsungList = getStockPriceInfo.getStockPriceInfoList( document );

            time = System.currentTimeMillis();
            log.debug( batch + "삼성전자 데이터로 " + yearMonth + "월 목록 확보완료 : " + time);

            // 날짜 하나하나씩, 전체 종목 데이터를 처리하자
            for ( GetStockPriceInfoResult samsungResult : samsungList ) {

                // 목록 초기화
                ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();

                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 처리 시작 : " + time);
                stockDailyService.upsertDailyDateFromKrx( samsungResult.getBasDt() );
                time = System.currentTimeMillis();
                log.debug( batch + "특정날짜 (" + samsungResult.getBasDt() + ") 데이터 처리 종료 : " + time);
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
     *  다음 Quotes 페이지에서 코스피 코스닥 전 종목 조회해서 upsert
     */
    public void upsertDailyStockDataBatchFromDaumQuotes() throws InterruptedException {

        String batch = "[BATCH] 다음 KOSPI, KOSDAQ 당일 업데이트 - ";

        String startTime = DateUtil.getCurrentDateTime();
        log.debug(batch + "시작 : " + startTime);

        // 페이징 파라미터, 종목코드로 정렬
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex("stockCode");

        // 코스피 전종목 조회
        StockParam stockParam = new StockParam();
        stockParam.setMarketCode( MarketCode.kospi );
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> kospiList = stockDao.getStockList(stockParam);

        // 코스닥
        stockParam.setMarketCode( MarketCode.kosdaq );
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> kosdaqList = stockDao.getStockList(stockParam);

        ArrayList<StockResult> stockList = new ArrayList<StockResult>();
        stockList.addAll(kospiList);
        stockList.addAll(kosdaqList);

        for (StockResult stockResult : stockList) {

        //    Thread.sleep( 100 );

            // 일별데이터 업서트 최대 3번 시도
            boolean isSuccess = false;
            for (int i = 0; i < 3; i++) {

                try {
                    stockDailyService.upsertStockDailyFromDaumQuotes( stockResult.getStockCode() );
                    isSuccess = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                    Thread.sleep( 60000 );
                }

                if (isSuccess) break;
            }

            if (!isSuccess) {
                String exceptionTime = DateUtil.getCurrentDateTime();;
                log.debug( batch + "에러 : " + startTime + " ~ " + exceptionTime);
                throw new ApplicationRuntimeException(stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 업서트 3회 시도 실패로 배치처리를 중지합니다.");
            }
        }

        String finishTime = DateUtil.getCurrentDateTime();;
        log.debug( batch + "정상종료 : " + startTime + " ~ " + finishTime);
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
        stockParam.setMarketCode( MarketCode.kospi );
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> kospiList = stockDao.getStockList(stockParam);

        // 코스닥
        stockParam.setMarketCode( MarketCode.kosdaq );
        stockParam.setPagingParam(pagingParam);
        ArrayList<StockResult> kosdaqList = stockDao.getStockList(stockParam);

        // 코넥스
        stockParam.setMarketCode( MarketCode.konex );
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

        String batch = "[BATCH] 일별/투자자별 KOSPI, KOSDAQ 종목 업데이트 - ";

        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );

        // 코스피 전종목 조회
        StockParam stockParam = new StockParam();
        stockParam.setMarketCode( MarketCode.kospi );
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> kospiList = stockDao.getStockList(stockParam);

        // 코스닥
        stockParam.setMarketCode( MarketCode.kosdaq );
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> kosdaqList = stockDao.getStockList(stockParam);

        ArrayList<StockResult> stockList = new ArrayList<StockResult>();
        stockList.addAll( kospiList );
        stockList.addAll( kosdaqList );

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


    /**
     *  오늘의 연간/월간/주간 주식통계 upsert
     */
    public void makeStatStockToday() {

        // 데이터가 등록되어있는 가장 최근 날짜로 설정
        PagingParam pagingParam = new PagingParam();
        pagingParam.setRows(1);
        pagingParam.setSortIndex("tradeDate");
        pagingParam.setSortType("desc");

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( MarketCode.kospi );
        indexDailyParam.setEndDate( DateUtil.getToday() );
        indexDailyParam.setPagingParam( pagingParam );

        ArrayList<IndexDailyResult> indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        makeStatStock( indexDailyList.get(0).getTradeDate() );
    }

    /**
     *  특정 날짜의 연간/월간/주간 주식통계 upsert
     */
    public void makeStatStock( String yyyymmdd ) {

        String batch = "[BATCH] 연간/월간/주간 지수통계 upsert - ";
        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            String startDate = DateUtil.getFirstDateOfWeek(yyyymmdd);
            String endDate = DateUtil.getLastDateOfWeek(yyyymmdd);
            ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockListBetween( MarketCode.kospi, startDate, endDate );
            ArrayList<StockDailyResult> kosdaqList = stockDailyDao.getStockListBetween( MarketCode.kosdaq, startDate, endDate );
            stockDailyList.addAll( kosdaqList );

            int count = 0;
            for (StockDailyResult stockDailyResult : stockDailyList) {
                count++;

                log.debug(batch + " [" + count + " / " + stockDailyList.size() + "] " + stockDailyResult.getStockCode() + " 시작");

                // 연간
                statStockService.makeStatStockYear(stockDailyResult.getStockCode(), yyyymmdd.substring(0, 4));

                // 월간
                statStockService.makeStatStockMonth(stockDailyResult.getStockCode(), yyyymmdd.substring(0, 6));

                // 주간
                statStockService.makeStatStockWeek(stockDailyResult.getStockCode(), yyyymmdd);

                log.debug(batch + " [" + count + " / " + stockDailyList.size() + "] " + stockDailyResult.getStockCode() + " 종료");
            }
        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug(batch + "종료 : " + time);
        }

    }

    public void makeStatStockWeek( String yyyymmdd ) {
        String batch = "[BATCH] 주간 지수통계 upsert - ";
        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            String startDate = DateUtil.getFirstDateOfWeek(yyyymmdd);
            String endDate = DateUtil.getLastDateOfWeek(yyyymmdd);
            ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockListBetween( MarketCode.kospi, startDate, endDate );
            ArrayList<StockDailyResult> kosdaqList = stockDailyDao.getStockListBetween( MarketCode.kosdaq, startDate, endDate );
            stockDailyList.addAll( kosdaqList );

            int count = 0;
            for (StockDailyResult stockDailyResult : stockDailyList) {

                count++;
                log.debug( batch + " [" + count + " / " + stockDailyList.size() + "] " + stockDailyResult.getStockCode() + " 시작" );

                // 주간
                statStockService.makeStatStockWeek(stockDailyResult.getStockCode(), yyyymmdd);

                log.debug( batch + " [" + count + " / " + stockDailyList.size() + "] " + stockDailyResult.getStockCode() + " 종료" );
            }
        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug(batch + "종료 : " + time);
        }
    }

    public void makeStatStockMonth( String yyyymmdd ) {
        String batch = "[BATCH] 월간 지수통계 upsert - ";
        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            String startDate = DateUtil.getFirstDateOfWeek(yyyymmdd);
            String endDate = DateUtil.getLastDateOfWeek(yyyymmdd);
            ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockListBetween( MarketCode.kospi, startDate, endDate );
            ArrayList<StockDailyResult> kosdaqList = stockDailyDao.getStockListBetween( MarketCode.kosdaq, startDate, endDate );
            stockDailyList.addAll( kosdaqList );

            int count = 0;
            for (StockDailyResult stockDailyResult : stockDailyList) {

                count++;
                log.debug( batch + " [" + count + " / " + stockDailyList.size() + "] " + stockDailyResult.getStockCode() + " 시작" );

                // 주간
                statStockService.makeStatStockMonth(stockDailyResult.getStockCode(), yyyymmdd.substring(0,6));

                log.debug( batch + " [" + count + " / " + stockDailyList.size() + "] " + stockDailyResult.getStockCode() + " 종료" );
            }
        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug(batch + "종료 : " + time);
        }
    }



    /**
     *  오늘의 주식 순위 upsert
     */
    public void makeDailyRankStockToday() {

        // 데이터가 등록되어있는 가장 최근 날짜로 설정
        PagingParam pagingParam = new PagingParam();
        pagingParam.setRows(1);
        pagingParam.setSortIndex("tradeDate");
        pagingParam.setSortType("desc");

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( MarketCode.kospi );
        indexDailyParam.setEndDate( DateUtil.getToday() );
        indexDailyParam.setPagingParam( pagingParam );

        ArrayList<IndexDailyResult> indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        makeDailyRankStock( indexDailyList.get(0).getTradeDate() );
    }

    public void makeDailyRankStock( String yyyymmdd ) {
        String batch = "[BATCH] 주식 순위 upsert - ";
        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            dailyRankStockService.makeDailyRank( MarketCode.kospi, yyyymmdd );
            dailyRankStockService.makeDailyRank( MarketCode.kosdaq, yyyymmdd );
        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug(batch + "종료 : " + time);
        }
    }


    /**
     *  오늘의 주식 통계 순위 upsert
     */
    public void makePeriodRankStockToday() {

        // 데이터가 등록되어있는 가장 최근 날짜로 설정
        PagingParam pagingParam = new PagingParam();
        pagingParam.setRows(1);
        pagingParam.setSortIndex("tradeDate");
        pagingParam.setSortType("desc");

        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setMarketCode( MarketCode.kospi );
        indexDailyParam.setEndDate( DateUtil.getToday() );
        indexDailyParam.setPagingParam( pagingParam );

        ArrayList<IndexDailyResult> indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        makePeriodRankStock( indexDailyList.get(0).getTradeDate() );
    }

    public void makePeriodRankStock(String yyyymmdd ) {
        String batch = "[BATCH] 주식 통계 순위 upsert - ";
        long time = System.currentTimeMillis();
        log.debug( batch + "시작 : " + time);

        try {
            periodRankStockService.makePeriodRankYear( yyyymmdd.substring(0,4) );
            periodRankStockService.makePeriodRankMonth( yyyymmdd.substring(0,6) );
            periodRankStockService.makePeriodRankWeek( DateUtil.getYearWeek( yyyymmdd ) );
        } catch ( Exception e ) {
            time = System.currentTimeMillis();
            log.debug( batch + "에러 : " + time);
            e.printStackTrace();
        } finally {
            time = System.currentTimeMillis();
            log.debug(batch + "종료 : " + time);
        }
    }

}


