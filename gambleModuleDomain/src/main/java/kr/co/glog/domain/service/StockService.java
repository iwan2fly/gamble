package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
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
public class StockService {

    private final StockDao stockDao;

    /**
     * IncludedStock -> Stock
     * @param includedStock
     */
    public Stock getStockFromIncludedStock( IncludedStock includedStock ) {

        if ( includedStock == null ) throw new ParameterMissingException( "includedStock" );


        Stock stock = new Stock();
        stock.setStockCode( includedStock.getSymbolCode().substring( 1 ) );
        stock.setStockName( includedStock.getName() );
        stock.setCurrentPrice( includedStock.getTradePrice() );

        /*
        stock.setStockTypeCode( includedStock.);
        stock.setMarketTypeCode();

        stock.setFaceValue( includedStock.get);
        stock.setForeignersStockLimit();
        stock.setForeignersStockCount();
        stock.setForeignersHoldRate();
        stock.setListedStockCount();
        stock.setListedDate();

        */

        return stock;
    }

    /**
     * RankingStock -> Stock
     * @param includedStock
     */
    public Stock getStockFromRankingStock( RankingStock rankingStock ) {

        if ( rankingStock == null ) throw new ParameterMissingException( "rankingStock" );


        Stock stock = new Stock();
        stock.setStockCode( rankingStock.getSymbolCode().substring( 1 ) );
        stock.setStockName( rankingStock.getName() );
        stock.setCurrentPrice( rankingStock.getTradePrice() );
        stock.setStockCount( rankingStock.getListedShareCount() );

        /*
        stock.setStockTypeCode( includedStock.);
        stock.setMarketTypeCode();

        stock.setFaceValue( includedStock.get);
        stock.setForeignersStockLimit();
        stock.setForeignersStockCount();
        stock.setForeignersHoldRate();
        stock.setListedStockCount();
        stock.setListedDate();

        */

        return stock;
    }
}
