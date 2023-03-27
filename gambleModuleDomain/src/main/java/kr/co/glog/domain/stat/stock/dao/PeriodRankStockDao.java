package kr.co.glog.domain.stat.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stat.stock.entity.PeriodRankStock;
import kr.co.glog.domain.stat.stock.mapper.PeriodRankStockMapper;
import kr.co.glog.domain.stat.stock.model.DailyRankStockParam;
import kr.co.glog.domain.stat.stock.model.PeriodRankStockResult;
import kr.co.glog.domain.stat.stock.model.PeriodRankStockParam;
import kr.co.glog.domain.stock.PeriodCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class PeriodRankStockDao {

    private final PeriodRankStockMapper periodRankStockMapper;


    public int insertPeriodRankFromStatStock( String marketCode, String periodCode, String rankCode, String yearOrder ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );
        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );

        PeriodRankStockParam periodRankStockParam = new PeriodRankStockParam();
        periodRankStockParam.setMarketCode( marketCode );
        periodRankStockParam.setPeriodCode( periodCode );
        periodRankStockParam.setRankCode( rankCode );
        periodRankStockParam.setYearOrder( yearOrder );

        return periodRankStockMapper.insertPeriodRankFromStatStock( periodRankStockParam) ;
    }

    // 유니크 SELECT
    public PeriodRankStockResult getPeriodRankStock(String stockCode, String periodCode, String yearOrder, String rankCode ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );

        PeriodRankStockResult periodRankStockResult = null;
        PeriodRankStockParam periodRankStockParam = new PeriodRankStockParam();
        periodRankStockParam.setStockCode( stockCode );
        periodRankStockParam.setPeriodCode( periodCode );
        periodRankStockParam.setYearOrder( yearOrder );
        periodRankStockParam.setRankCode( rankCode );
        ArrayList<PeriodRankStockResult> periodRankStockList = periodRankStockMapper.selectPeriodRankStockList( periodRankStockParam );
        if ( periodRankStockList != null && periodRankStockList.size() > 0 ) periodRankStockResult = periodRankStockList.get(0);
        return periodRankStockResult;
    }


    public ArrayList<PeriodRankStockResult> getPeriodRankStockList(PeriodRankStockParam periodRankStockParam ) {
        if ( periodRankStockParam == null ) throw new ParameterMissingException( "periodRankStockParam" );

        if ( periodRankStockParam.getTradeDate() != null ) {
            String tradeDate = periodRankStockParam.getTradeDate();
            int year = Integer.parseInt( tradeDate.substring(0,4) );

            if ( PeriodCode.year.equals( periodRankStockParam.getPeriodCode() ) ) periodRankStockParam.setYearOrder( year + "00" );
            else if ( PeriodCode.month.equals( periodRankStockParam.getPeriodCode() ) ) periodRankStockParam.setYearOrder( tradeDate.substring(0,6) );
            else periodRankStockParam.setYearOrder( DateUtil.getYearWeek( tradeDate ) );
        }

        return periodRankStockMapper.selectPeriodRankStockList( periodRankStockParam );
    }

    public int insertPeriodRankStock ( PeriodRankStock periodRankStock ) {

        if ( periodRankStock == null ) throw new ParameterMissingException( "PeriodRankStock" );
        if ( periodRankStock.getStockCode() == null || periodRankStock.getPeriodCode() == null || periodRankStock.getYearOrder() == null || periodRankStock.getRankCode() == null ) throw new ParameterMissingException( "주식코드, 주기, 회차, 순위코드는 필수값입니다.");

        return periodRankStockMapper.insertPeriodRankStock(periodRankStock);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param periodRankStockList
     * @return
     */
    public int insertsPeriodRankStock( ArrayList<PeriodRankStock> periodRankStockList) {
        return insertsPeriodRankStock(periodRankStockList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param periodRankStockList
     * @param insertSize
     * @return
     */
    public int insertsPeriodRankStock(ArrayList<PeriodRankStock> periodRankStockList, int insertSize ) {
        if ( periodRankStockList == null ) throw new ParameterMissingException( "periodRankStockList" );

        int insertCount = 0;
        int remainSize = periodRankStockList.size();
        int startStock = 0;
        int endStock = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<PeriodRankStock> subList = (ArrayList) periodRankStockList.subList( startStock, endStock );
            insertCount += periodRankStockMapper.insertsPeriodRankStock( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startStock += insertSize;           // startStock 를 다음으로
            endStock += insertSize;             // endStock를 다음으로
            if ( endStock > periodRankStockList.size() ) endStock = periodRankStockList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startStock < periodRankStockList.size() ) {
            ArrayList<PeriodRankStock> subList = (ArrayList) periodRankStockList.subList(startStock, endStock);
            insertCount += periodRankStockMapper.insertsPeriodRankStock(subList);
        }

        return insertCount;
    }

    public PeriodRankStock updatePeriodRankStock(PeriodRankStock periodRankStock) {
        if ( periodRankStock == null ) throw new ParameterMissingException( "PeriodRankStock" );
        if ( periodRankStock.getStockCode() == null || periodRankStock.getPeriodCode() == null || periodRankStock.getYearOrder() == null || periodRankStock.getRankCode() == null ) throw new ParameterMissingException( "주식코드, 주기, 회차, 순위코드는 필수값입니다.");
        periodRankStockMapper.updatePeriodRankStock(periodRankStock);
        return periodRankStock;
    }

    public PeriodRankStock insertUpdatePeriodRankStock(PeriodRankStock periodRankStock) {
        log.debug( periodRankStock.toString() );
        if ( periodRankStock == null ) throw new ParameterMissingException( "PeriodRankStock" );
        if ( periodRankStock.getStockCode() == null || periodRankStock.getPeriodCode() == null || periodRankStock.getYearOrder() == null || periodRankStock.getRankCode() == null ) throw new ParameterMissingException( "주식코드, 주기, 회차, 순위코드는 필수값입니다.");

        try {
            periodRankStockMapper.insertPeriodRankStock(periodRankStock);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            periodRankStockMapper.updatePeriodRankStock(periodRankStock);
        }

        return periodRankStock;
    }

    public PeriodRankStock updateInsertPeriodRankStock(PeriodRankStock periodRankStock) {
        log.debug( periodRankStock.toString() );
        if ( periodRankStock == null ) throw new ParameterMissingException( "PeriodRankStock" );
        if ( periodRankStock.getStockCode() == null || periodRankStock.getPeriodCode() == null || periodRankStock.getYearOrder() == null || periodRankStock.getRankCode() == null ) throw new ParameterMissingException( "주식코드, 주기, 회차, 순위코드는 필수값입니다.");

        try {
            if ( periodRankStockMapper.updatePeriodRankStock(periodRankStock) == 0 ) {
                periodRankStockMapper.insertPeriodRankStock(periodRankStock);
            }

        } catch ( Exception e ) {
            periodRankStockMapper.insertPeriodRankStock(periodRankStock);
        }

        return periodRankStock;
    }

    public int deletePeriodRankStock( String marketCode, String periodCode, String rankCode, String yearOrder ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );
        if ( rankCode == null ) throw new ParameterMissingException( "rankCode" );

        PeriodRankStockParam periodRankStockParam = new PeriodRankStockParam();
        periodRankStockParam.setMarketCode( marketCode );
        periodRankStockParam.setPeriodCode( periodCode );
        periodRankStockParam.setRankCode( rankCode );
        periodRankStockParam.setYearOrder( yearOrder );

        log.debug( periodRankStockParam.toString() );

        return periodRankStockMapper.deletePeriodRankStock(periodRankStockParam);
    }


}
