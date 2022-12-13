package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.datagokr.fsc.model.GetPriceStockInfoResult;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockDailyService {

    /**
     * DaumDailyStock -> StockDaily
     * @param daumDailyStock
     */
    public StockDaily getStockDailyFromDaumDailyStock( DaumDailyStock daumDailyStock ) {

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
    public StockDaily getStockDailyFromDaumInvestorStock( DaumInvestorStock daumInvestorStock ) {

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
     * @param getPriceStockInfoResult
     */
    public StockDaily getStockDailyFromFscStockInfo( GetPriceStockInfoResult getPriceStockInfoResult ) {

        if ( getPriceStockInfoResult == null ) throw new ParameterMissingException( "getPriceStockInfoResult" );
        log.debug( getPriceStockInfoResult.toString() );

        StockDaily stockDaily = new StockDaily();
        stockDaily.setIsin( getPriceStockInfoResult.getIsinCd() );                  // isin code
        stockDaily.setTradeDate( getPriceStockInfoResult.getBasDt() ) ;             // 날짜
        stockDaily.setStockCode( getPriceStockInfoResult.getSrtnCd() );             // 종목코드(6자리)
        stockDaily.setStockName( getPriceStockInfoResult.getItmsNm() );             // 종목명
        stockDaily.setPriceStart( getPriceStockInfoResult.getMkp() );               // 시가
        stockDaily.setPriceHigh( getPriceStockInfoResult.getHipr() );               // 고가
        stockDaily.setPriceLow( getPriceStockInfoResult.getLopr() );                // 저가
        stockDaily.setPriceFinal( getPriceStockInfoResult.getClpr() );              // 종가
        stockDaily.setPriceChange( getPriceStockInfoResult.getVs() );               // 대비
        stockDaily.setRateChange( getPriceStockInfoResult.getFltRt() );             // 등락율
        stockDaily.setVolumeTrade( getPriceStockInfoResult.getTrqu() );             // 거래량
        stockDaily.setPriceTrade( getPriceStockInfoResult.getTrPrc() );             // 거래대금
        stockDaily.setVolumeTotal( getPriceStockInfoResult.getLstgStCnt() );        // 주식수
        stockDaily.setPriceTotal( getPriceStockInfoResult.getMrktTotAmt() );        // 시총

        return stockDaily;
    }
}

