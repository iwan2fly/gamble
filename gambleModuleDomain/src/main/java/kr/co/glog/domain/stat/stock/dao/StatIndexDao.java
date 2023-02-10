package kr.co.glog.domain.stat.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.stock.entity.StatIndex;
import kr.co.glog.domain.stat.stock.mapper.StatIndexMapper;
import kr.co.glog.domain.stat.stock.model.StatIndexParam;
import kr.co.glog.domain.stat.stock.model.StatIndexResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StatIndexDao {

    private final StatIndexMapper statIndexMapper;

    // 키 SELECT
    public StatIndexResult getStatIndex(Long statIndexId ) {
        if ( statIndexId == null ) throw new ParameterMissingException( "statIndexId" );

        StatIndexResult statIndexResult = null;
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setStatIndexId( statIndexId );
        ArrayList<StatIndexResult> statIndexList = statIndexMapper.selectStatIndexList( statIndexParam );
        if ( statIndexList != null && statIndexList.size() > 0 ) statIndexResult = statIndexList.get(0);
        return statIndexResult;
    }

    // 유니크 SELECT
    public StatIndexResult getStatIndex( String marketCode, String periodCode, String yearWeek ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( periodCode == null ) throw new ParameterMissingException( "periodCode" );
        if ( yearWeek == null ) throw new ParameterMissingException( "yearWeek" );

        StatIndexResult statIndexResult = null;
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setPeriodCode( periodCode );
        statIndexParam.setYearWeek( yearWeek );
        ArrayList<StatIndexResult> statIndexList = statIndexMapper.selectStatIndexList( statIndexParam );
        if ( statIndexList != null && statIndexList.size() > 0 ) statIndexResult = statIndexList.get(0);
        return statIndexResult;
    }


    public ArrayList<StatIndexResult> getStatIndexList(StatIndexParam statIndexParam ) {
        if ( statIndexParam == null ) throw new ParameterMissingException( "statIndexParam" );

        return statIndexMapper.selectStatIndexList( statIndexParam );
    }

    public int insertStatIndex ( StatIndex statIndex ) {

        if ( statIndex == null ) throw new ParameterMissingException( "StatIndex" );
        if ( statIndex.getMarketCode() == null || statIndex.getPeriodCode() == null || statIndex.getYearWeek() == null) throw new ParameterMissingException( "시장코드, 주기, 회차는 필수값입니다.");

        return statIndexMapper.insertStatIndex(statIndex);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param statIndexList
     * @return
     */
    public int insertsStatIndex( ArrayList<StatIndex> statIndexList) {
        return insertsStatIndex(statIndexList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param statIndexList
     * @param insertSize
     * @return
     */
    public int insertsStatIndex(ArrayList<StatIndex> statIndexList, int insertSize ) {
        if ( statIndexList == null ) throw new ParameterMissingException( "statIndexList" );

        int insertCount = 0;
        int remainSize = statIndexList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StatIndex> subList = (ArrayList) statIndexList.subList( startIndex, endIndex );
            insertCount += statIndexMapper.insertsStatIndex( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > statIndexList.size() ) endIndex = statIndexList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < statIndexList.size() ) {
            ArrayList<StatIndex> subList = (ArrayList) statIndexList.subList(startIndex, endIndex);
            insertCount += statIndexMapper.insertsStatIndex(subList);
        }

        return insertCount;
    }

    public StatIndex updateStatIndex( StatIndex statIndex ) {
        if ( statIndex == null ) throw new ParameterMissingException( "StatIndex" );
        if ( statIndex.getMarketCode() == null || statIndex.getPeriodCode() == null || statIndex.getYearWeek() == null) throw new ParameterMissingException( "시장코드, 주기, 회차는 필수값입니다.");
        statIndexMapper.updateStatIndex(statIndex);
        return statIndex;
    }

    public StatIndex insertUpdateStatIndex(StatIndex statIndex) {
        log.debug( statIndex.toString() );
        if ( statIndex == null ) throw new ParameterMissingException( "StatIndex" );
        if ( statIndex.getMarketCode() == null || statIndex.getPeriodCode() == null ) throw new ParameterMissingException( "시장코드, 주기, 회차는 필수값입니다.");

        try {
            statIndexMapper.insertStatIndex(statIndex);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            statIndexMapper.updateStatIndex(statIndex);
        }

        return statIndex;
    }

    public void deleteStatIndex( Long statIndexId ) {
        if ( statIndexId == null ) throw new ParameterMissingException( "statIndexId" );

        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setStatIndexId( statIndexId );
        statIndexMapper.deleteStatIndex( statIndexParam );
    }




}
