package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.dao.DailyRankStockDao;
import kr.co.glog.domain.stat.stock.dao.PeriodRankStockDao;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.PeriodCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PeriodRankStockService {

    private final PeriodRankStockDao periodRankStockDao;
    String rankCodeList [] = { "rateChange", "priceTotal", "priceTrade", "volumeTrade", "riseCount", "evenCount", "fallCount" };

    public void makePeriodRankYear( String year ) {

        if ( year == null ) throw new ParameterMissingException( "year" );

        for ( int i = 0; i < rankCodeList.length; i++ ) {
            makePeriodRank(MarketCode.kospi, PeriodCode.year, rankCodeList[i], year + "00" );
            makePeriodRank(MarketCode.kosdaq, PeriodCode.year, rankCodeList[i], year + "00" );
        }
    }

    public void makePeriodRankMonth( String yearOrder ) {

        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );

        for ( int i = 0; i < rankCodeList.length; i++ ) {
            makePeriodRank(MarketCode.kospi, PeriodCode.month, rankCodeList[i], yearOrder );
            makePeriodRank(MarketCode.kosdaq, PeriodCode.month, rankCodeList[i], yearOrder ) ;
        }
    }

    public void makePeriodRankWeek( String yearOrder ) {

        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );

        for ( int i = 0; i < rankCodeList.length; i++ ) {
            makePeriodRank(MarketCode.kospi, PeriodCode.week, rankCodeList[i], yearOrder );
            makePeriodRank(MarketCode.kosdaq, PeriodCode.week, rankCodeList[i], yearOrder );
        }
    }

    public void makePeriodRank( String marketCode, String periodCode, String rankCode, String yearOrder ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );
        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );

        int deleteCount = periodRankStockDao.deletePeriodRankStock( marketCode, periodCode, rankCode, yearOrder );
        log.debug( "OLD deleted : " + deleteCount );
        int insertCount = periodRankStockDao.insertPeriodRankFromStatStock( marketCode, periodCode, rankCode, yearOrder );
        log.debug( "NEW inserted : " + insertCount );
    }


}


