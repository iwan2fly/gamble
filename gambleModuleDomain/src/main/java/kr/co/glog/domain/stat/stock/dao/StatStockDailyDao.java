package kr.co.glog.domain.stat.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.entity.StatStockDaily;
import kr.co.glog.domain.stat.stock.mapper.StatStockDailyMapper;
import kr.co.glog.domain.stat.stock.model.StatStockDailyParam;
import kr.co.glog.domain.stat.stock.model.StatStockDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StatStockDailyDao {

    private final StatStockDailyMapper statStockDailyMapper;

    // 키 SELECT
    public StatStockDailyResult getStatStockDaily(Long statStockDailyId ) {
        if ( statStockDailyId == null ) throw new ParameterMissingException( "statStockDailyId" );

        StatStockDailyResult statStockDailyResult = null;
        StatStockDailyParam statStockDailyParam = new StatStockDailyParam();
        statStockDailyParam.setStatStockDailyId( statStockDailyId );
        ArrayList<StatStockDailyResult> statStockDailyList = statStockDailyMapper.selectStatStockDailyList( statStockDailyParam );
        if ( statStockDailyList != null && statStockDailyList.size() > 0 ) statStockDailyResult = statStockDailyList.get(0);
        return statStockDailyResult;
    }

    // 유니크 SELECT
    public StatStockDailyResult getStatStockDaily( String stockCode, String tradeDate ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );

        StatStockDailyResult statStockDailyResult = null;
        StatStockDailyParam statStockDailyParam = new StatStockDailyParam();
        statStockDailyParam.setStockCode( stockCode );
        statStockDailyParam.setTradeDate( tradeDate );
        ArrayList<StatStockDailyResult> statStockDailyList = statStockDailyMapper.selectStatStockDailyList( statStockDailyParam );
        if ( statStockDailyList != null && statStockDailyList.size() > 0 ) statStockDailyResult = statStockDailyList.get(0);
        return statStockDailyResult;
    }


    public ArrayList<StatStockDailyResult> getStatStockDailyList(StatStockDailyParam statStockDailyParam ) {
        if ( statStockDailyParam == null ) throw new ParameterMissingException( "statStockDailyParam" );

        return statStockDailyMapper.selectStatStockDailyList( statStockDailyParam );
    }

    public int insertStatStockDaily ( StatStockDaily statStockDaily ) {

        if ( statStockDaily == null ) throw new ParameterMissingException( "StatStockDaily" );
        if ( statStockDaily.getStockCode() == null || statStockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");

        return statStockDailyMapper.insertStatStockDaily(statStockDaily);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param statStockDailyList
     * @return
     */
    public int insertsStatStockDaily( ArrayList<StatStockDaily> statStockDailyList) {
        return insertsStatStockDaily(statStockDailyList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param statStockDailyList
     * @param insertSize
     * @return
     */
    public int insertsStatStockDaily(ArrayList<StatStockDaily> statStockDailyList, int insertSize ) {
        if ( statStockDailyList == null ) throw new ParameterMissingException( "statStockDailyList" );

        int insertCount = 0;
        int remainSize = statStockDailyList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StatStockDaily> subList = (ArrayList) statStockDailyList.subList( startIndex, endIndex );
            insertCount += statStockDailyMapper.insertsStatStockDaily( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > statStockDailyList.size() ) endIndex = statStockDailyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < statStockDailyList.size() ) {
            ArrayList<StatStockDaily> subList = (ArrayList) statStockDailyList.subList(startIndex, endIndex);
            insertCount += statStockDailyMapper.insertsStatStockDaily(subList);
        }

        return insertCount;
    }

    public StatStockDaily updateStatStockDaily( StatStockDaily statStockDaily ) {
        if ( statStockDaily == null ) throw new ParameterMissingException( "StatStockDaily" );
        if ( statStockDaily.getStockCode() == null || statStockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");
        statStockDailyMapper.updateStatStockDaily(statStockDaily);
        return statStockDaily;
    }

    public StatStockDaily saveStatStockDaily(StatStockDaily statStockDaily) {
        if ( statStockDaily == null ) throw new ParameterMissingException( "StatStockDaily" );
        if ( statStockDaily.getStockCode() == null || statStockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");

        try {
            statStockDailyMapper.insertStatStockDaily(statStockDaily);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            statStockDailyMapper.updateStatStockDaily(statStockDaily);
        }

        return statStockDaily;
    }

    public void deleteStatStockDaily( Long statStockDailyId ) {
        if ( statStockDailyId == null ) throw new ParameterMissingException( "statStockDailyId" );

        StatStockDailyParam statStockDailyParam = new StatStockDailyParam();
        statStockDailyParam.setStatStockDailyId( statStockDailyId );
        statStockDailyMapper.deleteStatStockDaily( statStockDailyParam );
    }




}
