package kr.co.glog.batch;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
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

    //스프링 스케줄러 / 쿼츠 크론 표현식
    //초		분		시		일			월		요일			연도
    //0-59	0-55	0-23	1-31 / ?	1-12	0-6 / ?		생략가능

    // 코스피 코스닥 전 종목 조회 후 stock 테이블에 등록
    @Scheduled(cron = "0 31 16 13 * * ")
    /*@Scheduled(fixedRate = 999999999)*/
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
    @Scheduled(cron = "0 12 12 08 * *")
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
    @Scheduled(cron = "0 00 16 * * *")
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
            daumDailyStockScrapper.upsertDailyStock( stockResult.getStockCode() );
            daumDailyInvestorScrapper.upsertDailyStock( stockResult.getStockCode() );
        }

        log.info( "다음증권 일별 데이터 전 종목 첫페이지 Upsert 정상종료 ");
    }
}