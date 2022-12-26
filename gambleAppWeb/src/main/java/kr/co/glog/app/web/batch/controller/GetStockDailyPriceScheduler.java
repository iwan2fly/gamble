package kr.co.glog.app.web.batch.controller;

import kr.co.glog.batch.service.IndexDailyBatchService;
import kr.co.glog.batch.service.StockDailyBatchService;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.StatIndexService;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.dao.CompanyFinancialInfoDao;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.model.*;
import kr.co.glog.external.dartApi.DartCompanyApi;
import kr.co.glog.external.dartApi.DartCorpCodeApi;
import kr.co.glog.external.dartApi.DartFinancialApi;
import kr.co.glog.external.datagokr.fsc.GetStockPriceInfo;
import kr.co.glog.external.datagokr.seibro.GetShortnByMartN1;
import kr.co.glog.external.datagokr.seibro.GetStkIsinByShortIsinN1;
import kr.co.glog.external.daumFinance.DaumDailyIndexScrapper;
import kr.co.glog.external.daumFinance.DaumDailyInvestorScrapper;
import kr.co.glog.external.daumFinance.DaumDailyStockScrapper;
import kr.co.glog.external.daumFinance.StockRankingScrapper;
import kr.co.glog.external.naverStock.NaverDailyStockPriceScrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetStockDailyPriceScheduler {

    private final NaverDailyStockPriceScrapper naverDailyStockPriceScrapper;
    private final StockRankingScrapper stockRankingScrapper;
    private final DaumDailyStockScrapper daumDailyStockScrapper;
    private final DaumDailyInvestorScrapper daumDailyInvestorScrapper;
    private final StockDao stockDao;
    private final StockDailyDao stockDailyDao;
    private final CompanyDao companyDao;
    private final DartCorpCodeApi dartCorpCodeApi;
    private final DartCompanyApi dartCompanyApi;
    private final GetShortnByMartN1 getShorByMartN1;
    private final GetStkIsinByShortIsinN1 getStkIsinByShortIsinN1;
    private final DartFinancialApi dartFinancialApi;
    private final CompanyFinancialInfoDao companyFinancialInfoDao;

    private final DaumDailyIndexScrapper daumDailyIndexScrapper;

    private final GetStockPriceInfo getStockPriceInfo;
    private final StockDailyBatchService stockDailyBatchService;
    private final IndexDailyBatchService indexDailyBatchService;
    private final StatIndexService statIndexService;


    //스프링 스케줄러 / 쿼츠 크론 표현식
    //초		분		시		일			월		요일			연도
    //0-59	0-55	0-23	1-31 / ?	1-12	0-6 / ?		생략가능

    // 코스피 코스닥 전 종목 조회 후 stock 테이블에 등록
    // @Scheduled(cron = "0 31 16 13 * * ")
    // @Scheduled(fixedRate = 999999999)
    public void runFixCudLog() throws InterruptedException {

        log.info( "getStockDailyPriceScheduler START" );
        try {
            stockRankingScrapper.registerStock("KOSPI");
            stockRankingScrapper.registerStock("KOSDAQ");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        log.info( "getStockDailyPriceScheduler END" );

    }


    // 최초 증권종목 데이터 생성 ( 1회만 사용하면 됨 )
    // 시간이 얼마나 걸릴까?
    // @Scheduled(cron = "0 39 18 07 * *")
    public void allStockDailyDataInsert() throws InterruptedException {

        long startTime = System.currentTimeMillis();

        // 코스피 전종목 등록
        // stockRankingScrapper.registerStock("KOSPI");

        // 코스닥 전종목 등록
        // stockRankingScrapper.registerStock("KOSDAQ");

        long middleTime = System.currentTimeMillis();

        // 전종목 조회
        StockParam stockParam = new StockParam();
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> stockList = stockDao.getStockList(stockParam);

        for ( StockResult stockResult : stockList ) {

            // 이미 등록되어있는 녀석은 패스
            StockDailyParam stockDailyParam = new StockDailyParam();
            stockDailyParam.setStockCode( stockResult.getStockCode() );
            ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
            if ( stockDailyList != null && stockDailyList.size() > 1 ) {
                log.debug( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] : (" + stockDailyList.size() + ") is Already inserted") ;
                continue;
            }

            daumDailyStockScrapper.insertDailyStockFullData( stockResult.getStockCode() );

        }

        long endTime = System.currentTimeMillis();

        log.debug( "시작시간 : " + startTime  + " : " + DateUtil.getCurrentTime() );
        log.debug( "중간시간 : " + middleTime  + " : " + DateUtil.getCurrentTime() );
        log.debug( "종료시간 : " + endTime  + " : " + DateUtil.getCurrentTime() );
    }


    // KOSPI, KOSDAQ INDEX 일별 데이터 전체 등록
    @Scheduled(cron = "0 35 15 * * *")
    public void allIndexDailyDataInsert() throws InterruptedException {

        long startTime = System.currentTimeMillis();

        daumDailyIndexScrapper.insertDailyIndexFullData( "kospi" );
        daumDailyIndexScrapper.insertDailyIndexFullData( "kosdaq" );

        long endTime = System.currentTimeMillis();
        log.debug( "시작시간 : " + startTime  + " : " + DateUtil.getCurrentTime() );
        log.debug( "종료시간 : " + endTime  + " : " + DateUtil.getCurrentTime() );
    }


    // 최초 증권종목 데이터 생성 시 1회만 사용하면 됨
    // 투자자별 거래 전체 데이터 업데이트
 //   @Scheduled(cron = "0 11 15 05 * *")
    public void allStockDailyInvestorUpdate() throws InterruptedException {

        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );

        // 코스피, 코스닥, 코넥스 전종목 조회
        StockParam stockParam = new StockParam();
        stockParam.setPagingParam( pagingParam );
        stockParam.setMarketCode("kospi");
        stockParam.setStartStockCode("140700");

        ArrayList<StockResult> kospiList = stockDao.getStockList(stockParam);
        stockParam.setPagingParam( pagingParam );
        stockParam.setMarketCode("kosdaq");

        ArrayList<StockResult> kosdaqList = stockDao.getStockList(stockParam);
        stockParam.setPagingParam( pagingParam );
        stockParam.setMarketCode("konex");

        ArrayList<StockResult> konexList = stockDao.getStockList(stockParam);
        ArrayList<StockResult> stockList = new ArrayList<StockResult>();
        stockList.addAll( kospiList );
        stockList.addAll( kosdaqList );
        stockList.addAll( konexList );

        for ( StockResult stockResult : stockList ) {
            daumDailyInvestorScrapper.updateDailyInvestorFullData( stockResult.getStockCode() );
        }
    }

    // 투자자별 거래 빠진 데이터 업데이트
    //@Scheduled(cron = "0 34 09 14 * *")
    public void missingStockDailyInvestorUpdate() throws InterruptedException {

        // 전종목 조회
        StockParam stockParam = new StockParam();
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> stockList = stockDao.getStockList(stockParam);

        for ( StockResult stockResult : stockList ) {

            // 이미 등록되어있는 녀석은 패스
            StockDailyParam stockDailyParam = new StockDailyParam();
            stockDailyParam.setStockCode( stockResult.getStockCode() );
            stockDailyParam.setTradeDate( "20220825" );
            ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
            if ( stockDailyList == null || stockDailyList.size() == 0 ) {
                log.debug( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] : (" + stockDailyList.size() + ") is NULL PASS") ;
                continue;
            }

            // 해당일자에 데이터가 있으나 기관보유량 값이 없으면 해당 종목의 전체 투자자별 데이터 업데이트
            if ( stockDailyList.get(0).getVolumeOrg() == null ) {
                daumDailyInvestorScrapper.updateDailyInvestorFullData( stockResult.getStockCode() );
            }
        }
    }



    // 당일 가격정보 업서트
    @Scheduled(cron = "0 0 16 * * *")
    public void stockDailyPriceUpsert() throws InterruptedException {

        log.info( "당일 가격 정보 업데이트 배치 처리 시작");

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

            // 일별데이터 업서트 최대 3번 시도
            boolean isSuccess = false;
            for ( int i = 0; i < 3; i++ ) {

                try {
                    daumDailyStockScrapper.upsertDailyStock(stockResult.getStockCode());
                    isSuccess = true;
                } catch ( Exception e ) {
                    e.printStackTrace();
                    isSuccess = false;
                }

                if ( isSuccess ) break;
            }

            if ( !isSuccess ) throw new ApplicationRuntimeException( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 일별데이터 업서트 3회 시도 실패로 배치처리를 중지합니다.");

        }

        log.info( "당일 가격 정보 업데이트 배치 처리 정상 종료");
    }

    // 외인/기관 거래정보 업서트
    @Scheduled(cron = "0 0 20 * * *")
    public void stockDailyInvestorUpsert() throws InterruptedException {

        log.info( "당일 외인기관 거래정보 업데이트 배치 처리");

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
                    daumDailyInvestorScrapper.upsertDailyStock( stockResult.getStockCode() );
                    isSuccess = true;
                } catch ( Exception e ) {
                    e.printStackTrace();
                    isSuccess = false;
                }

                if ( isSuccess ) break;
            }

            if ( !isSuccess ) throw new ApplicationRuntimeException( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 일별데이터 업서트 3회 시도 실패로 배치처리를 중지합니다.");


        }

        log.info( "당일 외인기관 거래정보 업데이트 배치 처리 정상 종료");
    }




    /**
     * DART의 고유번호 파일 다운로드 받아서, 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
//    @Scheduled(cron = "0 11 14 07 * * ")
    public void updateCorpCode() throws InterruptedException {

        log.info( "corpCodeApi START" );
        try {
            dartCorpCodeApi.updateCorpCode();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        log.info( "coprCodeApi END" );

    }

    /**
     *  DART 의 고유번호로 기업개황 가져와서 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
  //  @Scheduled(cron = "0 16 14 04 * * ")
    public void updateCompany() throws InterruptedException {

        log.info( "corpCodeApi START" );
        try {
            CompanyParam CompanyParam = new CompanyParam();
            CompanyParam.setHasStockCode( true );
            ArrayList<CompanyResult> CompanyList = companyDao.getCompanyList( CompanyParam );
            int index=0;

            for ( CompanyResult dartCorpResult : CompanyList ) {
                index++;
                log.debug( index + " :" + dartCorpResult.toString() );
                dartCompanyApi.updateCompany(dartCorpResult.getCompanyCode());
                Thread.sleep( 100 );        // DART 의 경우 1분에 1000회 이상 호출할경우 24시간 IP 차단 방지
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        log.info( "coprCodeApi END" );

    }

    /**
     *  한국예탁결제원_주식정보서비스 : 시장별 단축코드 전체 조회
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 50 8 14 * * ")
    public void getShortByMart() throws InterruptedException {

        log.info( "Seibro getShorTbyMartN1 START" );
        try {
            getShorByMartN1.upsertMarket();
            getStkIsinByShortIsinN1.upsertMarket();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        log.info( "coprCodeApi END" );

    }


    /**
     *  DART 의 고유번호로 전체 재무정보 가져와서 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
    //@Scheduled(cron = "0 6 11 17 * * ")
    public void updateCompanyFinancialInfoReport() throws InterruptedException {

         /*
                1분기보고서 : 11013
                반기보고서 : 11012
                3분기보고서 : 11014
                사업보고서 : 11011
        */

        log.info( "dartFinancialApi START" );
        String result = "SUCCESS";
        String year = "2022";
        String reportCode = "11012";


        try {
            CompanyParam CompanyParam = new CompanyParam();
            CompanyParam.setHasStockCode( true );
            ArrayList<CompanyResult> companyList = companyDao.getCompanyList( CompanyParam );

            for ( int i = companyList.size()-1; i >= 0; i-- ) {
                CompanyResult companyResult = companyList.get(i);

                // 이미 한개라도 재무정보가 들어가 있으면 패스
                DartCompanyFinancialInfoParam companyFinancialInfoParam = new DartCompanyFinancialInfoParam();
                companyFinancialInfoParam.setCompanyCode(companyResult.getCompanyCode());
                companyFinancialInfoParam.setYear(year);
                companyFinancialInfoParam.setReportCode(reportCode);
                ArrayList<DartCompanyFinancialInfoResult> financialList = companyFinancialInfoDao.getCompanyFinancialInfoList(companyFinancialInfoParam);
                if (financialList != null && financialList.size() > 0) continue;

                log.debug(companyResult.getCompanyCode() + " : " + companyResult.getCompanyName() + " : " + companyResult.getStockCode());
                if (companyResult.getStockCode() == null || companyResult.getStockCode().trim().equals("")) {
                    log.debug("NO StockCode");
                    continue;
                }

                dartFinancialApi.updateCompanyFinancialInfo(companyResult.getCompanyCode(), year, reportCode);


                Thread.sleep( 100 );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            result = "FAIL";
        }

        log.info( "" );
        log.info( "===========================================================================" );
        log.info( "dartFinancialApi END " + result );
        log.info( "===========================================================================" );
        log.info( "" );



    }


    /**
     *  DART 의 고유번호로 특정회사 재무정보 가져와서 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 25 11 25 * * ")
    public void updateCompanyFinancialInfo() throws InterruptedException {

         /*
                1분기보고서 : 11013
                반기보고서 : 11012
                3분기보고서 : 11014
                사업보고서 : 11011
        */

        log.info( "dartFinancialApi START" );
        String result = "SUCCESS";
        String year = "2019";
        String reportCode = "11012";
        String companyCode = "00126380";

        try {
            CompanyParam CompanyParam = new CompanyParam();
            CompanyParam.setHasStockCode( true );
            ArrayList<CompanyResult> companyList = companyDao.getCompanyList( CompanyParam );

            for ( int i = companyList.size()-1; i >= 0; i-- ) {
                CompanyResult companyResult = companyList.get(i);
                dartFinancialApi.updateCompanyFinancialInfo(companyResult.getCompanyCode(), year, reportCode);
                Thread.sleep(100);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            result = "FAIL";
        }

        log.info( "" );
        log.info( "===========================================================================" );
        log.info( "dartFinancialApi END " + result );
        log.info( "===========================================================================" );
        log.info( "" );
    }



    @Scheduled(cron = "0 0 12 * * * ")
    public void upsertFromKrxData10() throws InterruptedException {

        // 코스피, 코스닥 업서트
        indexDailyBatchService.upsertFromKrxData10( MarketCode.kospi );
        indexDailyBatchService.upsertFromKrxData10( MarketCode.kosdaq );

        // 코스피 코스닥 전 종목 조회 후 stock 테이블에 등록
        stockDailyBatchService.upsertFromKrxData10();


    }



    @Scheduled(cron = "0 33 20 26 * * ")
    public void test() throws InterruptedException {
        statIndexService.makeStatIndex( MarketCode.kospi, "20220101", "20221231");
    }
}