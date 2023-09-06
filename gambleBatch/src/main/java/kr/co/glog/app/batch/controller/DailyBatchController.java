package kr.co.glog.app.batch.controller;


import kr.co.glog.batch.service.IndexDailyBatchService;
import kr.co.glog.batch.service.StockDailyBatchService;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.*;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.PeriodCode;
import kr.co.glog.domain.stock.dao.CompanyDao;

import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.model.CompanyParam;
import kr.co.glog.domain.stock.model.CompanyResult;
import kr.co.glog.domain.stock.model.IndexDailyParam;
import kr.co.glog.domain.stock.model.IndexDailyResult;
import kr.co.glog.external.dartApi.DartCompanyApi;
import kr.co.glog.external.dartApi.DartCorpCodeApi;
import kr.co.glog.external.dartApi.DartFinancialApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyBatchController {

    private final StockDailyBatchService stockDailyBatchService;
    private final IndexDailyBatchService indexDailyBatchService;
    private final DartCorpCodeApi dartCorpCodeApi;
    private final DartCompanyApi dartCompanyApi;
    private final DartFinancialApi dartFinancialApi;
    private final CompanyDao companyDao;
    private final StockDao stockDao;
    private final StatIndexService statIndexService;


    //스프링 스케줄러 / 쿼츠 크론 표현식
    //초		분		시		일			월		요일			연도
    //0-59	0-55	0-23	1-31 / ?	1-12	0-6 / ?		생략가능
    // @Scheduled(cron = "0 31 16 13 * * ")
    // @Scheduled(fixedRate = 999999999)


    /**
     * DART의 고유번호 파일 다운로드 받아서, 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 0 0 * * * ")
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
    @Scheduled(cron = "0 1 0 * * * ")
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
                if ( dartCorpResult.getCompanyCode().equals("99999999") ) continue;
                dartCompanyApi.updateCompany(dartCorpResult.getCompanyCode());
                Thread.sleep( 100 );        // DART 의 경우 1분에 1000회 이상 호출할경우 24시간 IP 차단 방지
            }

            stockDao.updateCompanyCodeFromCompany();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        log.info( "coprCodeApi END" );

    }


    /**
     *  DART 의 고유번호로 특정회사 재무정보 가져와서 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
    //초		분		시		일			월		요일			연도
    @Scheduled(cron = "0 8 18 31 * * ")
    public void updateCompanyFinancialInfo() throws InterruptedException {

         /*
                1분기보고서 : 11013
                반기보고서 : 11012
                3분기보고서 : 11014
                사업보고서 : 11011
        */

        log.info( "dartFinancialApi START" );
        String result = "SUCCESS";
        String year = "2023";
        String reportCode = "11013";

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







    /**
     *  낮 12시 배치
     *  금융위원회 자료로 지수/종목 정보 업데이트
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 30 11 * * * ")
    public void at12() throws InterruptedException {

        // 금융위원회 자료로 코스피 지수 업서트
        indexDailyBatchService.upsertFromKrxData10( MarketCode.kospi );

        // 금융위원회 자료로 코스닥 지수 업서트
        indexDailyBatchService.upsertFromKrxData10( MarketCode.kosdaq );

        // 금융위원회 정보로 지수를 업데이트 했으니, 지수통계도 없데이트
        indexDailyBatchService.makeStatIndexToday();


        // 금융위원회 자료로 코스피 코스닥 전 종목 조회 후 stock 테이블에 등록
        stockDailyBatchService.upsertFromKrxData10();

        // 금융위원회 자료로 코스피 코스닥 전 종목 업데이트 했으니, 종목별 통계도 업데이트
        stockDailyBatchService.makeStatStockToday();
    }


    /**
     * 오후 3시 35분 배치
     * 다음 주식 정보로 당일 지수 / 종목 정보 업서트
     * @throws InterruptedException
     */
    // @Scheduled(cron = "0 34 15 * * *")
    @Scheduled(cron = "0 15 17 * * *")
    public void at1535() throws InterruptedException {

        // 다음 데이터로 지수 UPSERT
        indexDailyBatchService.upsertDailyIndexDataBatchFromDaum();

        // 다음 데이터 정보로 지수를 업데이트 했으니, 지수통계도 없데이트
        indexDailyBatchService.makeStatIndexToday();


        // 다음 데이터로 종목 UPSERT
        stockDailyBatchService.upsertDailyStockDataBatchFromDaumDaily();

        // 다음 데이터로 자료로 코스피 코스닥 전 종목 업데이트 했으니, 종목별 통계도 업데이트
        stockDailyBatchService.makeStatStockToday();


        // 종목 정보도 업데이트 되었으니, 일별/주간/월간/연간, 상승/보합/하락 종목 수 업데이트
        indexDailyBatchService.updateStockCountStatToday();

        // 오늘 일간 순위
        stockDailyBatchService.makeDailyRankStockToday();

        // 오늘의 주간/월간/년간 순위
        stockDailyBatchService.makePeriodRankStockToday();

    }

    /**
     * 오후 20시 배치
     *  주식 종목에 대해서 외인 / 기관 거래정보 업서트
     * @throws InterruptedException
     */
    // 20시 배치 : 외인/기관 거래정보 업서트
    @Scheduled(cron = "0 0 20 * * *")
    public void at2000() throws InterruptedException {


        // 다음 외인 / 기관 거래 정보
        stockDailyBatchService.upsertDailyStockDataBatchFromDaumInvestor();

        // 다음 외인 / 기관 정보로 주식을 업데이트 했으니, 주식 동계도 없데이트
        stockDailyBatchService.makeStatStockToday();


        // 오늘 일간 순위
        stockDailyBatchService.makeDailyRankStockToday();
        // 오늘의 주간/월간/년간 순위
        stockDailyBatchService.makePeriodRankStockToday();
    }



    @Scheduled(cron = "0 42 10 25 * * ")
    public void test() throws InterruptedException {


        // 오늘 일간 순위
       // stockDailyBatchService.makeDailyRankStockToday();

        // 오늘의 주간/월간/년간 순위
        stockDailyBatchService.makePeriodRankStockToday();


        /*
        // 오늘 일간 순위
        stockDailyBatchService.makeDailyRankStockToday();
        // 오늘의 주간/월간/년간 순위
        stockDailyBatchService.makePeriodRankStockToday();
        statIndexService.makeStatIndexWeek(MarketCode.kospi, "20230120" );
        statIndexService.makeStatIndexWeek(MarketCode.kospi, "20230127" );
        statIndexService.makeStatIndexWeek(MarketCode.kospi, "20230203" );
        statIndexService.makeStatIndexWeek(MarketCode.kospi, "20230210" );
        statIndexService.makeStatIndexWeek(MarketCode.kospi, "20230217" );
        */
/*
        stockDailyBatchService.makeStatStockWeek( "20230130" );
        stockDailyBatchService.makeStatStockWeek( "20230206" );
        stockDailyBatchService.makeStatStockWeek( "20230213" );
        stockDailyBatchService.makeStatStockWeek( "20230220" );

        statIndexService.makeStatIndexWeek( MarketCode.kospi, "20230130");
        statIndexService.makeStatIndexWeek( MarketCode.kospi, "20230206");
        statIndexService.makeStatIndexWeek( MarketCode.kospi, "20230213");

        statIndexService.makeStatIndexWeek( MarketCode.kosdaq, "20230130");
        statIndexService.makeStatIndexWeek( MarketCode.kosdaq, "20230206");
        statIndexService.makeStatIndexWeek( MarketCode.kosdaq, "20230213");


        statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kospi, PeriodCode.week, "202305" );
        statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kosdaq, PeriodCode.week, "202305" );

        statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kospi, PeriodCode.week, "202306" );
        statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kosdaq, PeriodCode.week, "202306" );

        statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kospi, PeriodCode.week, "202307" );
        statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kosdaq, PeriodCode.week, "202307" );


        for ( int i = 202101; i <= 202112; i++ ) {
            stockDailyBatchService.makeStatStockWeek( i + "01");
        }

        for ( int i = 202201; i <= 202212; i++ ) {
            stockDailyBatchService.makeStatStockMonth( i + "01");
        }
*/




       // stockDailyBatchService.makeStatStock("20221228");
      //  statIndexService.makeStatIndexYear( "kospi", "2021");
    //    statIndexService.makeStatIndexYear( "kospi", "2022");
     //   statIndexService.makeStatIndexYear( "kosdaq", "2021");
     //   statIndexService.makeStatIndexYear( "kosdaq", "2022");
       // indexDailyBatchService.updateStockCountStat( "20221228");

     //   statIndexService.makeStatIndexMonth( MarketCode.kospi, "20210131" );
    //    statIndexService.makeStatIndexMonth( MarketCode.kosdaq, "20230131" );

        // 월별통계 및 상숭하락주식수 업댓
        /*
        for ( int year = 2020; year < 2023; year++ ) {
            for ( int month = 1; month <=12; month++ ) {

                statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kospi, PeriodCode.month, "" + year + ( month < 10 ? "0" + month : "" + month ) );
                statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kosdaq, PeriodCode.month, "" + year + ( month < 10 ? "0" + month : "" + month ) );
            }
        }

         */

      //  statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kospi, PeriodCode.month, "202201" );
     //   statIndexService.updateRiseFallCountFromStockIndex( MarketCode.kosdaq, PeriodCode.month, "202201" );



        // stockDailyBatchService.makeDailyRankStock( "20230217" );
        // stockDailyBatchService.makePeriodRankStock( "20230217" );
        // periodRankStockService.makePeriodRank( MarketCode.kospi, PeriodCode.year, "rateChange", "202300" );
        // periodRankStockService.makePeriodRank( MarketCode.kosdaq, PeriodCode.year, "rateChange", "202300" );


/*
        int date = 20230102;
        while ( date < 20230127 ) {
            String dateStr = "" + date;
            statStockService.makeStatStockWeek( dateStr );

            dateStr = dateUtil.addDate( dateStr, "D", "yyyyMMdd", 7 );
            date = Integer.parseInt( dateStr );
        }

 */
/*
        for ( int i = 1; i < 2; i++ ) {
            String month = i < 9 ? "0"+ i : ""+i;
            stockDailyBatchService.upsertFromKrxDataMonthOf( 2021 + month );
        }

        for ( int i = 1; i <= 12; i++ ) {
            String month = i < 9 ? "0"+ i : ""+i;
            stockDailyBatchService.upsertFromKrxDataMonthOf( 2020 + month );
        }
*/
        // 다음 데이터로 종목 UPSERT
//        stockDailyBatchService.upsertDailyStockDataBatchFromDaumDaily();
        //    stockDailyService.upsertStockDailyFromDaumDaily( "000020" );

        //    stockDailyBatchService.upsertDailyStockDataBatchFromDaumDaily();
        //      stockDailyBatchService.makeStatStockToday();


        //    stockDailyService.upsertDailyStockFromKrx( "049960" );
        // stockDailyService.upsertDailyDateFromKrx( "20230117" );
        // stockDailyService.insertDailyStockAllFromDaumInvestor( "049960" );

        /*
        statStockService.makeStatStockYear( "2020" );
        statStockService.makeStatStockYear( "2021" );
        statStockService.makeStatStockYear( "2022" );

        // 월간waw
        for ( int year = 2020; year <= 2022; year++ ) {
            for (int i = 1; i <= 12; i++) {
                String month = i < 10 ? "0" + i : "" + i;
                statStockService.makeStatStockMonth( "" + year + month );
            }
        }


        int date = 20230101;
        while ( date < 20230113 ) {
            String dateStr = "" + date;
            statStockService.makeStatStockWeek( dateStr );

            dateStr = dateUtil.addDate( dateStr, "D", "yyyyMMdd", 7 );
            date = Integer.parseInt( dateStr );
        }
*/
    }
}
