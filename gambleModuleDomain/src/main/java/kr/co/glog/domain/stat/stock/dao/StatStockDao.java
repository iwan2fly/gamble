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
    public StatStockResult getStatStock( String stockCode, String periodCode, String yearWeek ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( yearWeek == null ) throw new ParameterMissingException( "yearWeek" );

        StatStockResult statStockResult = null;
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYearWeek( yearWeek );
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
        if ( statStock.getStockCode() == null || statStock.getPeriodCode() == null || statStock.getYearWeek() == null) throw new ParameterMissingException( "주식코드, 주기, 회차는 필수값입니다.");

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
        if ( statStock.getStockCode() == null || statStock.getPeriodCode() == null || statStock.getYearWeek() == null) throw new ParameterMissingException( "주식코드, 주기, 회차는 필수값입니다.");
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
