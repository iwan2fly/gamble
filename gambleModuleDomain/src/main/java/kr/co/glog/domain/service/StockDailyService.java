package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
import kr.co.glog.external.daumFinance.model.IncludedStock;
import kr.co.glog.external.daumFinance.model.RankingStock;
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

    private final StockDao stockDao;

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
        stockDaily.setVolume( daumDailyStock.getAccTradeVolume() );
        stockDaily.setRateChange( Float.parseFloat( daumDailyStock.getChangeRate() ) );

        /*
        stockDaily.setVolumeOrg(0);
        stockDaily.setVolumeForeigner(0);
        stockDaily.setForeignerStockCount(0L);
        stockDaily.setForeignerHoldRate(0.0F);
        */

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
        stockDaily.setForeignerHoldRate( daumInvestorStock.getForeignOwnSharesRate() );

        return stockDaily;
    }
}
