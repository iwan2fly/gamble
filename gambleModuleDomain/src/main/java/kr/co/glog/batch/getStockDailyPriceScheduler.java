package kr.co.glog.batch;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stock.dao.DartCorpDao;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.model.*;
import kr.co.glog.external.dartApi.DartCompanyApi;
import kr.co.glog.external.dartApi.DartCorpCodeApi;
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
public class getStockDailyPriceScheduler {

    private final NaverDailyStockPriceScrapper naverDailyStockPriceScrapper;
    private final StockRankingScrapper stockRankingScrapper;
    private final DaumDailyStockScrapper daumDailyStockScrapper;
    private final DaumDailyInvestorScrapper daumDailyInvestorScrapper;
    private final StockDao stockDao;
    private final StockDailyDao stockDailyDao;
    private final DartCorpCodeApi dartCorpCodeApi;
    private final DartCorpDao dartCorpDao;
    private final DartCompanyApi dartCompanyApi;

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

    // 최초 증권종목 데이터 생성 시 1회만 사용하면 됨
    // 투자자별 거래 전체 데이터 업데이트
    // @Scheduled(cron = "0 12 12 08 * *")
    public void allStockDailyInvestorUpdate() throws InterruptedException {

        // 전종목 조회
        StockParam stockParam = new StockParam();
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> stockList = stockDao.getStockList(stockParam);

        for ( StockResult stockResult : stockList ) {

            // 이미 등록되어있는 녀석은 패스
            /*
            StockDailyParam stockDailyParam = new StockDailyParam();
            stockDailyParam.setStockCode( stockResult.getStockCode() );
            ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
            if ( stockDailyList != null && stockDailyList.size() > 1 ) {
                log.debug( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] : (" + stockDailyList.size() + ") is Already inserted") ;
                continue;
            }
            */

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





    // 다음 증권 일별가격 첫 페이지 ( 10개 ) 만 Upsert
    @Scheduled(cron = "0 54 19 * * *")
    public void stockDailyDataUpsert() throws InterruptedException {

        log.info( "다음증권 일별 데이터 전 종목 첫페이지 Upsert 시작 ");

        // 코스피 전종목 등록
        stockRankingScrapper.registerStock("KOSPI");

        // 코스닥 전종목 등록
        stockRankingScrapper.registerStock("KOSDAQ");

        // 전종목 조회
        StockParam stockParam = new StockParam();
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "stockCode" );
        stockParam.setPagingParam( pagingParam );
        ArrayList<StockResult> stockList = stockDao.getStockList(stockParam);


        for ( StockResult stockResult : stockList ) {

            // 일별데이터 업서트 최대 3번 시도
            boolean isSuccess = false;
            for ( int i = 0; i < 3; i++ ) {

                try {
                    daumDailyStockScrapper.upsertDailyStock(stockResult.getStockCode());
                    isSuccess = true;
                } catch ( Exception e ) {
                    isSuccess = false;
                }

                if ( isSuccess ) break;
            }

            if ( !isSuccess ) throw new ApplicationRuntimeException( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 일별데이터 업서트 3회 시도 실패로 배치처리를 중지합니다.");

            isSuccess = false;
            for ( int i = 0; i < 3; i++ ) {

                try {
                    daumDailyInvestorScrapper.upsertDailyStock( stockResult.getStockCode() );
                    isSuccess = true;
                } catch ( Exception e ) {
                    isSuccess = false;
                }

                if ( isSuccess ) break;
            }

            if ( !isSuccess ) throw new ApplicationRuntimeException( stockResult.getStockName() + "[" + stockResult.getStockCode() + "] 일별데이터 업서트 3회 시도 실패로 배치처리를 중지합니다.");


        }

        log.info( "다음증권 일별 데이터 전 종목 첫페이지 Upsert 정상종료 ");
    }


    /**
     * DART의 고유번호 파일 다운로드 받아서, 있는 건 업데이트하고, 없는 건 등록
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 47 09 19 * * ")
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
    @Scheduled(cron = "0 23 10 19 * * ")
    public void updateDartCompany() throws InterruptedException {

        log.info( "corpCodeApi START" );
        try {
            DartCorpParam dartCorpParam = new DartCorpParam();
            dartCorpParam.setHasStockCode( true );
            ArrayList<DartCorpResult> dartCorpList = dartCorpDao.getDartCorpList( dartCorpParam );
            for ( DartCorpResult dartCorpResult : dartCorpList ) {
                dartCompanyApi.updateDartCompany(dartCorpResult.getCorpCode());
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        log.info( "coprCodeApi END" );

    }
}