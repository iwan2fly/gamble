package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import kr.co.glog.external.daumFinance.DaumDailyInvestorScrapper;
import kr.co.glog.external.daumFinance.DaumDailyStockScrapper;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
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

    private final DaumDailyStockScrapper daumDailyStockScrapper;
    private final DaumDailyInvestorScrapper daumDailyInvestorScrapper;
    private final StockDailyDao stockDailyDao;
    private final StockDao stockDao;

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
                StockDaily stockDaily = convertToStockDailyFromDaumDailyStock( daumDailyStock );
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
            log.debug( stockDaily.toString() );
            try {
                stockDailyDao.insertStockDaily( stockDaily );
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
                StockDaily stockDaily = convertToStockDailyFromDaumInvestorStock( daumInvestorStock );
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
            log.debug( stockDaily.toString() );
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
                Thread.sleep( 3000 );
            }
        }

        // 우리 DB에 맞게 컨버트
        for ( DaumDailyStock daumDailyStock : daumDailyStockList ) {
            StockDaily stockDaily = convertToStockDailyFromDaumDailyStock( daumDailyStock );
            stockDailyList.add( stockDaily );
        }

        for ( int i = 0; i < stockDailyList.size(); i++ ) {
            StockDaily stockDaily = stockDailyList.get(i);

            log.debug( stockDaily.toString() );
            try {
                // 리스트의 첫번째는 가장 최근날짜의 가격, stock 테이블의 현재가격 업데이트
                if ( i == 0 ) {
                    Stock stock = new Stock();
                    stock.setStockCode( stockCode );
                    stock.setLastTradeDate( stockDaily.getTradeDate() );        // 마지막 거래일
                    stock.setCurrentPrice( stockDaily.getPriceFinal() );        // 마지막 가격
                    stockDao.updateStock( stock );
                }
                stockDailyDao.saveStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {

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
            StockDaily stockDaily = convertToStockDailyFromDaumInvestorStock( daumInvestorStock );
            if ( stockDaily.getStockCode() == null ) stockDaily.setStockCode( stockCode );      // 해당 페이지의 데이터에는 stockCode가 없어서 세팅
            stockDailyList.add( stockDaily );
        }

        for ( StockDaily stockDaily : stockDailyList ) {
            log.debug( stockDaily.toString() );
            try {
                stockDailyDao.updateStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {

            }
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
     * DaumDailyStock -> StockDaily
     * @param daumDailyStock
     */
    public StockDaily convertToStockDailyFromDaumDailyStock(DaumDailyStock daumDailyStock ) {

        if ( daumDailyStock == null ) throw new ParameterMissingException( "daumDailyStock" );

        log.debug( daumDailyStock.toString() );


        StockDaily stockDaily = new StockDaily();
        stockDaily.setStockCode( daumDailyStock.getSymbolCode() != null ? daumDailyStock.getSymbolCode().substring( 1 ) : "" );
        stockDaily.setTradeDate( daumDailyStock.getDate().substring(0, 10).replaceAll("-", "") ) ;
        stockDaily.setPriceFinal( daumDailyStock.getTradePrice() );
        stockDaily.setPriceChange( daumDailyStock.getChangePrice() );
        stockDaily.setPriceStart( daumDailyStock.getOpeningPrice() );
        stockDaily.setPriceHigh( daumDailyStock.getHighPrice() );
        stockDaily.setPriceLow( daumDailyStock.getLowPrice() );
        stockDaily.setVolumeTrade( daumDailyStock.getAccTradeVolume() );
        stockDaily.setPriceTrade( daumDailyStock.getAccTradePrice() );
        stockDaily.setRateChange( Float.parseFloat( daumDailyStock.getChangeRate() ) * 100 );

        if ( daumDailyStock.getChange() != null && daumDailyStock.getChange().equals("FALL") ) stockDaily.setPriceChange( -stockDaily.getPriceChange() );

        return stockDaily;
    }

    /**
     * DaumInvestorStock -> StockDaily
     * @param daumInvestorStock
     */
    public StockDaily convertToStockDailyFromDaumInvestorStock(DaumInvestorStock daumInvestorStock ) {

        if ( daumInvestorStock == null ) throw new ParameterMissingException( "daumInvestorStock" );
        log.debug( daumInvestorStock.toString() );

        StockDaily stockDaily = new StockDaily();
        stockDaily.setTradeDate( daumInvestorStock.getDate().substring(0, 10).replaceAll("-", "") ) ;
        stockDaily.setVolumeOrg( daumInvestorStock.getInstitutionStraightPurchaseVolume() );
        stockDaily.setVolumeForeigner( daumInvestorStock.getForeignStraightPurchaseVolume() );
        stockDaily.setForeignerStockCount( daumInvestorStock.getForeignOwnShares() );
        stockDaily.setForeignerHoldRate( daumInvestorStock.getForeignOwnSharesRate() * 100 );

        return stockDaily;
    }

    /**
     * GetPriceStockInfoResult -> StockDaily
     * @param getStockPriceInfoResult
     */
    public StockDaily convertToStockDailyFromFscStockInfo(GetStockPriceInfoResult getStockPriceInfoResult) {

        if ( getStockPriceInfoResult == null ) throw new ParameterMissingException( "getPriceStockInfoResult" );
        log.debug( getStockPriceInfoResult.toString() );

        StockDaily stockDaily = new StockDaily();
        stockDaily.setIsin( getStockPriceInfoResult.getIsinCd() );                  // isin code
        stockDaily.setTradeDate( getStockPriceInfoResult.getBasDt() ) ;             // 날짜
        stockDaily.setStockCode( getStockPriceInfoResult.getSrtnCd() );             // 종목코드(6자리)
        stockDaily.setMarketCode( getStockPriceInfoResult.getMrktCtg() );          // 시장코드
        stockDaily.setStockName( getStockPriceInfoResult.getItmsNm() );             // 종목명
        stockDaily.setPriceStart( getStockPriceInfoResult.getMkp() );               // 시가
        stockDaily.setPriceHigh( getStockPriceInfoResult.getHipr() );               // 고가
        stockDaily.setPriceLow( getStockPriceInfoResult.getLopr() );                // 저가
        stockDaily.setPriceFinal( getStockPriceInfoResult.getClpr() );              // 종가
        stockDaily.setPriceChange( getStockPriceInfoResult.getVs() );               // 대비
        stockDaily.setRateChange( getStockPriceInfoResult.getFltRt() * 100 / 100f );             // 등락율
        stockDaily.setVolumeTrade( getStockPriceInfoResult.getTrqu() );             // 거래량
        stockDaily.setPriceTrade( getStockPriceInfoResult.getTrPrc() );             // 거래대금
        stockDaily.setVolumeTotal( getStockPriceInfoResult.getLstgStCnt() );        // 주식수
        stockDaily.setPriceTotal( getStockPriceInfoResult.getMrktTotAmt() );        // 시총

        return stockDaily;
    }
}

