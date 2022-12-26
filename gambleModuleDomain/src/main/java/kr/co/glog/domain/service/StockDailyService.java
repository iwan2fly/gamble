package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
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
     * @param getStockPriceInfoResult
     */
    public StockDaily getStockDailyFromFscStockInfo( GetStockPriceInfoResult getStockPriceInfoResult) {

        if ( getStockPriceInfoResult == null ) throw new ParameterMissingException( "getPriceStockInfoResult" );
        log.debug( getStockPriceInfoResult.toString() );

        StockDaily stockDaily = new StockDaily();
        stockDaily.setIsin( getStockPriceInfoResult.getIsinCd() );                  // isin code
        stockDaily.setTradeDate( getStockPriceInfoResult.getBasDt() ) ;             // 날짜
        stockDaily.setStockCode( getStockPriceInfoResult.getSrtnCd() );             // 종목코드(6자리)
        stockDaily.setStockName( getStockPriceInfoResult.getItmsNm() );             // 종목명
        stockDaily.setPriceStart( getStockPriceInfoResult.getMkp() );               // 시가
        stockDaily.setPriceHigh( getStockPriceInfoResult.getHipr() );               // 고가
        stockDaily.setPriceLow( getStockPriceInfoResult.getLopr() );                // 저가
        stockDaily.setPriceFinal( getStockPriceInfoResult.getClpr() );              // 종가
        stockDaily.setPriceChange( getStockPriceInfoResult.getVs() );               // 대비
        stockDaily.setRateChange( getStockPriceInfoResult.getFltRt() );             // 등락율
        stockDaily.setVolumeTrade( getStockPriceInfoResult.getTrqu() );             // 거래량
        stockDaily.setPriceTrade( getStockPriceInfoResult.getTrPrc() );             // 거래대금
        stockDaily.setVolumeTotal( getStockPriceInfoResult.getLstgStCnt() );        // 주식수
        stockDaily.setPriceTotal( getStockPriceInfoResult.getMrktTotAmt() );        // 시총

        return stockDaily;
    }
}

