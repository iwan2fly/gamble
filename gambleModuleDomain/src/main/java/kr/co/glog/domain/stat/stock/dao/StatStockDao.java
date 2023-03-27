package kr.co.glog.domain.stat.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.entity.StatStock;
import kr.co.glog.domain.stat.stock.mapper.StatStockMapper;
import kr.co.glog.domain.stat.stock.model.StatStockParam;
import kr.co.glog.domain.stat.stock.model.StatStockResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StatStockDao {

    private final StatStockMapper statStockMapper;

    // 변동 구간별 주식 수
    public ArrayList<StatStockResult> getRateChangeSpreadList( String marketCode, String periodCode, String yearOrder, Integer rangeSize ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );
        if ( rangeSize == null ) throw new ParameterMissingException( "rangeSize" );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYearOrder( yearOrder );
        statStockParam.setRangeSize( rangeSize );
        return statStockMapper.selectRateChangeSpreadList( statStockParam );
    }

    public ArrayList<StatStockResult> getRiseFallStockCountList( String marketCode, String periodCode, String startYearOrder, String endYearOrder ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( startYearOrder == null ) throw new ParameterMissingException( "startYearOrder" );
        if ( endYearOrder == null ) throw new ParameterMissingException( "endYearOrder" );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setStartYearOrder( startYearOrder );
        statStockParam.setEndYearOrder( endYearOrder );
        return statStockMapper.selectRiseFallStockCountList( statStockParam );
    }


    // 키 SELECT
    public StatStockResult getStatStock(Long statStockId ) {
        if ( statStockId == null ) throw new ParameterMissingException( "statStockId" );

        StatStockResult statStockResult = null;
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStatStockId( statStockId );
        ArrayList<StatStockResult> statStockList = statStockMapper.selectStatStockList( statStockParam );
        if ( statStockList != null && statStockList.size() > 0 ) statStockResult = statStockList.get(0);
        return statStockResult;
    }

    // 유니크 SELECT
    public StatStockResult getStatStock( String stockCode, String periodCode, String yearOrder ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( yearOrder == null ) throw new ParameterMissingException( "yearOrder" );

        StatStockResult statStockResult = null;
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYearOrder( yearOrder );
        ArrayList<StatStockResult> statStockList = statStockMapper.selectStatStockList( statStockParam );
        if ( statStockList != null && statStockList.size() > 0 ) statStockResult = statStockList.get(0);
        return statStockResult;
    }


    public ArrayList<StatStockResult> getStatStockList(StatStockParam statStockParam ) {
        if ( statStockParam == null ) throw new ParameterMissingException( "statStockParam" );

        return statStockMapper.selectStatStockList( statStockParam );
    }

    public int insertStatStock ( StatStock statStock ) {

        if ( statStock == null ) throw new ParameterMissingException( "StatStock" );
        if ( statStock.getStockCode() == null || statStock.getPeriodCode() == null || statStock.getYearOrder() == null) throw new ParameterMissingException( "주식코드, 주기, 회차는 필수값입니다.");

        return statStockMapper.insertStatStock(statStock);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param statStockList
     * @return
     */
    public int insertsStatStock( ArrayList<StatStock> statStockList) {
        return insertsStatStock(statStockList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param statStockList
     * @param insertSize
     * @return
     */
    public int insertsStatStock(ArrayList<StatStock> statStockList, int insertSize ) {
        if ( statStockList == null ) throw new ParameterMissingException( "statStockList" );

        int insertCount = 0;
        int remainSize = statStockList.size();
        int startStock = 0;
        int endStock = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StatStock> subList = (ArrayList) statStockList.subList( startStock, endStock );
            insertCount += statStockMapper.insertsStatStock( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startStock += insertSize;           // startStock 를 다음으로
            endStock += insertSize;             // endStock를 다음으로
            if ( endStock > statStockList.size() ) endStock = statStockList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startStock < statStockList.size() ) {
            ArrayList<StatStock> subList = (ArrayList) statStockList.subList(startStock, endStock);
            insertCount += statStockMapper.insertsStatStock(subList);
        }

        return insertCount;
    }

    public StatStock updateStatStock( StatStock statStock ) {
        if ( statStock == null ) throw new ParameterMissingException( "StatStock" );
        if ( statStock.getStockCode() == null || statStock.getPeriodCode() == null || statStock.getYearOrder() == null) throw new ParameterMissingException( "주식코드, 주기, 회차는 필수값입니다.");
        statStockMapper.updateStatStock(statStock);
        return statStock;
    }

    public StatStock insertUpdateStatStock(StatStock statStock) {
        log.debug( statStock.toString() );
        if ( statStock == null ) throw new ParameterMissingException( "StatStock" );
        if ( statStock.getStockCode() == null || statStock.getPeriodCode() == null ) throw new ParameterMissingException( "주식코드, 주기, 회차는 필수값입니다.");

        try {
            statStockMapper.insertStatStock(statStock);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            statStockMapper.updateStatStock(statStock);
        }

        return statStock;
    }

    public StatStock updateInsertStatStock(StatStock statStock) {
        log.debug( statStock.toString() );
        if ( statStock == null ) throw new ParameterMissingException( "StatStock" );
        if ( statStock.getStockCode() == null || statStock.getPeriodCode() == null ) throw new ParameterMissingException( "주식코드, 주기, 회차는 필수값입니다.");

        try {
            if ( statStockMapper.updateStatStock(statStock) == 0 ) {
                statStockMapper.insertStatStock(statStock);
            }

        } catch ( Exception e ) {
            statStockMapper.insertStatStock(statStock);
        }

        return statStock;
    }

    public void deleteStatStock( Long statStockId ) {
        if ( statStockId == null ) throw new ParameterMissingException( "statStockId" );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStatStockId( statStockId );
        statStockMapper.deleteStatStock( statStockParam );
    }




}
