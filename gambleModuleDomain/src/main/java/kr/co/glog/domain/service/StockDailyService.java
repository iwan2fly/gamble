package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.StockResult;
import kr.co.glog.external.datagokr.fsc.GetStockPriceInfo;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import kr.co.glog.external.daumFinance.DaumDailyInvestorScrapper;
import kr.co.glog.external.daumFinance.DaumDailyStockScrapper;
import kr.co.glog.external.daumFinance.DaumQuotesScrapper;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
import kr.co.glog.external.daumFinance.model.DaumQuotes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockDailyService {

    private final DaumQuotesScrapper daumQuotesScrapper;
    private final DaumDailyStockScrapper daumDailyStockScrapper;
    private final DaumDailyInvestorScrapper daumDailyInvestorScrapper;
    private final StockDailyDao stockDailyDao;
    private final StockDao stockDao;
    private final GetStockPriceInfo getStockPriceInfo;          // 일별 주식 시세 - 금융위원회


    /**
     * KRX의 특정 종목의 전체 일별 데이터를 upsert
     * @param stockCode
     * @throws InterruptedException
     */
    public void upsertDailyStockFromKrx( String stockCode ) throws InterruptedException {

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        ArrayList<GetStockPriceInfoResult> priceList = getStockPriceInfo.getStockPriceInfo( stockCode );

        // DB에 맞게 변환
        for ( GetStockPriceInfoResult result : priceList ) {
            StockDaily stockDaily = new StockDaily( result );
            stockDailyList.add( stockDaily );
        }

        // 변환된 녀석을 저장
        for ( StockDaily stockDaily : stockDailyList ) {
            stockDailyDao.updateInsertStockDaily( stockDaily );
        }
    }

    /**
     * KRX의 특정 종목의 전체 일별 데이터를 upsert
     * @param targetDate
     * @throws InterruptedException
     */
    public void upsertDailyDateFromKrx( String targetDate ) throws InterruptedException {

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        ArrayList<GetStockPriceInfoResult> priceList = getStockPriceInfo.getDatePriceInfo( targetDate );

        // DB에 맞게 변환
        for ( GetStockPriceInfoResult result : priceList ) {
            StockDaily stockDaily = new StockDaily( result );
            stockDailyList.add( stockDaily );
        }

        // 변환된 녀석을 저장
        for ( StockDaily stockDaily : stockDailyList ) {
            stockDailyDao.updateInsertStockDaily( stockDaily );
        }

        // 조회하는 날짜가 어제 데이터이면 stock 테이블도 업데이트하자.
        // 금융위원회 주식정보는 어제(사실은 1영업일 전) 데이터가 가장 최신
        if ( targetDate.equals( DateUtil.getYesterday() ) ) {
            for (StockDaily stockDaily : stockDailyList) {
                Stock stock = new Stock(stockDaily);
                stockDao.updateInsert( stock );
            }
        }


    }


    /**
     * 특정 종목의 전체 일별 데이터를 StockDaily 테이블에 인서트
     * @param stockCode
     */
    public void insertDailyStockAllFromDaumDaily(String stockCode  ) {

        // 페이지당 100개씩
        int perPage = 100;

        // 전체 페이지 숫자부터 구함
        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        int totalPages = daumDailyStockScrapper.getTotalPages( stockCode, perPage );

        log.debug( "totalPages : " + totalPages );

        for ( int page = 1; page <= totalPages; page++ ) {

            ArrayList<DaumDailyStock> daumDailyStockList = daumDailyStockScrapper.getDailyStockList( stockCode, perPage, page );

            for ( DaumDailyStock daumDailyStock : daumDailyStockList ) {
                StockDaily stockDaily = new StockDaily( daumDailyStock );
                if ( stockDaily.getStockCode() == null || stockDaily.getStockCode().equals("") ) stockDaily.setStockCode( stockCode );
                stockDailyList.add( stockDaily );
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for ( StockDaily stockDaily : stockDailyList ) {

            // 주식명, 시장코드 가져오려고 조회
            // 현재 식
            StockResult stockResult = null;
            try {
                stockResult = stockDao.getStock( stockDaily.getStockCode() );
            } catch ( Exception e ) {

            }

            if ( stockResult != null ) {
                stockDaily.setStockName(stockResult.getStockName());
                stockDaily.setMarketCode(stockResult.getMarketCode());
                if ( stockDaily.getVolumeTotal() == null && stockResult.getStockCount() != null ) {
                    stockDaily.setVolumeTotal( stockResult.getStockCount() );
                    if ( stockDaily.getVolumeTotal() != null )  stockDaily.setPriceTotal( stockResult.getStockCount() * stockDaily.getPriceFinal() );
                }
            }

            try {
                stockDailyDao.insertStockDaily( stockDaily );
                stockDao.saveStock( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                log.debug( dke.getMessage() );
                break;
            }
        }

    }


    /**
     * 특정 종목의 전체 투자자별 데이터를 StockDaily 테이블에 업데이트
     * @param stockCode
     */
    public void insertDailyStockAllFromDaumInvestor( String stockCode  ) throws InterruptedException {

        int perPage = 100;

        Document document = daumDailyInvestorScrapper.getDocument( stockCode, perPage, 1 );
        int totalPages = daumDailyInvestorScrapper.getTotalPages( document );
        log.debug( "totalPages : " + totalPages );

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        for ( int page = 1; page <= totalPages; page++ ) {

            ArrayList<DaumInvestorStock> daumInvestorStockList = daumDailyInvestorScrapper.getDailyInvestorList( stockCode, perPage, page );

            for ( DaumInvestorStock daumInvestorStock : daumInvestorStockList ) {
                StockDaily stockDaily = new StockDaily( daumInvestorStock );
                stockDaily.setStockCode( stockCode );
                stockDailyList.add( stockDaily );
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for ( StockDaily stockDaily : stockDailyList ) {

            // 주식명, 시장코드 가져오려고 조회
            StockResult stockResult = null;
            try {
                stockResult = stockDao.getStock( stockDaily.getStockCode() );
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            if ( stockResult != null ) {
                stockDaily.setStockName( stockResult.getStockName() );
                stockDaily.setMarketCode( stockResult.getMarketCode() );
                if ( stockDaily.getVolumeTotal() == null ) stockDaily.setVolumeTotal( stockResult.getStockCount() );
                if ( stockDaily.getVolumeTotal() != null ) stockDaily.setPriceTotal( stockResult.getStockCount() * stockDaily.getPriceFinal() );
            }

            try {
                stockDailyDao.updateStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                log.debug( dke.getMessage() );
                break;
            }
        }

    }




    /**
     * 다음 주식 페이지, 일별 데이터 첫 페이지 10개 데이터를 stockDaily 테이블에 upsert
     * @param stockCode
     */
    public void upsertStockDailyFromDaumDaily(String stockCode ) throws InterruptedException {

        int perPage = 10;

        // 다음
        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        ArrayList<DaumDailyStock> daumDailyStockList = null;

        int readCount = 0;
        boolean isRead = false;
        while ( !isRead ) {

            try {
                daumDailyStockList = daumDailyStockScrapper.getDailyStockList(stockCode, perPage, 1);
                isRead = true;
            } catch (Exception e) {
                e.printStackTrace();
                readCount++;
                if ( readCount > 5 ) throw new NetworkCommunicationFailureException();
                Thread.sleep( 3000 );
            }
        }

        // 우리 DB에 맞게 컨버트
        for ( DaumDailyStock daumDailyStock : daumDailyStockList ) {
            StockDaily stockDaily = new StockDaily( daumDailyStock );
            stockDailyList.add( stockDaily );
        }

        for ( int i = 0; i < stockDailyList.size(); i++ ) {
            StockDaily stockDaily = stockDailyList.get(i);

            // 현재 Stock 테이블 정보 읽어옴
            StockResult stockResult = null;
            try {
                stockResult = stockDao.getStock( stockDaily.getStockCode() );
            } catch ( Exception e ) {

            }

            if ( stockResult != null ) {
                stockDaily.setStockName(stockResult.getStockName());
                stockDaily.setMarketCode(stockResult.getMarketCode());
                if ( stockDaily.getVolumeTotal() == null && stockResult.getStockCount() != null ) {
                    stockDaily.setVolumeTotal( stockResult.getStockCount() );
                    if ( stockDaily.getVolumeTotal() != null )  stockDaily.setPriceTotal( stockResult.getStockCount() * stockDaily.getPriceFinal() );
                }
            }

            try {
                // 리스트의 첫번째는 가장 최근날짜의 가격, stock 테이블의 현재가격 업데이트
                if ( i == 0 ) {
                    Stock stock = new Stock();
                    stock.setStockCode( stockCode );
                    stock.setLastTradeDate( stockDaily.getTradeDate() );        // 마지막 거래일
                    stock.setCurrentPrice( stockDaily.getPriceFinal() );        // 마지막 가격
                    stockDao.updateStock( stock );
                }
                stockDailyDao.updateInsertStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                dke.printStackTrace();
                throw new ApplicationRuntimeException("왜 키 중복 에러지?");
            }
        }
    }


    /**
     * 다음 주식 페이지, 일별/투자자별 데이터 첫 페이지 10개 데이터를 stockDaily 테이블에 upsert
     * @param stockCode
     */
    public void upsertStockDailyFromDaumInvestor(String stockCode  ) {

        int perPage = 10;

        ArrayList<DaumInvestorStock> daumInvestorStockList = daumDailyInvestorScrapper.getDailyInvestorList( stockCode, perPage, 1 );

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        for ( DaumInvestorStock daumInvestorStock : daumInvestorStockList ) {
            StockDaily stockDaily = new StockDaily( daumInvestorStock );
            if ( stockDaily.getStockCode() == null ) stockDaily.setStockCode( stockCode );      // 해당 페이지의 데이터에는 stockCode가 없어서 세팅
            stockDailyList.add( stockDaily );
        }

        for ( StockDaily stockDaily : stockDailyList ) {
            log.debug( stockDaily.toString() );

            // 주식명, 시장코드 가져오려고 조회
            StockResult stockResult = null;
            try {
                stockResult = stockDao.getStock( stockDaily.getStockCode() );
            } catch ( Exception e ) {

            }

            if ( stockResult != null ) {
                stockDaily.setStockName(stockResult.getStockName());
                stockDaily.setMarketCode(stockResult.getMarketCode());
                /*  다음 inverstor stock 에 priceFinal 이 없음, stockCount 도 없음
                if ( stockDaily.getVolumeTotal() == null && stockResult.getStockCount() != null ) {
                    stockDaily.setVolumeTotal( stockResult.getStockCount() );
                    if ( stockDaily.getVolumeTotal() != null )  stockDaily.setPriceTotal( stockResult.getStockCount() * stockDaily.getPriceFinal() );
                }
                */
            }

            stockDailyDao.updateInsertStockDaily( stockDaily );

        }

        // 일간 데이터가 있을 경우, 그 첫 번째 데이터는 가장 최근 데이터임
        // 그 최근 데이터를 stock 에 업데이트
        if ( stockDailyList.size() > 0 ) {
            StockDaily stockDaily = stockDailyList.get(0);
            Stock stock = new Stock();
            stock.setStockCode( stockCode );
            stock.setCurrentPrice( stockDaily.getPriceFinal() );
            stock.setForeignersHoldRate( stockDaily.getForeignerHoldRate() );
            stock.setForeignersStockCount( stockDaily.getForeignerStockCount() );
            stockDao.updateStock( stock );
        }

    }


    /**
     * 다음 주식 페이지, 주식 상세 페이지 데이터를 stockDaily 테이블에 upsert
     * @param stockCode
     */
    public void upsertStockDailyFromDaumQuotes( String stockCode ) {

        DaumQuotes daumQuotes = daumQuotesScrapper.get( stockCode );
        if ( daumQuotes != null ) {
            StockDaily stockDaily = new StockDaily(daumQuotes);
            stockDailyDao.updateInsertStockDaily(stockDaily);

            Stock stock = new Stock(stockDaily);
            stock.setIssueDate(DateUtil.getYyyymmdd(daumQuotes.getListingDate()));
            stockDao.updateInsert(stock);
        } else {
            Stock stock = new Stock();
            stock.setStockCode( stockCode );
            stock.setDelistingYn( "Y" );
            stockDao.updateStock( stock );

        }
    }

}

