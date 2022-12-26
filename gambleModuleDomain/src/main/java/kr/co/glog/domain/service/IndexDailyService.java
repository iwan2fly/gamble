package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.datagokr.fsc.GetMarketIndexInfo;
import kr.co.glog.external.datagokr.fsc.model.GetMarketIndexInfoResult;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import kr.co.glog.external.daumFinance.model.DaumDailyIndex;
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
public class IndexDailyService {

    private final StockDao stockDao;

    /**
     * DaumDailyIndex -> IndexDaily
     * @param daumDailyIndex
     */
    public IndexDaily getIndexDailyFromDaumDailyIndex( String marketCode, DaumDailyIndex daumDailyIndex ) {

        if ( daumDailyIndex == null ) throw new ParameterMissingException( "daumDailyIndex" );

        log.debug( daumDailyIndex.toString() );


        IndexDaily indexDaily = new IndexDaily();
        indexDaily.setMarketCode( marketCode );
        indexDaily.setTradeDate( daumDailyIndex.getDate().substring(0, 10).replaceAll("-", "") ) ;
        indexDaily.setPriceFinal( daumDailyIndex.getTradePrice() );
        indexDaily.setPriceChange( daumDailyIndex.getChangePrice() );
        indexDaily.setRateChange( daumDailyIndex.getChangePrice() / ( daumDailyIndex.getTradePrice() + daumDailyIndex.getChangePrice() ) * 100 ) ;

        if ( daumDailyIndex.getChange() != null && daumDailyIndex.getChange().equals("FALL") ) indexDaily.setPriceChange( -indexDaily.getPriceChange() );

        return indexDaily;
    }


    public IndexDaily getIndexkDailyFromFscMarketInfo( String marketCode, GetMarketIndexInfoResult getMarketIndexInfoResult ) {

        if ( getMarketIndexInfoResult == null ) throw new ParameterMissingException( "getMarketIndexInfoResult" );
        log.debug( getMarketIndexInfoResult.toString() );

        IndexDaily indexDaily = new IndexDaily();
        indexDaily.setMarketCode( marketCode );                                      // index code : KOSPI, KOSDAQ
        indexDaily.setTradeDate( getMarketIndexInfoResult.getBasDt() ) ;             // 날짜
        indexDaily.setPriceStart( getMarketIndexInfoResult.getMkp() );               // 시가
        indexDaily.setPriceHigh( getMarketIndexInfoResult.getHipr() );               // 고가
        indexDaily.setPriceLow( getMarketIndexInfoResult.getLopr() );                // 저가
        indexDaily.setPriceFinal( getMarketIndexInfoResult.getClpr() );              // 종가
        indexDaily.setPriceChange( getMarketIndexInfoResult.getVs() );               // 대비
        indexDaily.setRateChange( getMarketIndexInfoResult.getFltRt() );             // 등락율
        indexDaily.setVolumeTrade( getMarketIndexInfoResult.getTrqu() );             // 거래량
        indexDaily.setPriceTrade( getMarketIndexInfoResult.getTrPrc() );             // 거래대금


        return indexDaily;
    }

}
