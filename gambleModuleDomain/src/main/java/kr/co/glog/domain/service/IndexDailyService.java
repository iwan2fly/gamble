package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.IndexDaily;
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
        indexDaily.setVolume( daumDailyIndex.getAccTradeVolume() );
        indexDaily.setPriceTotal( daumDailyIndex.getAccTradePrice() );
        indexDaily.setPriceTotalPerson( daumDailyIndex.getIndividualStraightPurchasePrice() );
        indexDaily.setPriceTotalForeigner( daumDailyIndex.getForeignStraightPurchasePrice() );
        indexDaily.setPriceTotalOrg( daumDailyIndex.getInstitutionStraightPurchasePrice() );

        if ( daumDailyIndex.getChange() != null && daumDailyIndex.getChange().equals("FALL") ) indexDaily.setPriceChange( -indexDaily.getPriceChange() );

        return indexDaily;
    }


}
