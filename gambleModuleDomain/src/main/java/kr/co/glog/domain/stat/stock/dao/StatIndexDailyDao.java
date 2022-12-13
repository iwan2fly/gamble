package kr.co.glog.domain.stat.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.entity.StatIndexDaily;
import kr.co.glog.domain.stat.stock.mapper.StatIndexDailyMapper;
import kr.co.glog.domain.stat.stock.model.StatIndexDailyParam;
import kr.co.glog.domain.stat.stock.model.StatIndexDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StatIndexDailyDao {

    private final StatIndexDailyMapper statIndexDailyMapper;

    // 키 SELECT
    public StatIndexDailyResult getStatIndexDaily(Long statIndexDailyId ) {
        if ( statIndexDailyId == null ) throw new ParameterMissingException( "statIndexDailyId" );

        StatIndexDailyResult statIndexDailyResult = null;
        StatIndexDailyParam statIndexDailyParam = new StatIndexDailyParam();
        statIndexDailyParam.setStatIndexDailyId( statIndexDailyId );
        ArrayList<StatIndexDailyResult> statIndexDailyList = statIndexDailyMapper.selectStatIndexDailyList( statIndexDailyParam );
        if ( statIndexDailyList != null && statIndexDailyList.size() > 0 ) statIndexDailyResult = statIndexDailyList.get(0);
        return statIndexDailyResult;
    }

    // 유니크 SELECT
    public StatIndexDailyResult getStatIndexDaily( String marketCode, String tradeDate ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );

        StatIndexDailyResult statIndexDailyResult = null;
        StatIndexDailyParam statIndexDailyParam = new StatIndexDailyParam();
        statIndexDailyParam.setMarketCode( marketCode );
        statIndexDailyParam.setTradeDate( tradeDate );
        ArrayList<StatIndexDailyResult> statIndexDailyList = statIndexDailyMapper.selectStatIndexDailyList( statIndexDailyParam );
        if ( statIndexDailyList != null && statIndexDailyList.size() > 0 ) statIndexDailyResult = statIndexDailyList.get(0);
        return statIndexDailyResult;
    }


    public ArrayList<StatIndexDailyResult> getStatIndexDailyList(StatIndexDailyParam statIndexDailyParam ) {
        if ( statIndexDailyParam == null ) throw new ParameterMissingException( "statIndexDailyParam" );

        return statIndexDailyMapper.selectStatIndexDailyList( statIndexDailyParam );
    }

    public int insertStatIndexDaily ( StatIndexDaily statIndexDaily ) {

        if ( statIndexDaily == null ) throw new ParameterMissingException( "StatIndexDaily" );
        if ( statIndexDaily.getMarketCode() == null || statIndexDaily.getTradeDate() == null ) throw new ParameterMissingException( "시장코드와 날짜는 필수값입니다.");

        return statIndexDailyMapper.insertStatIndexDaily(statIndexDaily);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param statIndexDailyList
     * @return
     */
    public int insertsStatIndexDaily( ArrayList<StatIndexDaily> statIndexDailyList) {
        return insertsStatIndexDaily(statIndexDailyList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param statIndexDailyList
     * @param insertSize
     * @return
     */
    public int insertsStatIndexDaily(ArrayList<StatIndexDaily> statIndexDailyList, int insertSize ) {
        if ( statIndexDailyList == null ) throw new ParameterMissingException( "statIndexDailyList" );

        int insertCount = 0;
        int remainSize = statIndexDailyList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StatIndexDaily> subList = (ArrayList) statIndexDailyList.subList( startIndex, endIndex );
            insertCount += statIndexDailyMapper.insertsStatIndexDaily( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > statIndexDailyList.size() ) endIndex = statIndexDailyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < statIndexDailyList.size() ) {
            ArrayList<StatIndexDaily> subList = (ArrayList) statIndexDailyList.subList(startIndex, endIndex);
            insertCount += statIndexDailyMapper.insertsStatIndexDaily(subList);
        }

        return insertCount;
    }

    public StatIndexDaily updateStatIndexDaily( StatIndexDaily statIndexDaily ) {
        if ( statIndexDaily == null ) throw new ParameterMissingException( "StatIndexDaily" );
        if ( statIndexDaily.getMarketCode() == null || statIndexDaily.getTradeDate() == null ) throw new ParameterMissingException( "시장코드와 날짜는 필수값입니다.");
        statIndexDailyMapper.updateStatIndexDaily(statIndexDaily);
        return statIndexDaily;
    }

    public StatIndexDaily saveStatIndexDaily(StatIndexDaily statIndexDaily) {
        if ( statIndexDaily == null ) throw new ParameterMissingException( "StatIndexDaily" );
        if ( statIndexDaily.getMarketCode() == null || statIndexDaily.getTradeDate() == null ) throw new ParameterMissingException( "시장코드와 날짜는 필수값입니다.");

        try {
            statIndexDailyMapper.insertStatIndexDaily(statIndexDaily);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            statIndexDailyMapper.updateStatIndexDaily(statIndexDaily);
        }

        return statIndexDaily;
    }

    public void deleteStatIndexDaily( Long statIndexDailyId ) {
        if ( statIndexDailyId == null ) throw new ParameterMissingException( "statIndexDailyId" );

        StatIndexDailyParam statIndexDailyParam = new StatIndexDailyParam();
        statIndexDailyParam.setStatIndexDailyId( statIndexDailyId );
        statIndexDailyMapper.deleteStatIndexDaily( statIndexDailyParam );
    }




}
