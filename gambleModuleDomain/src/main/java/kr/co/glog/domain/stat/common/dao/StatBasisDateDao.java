package kr.co.glog.domain.stat.common.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stat.common.entity.StatBasisDate;
import kr.co.glog.domain.stat.common.mapper.StatBasisDateMapper;
import kr.co.glog.domain.stat.common.model.StatBasisDateParam;
import kr.co.glog.domain.stat.common.model.StatBasisDateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StatBasisDateDao {

    private final StatBasisDateMapper statBasisDateMapper;

    // 키 SELECT
    public StatBasisDateResult getStatBasisDate(String statDate ) {
        if ( statDate == null ) throw new ParameterMissingException( "statDate" );

        StatBasisDateResult statBasisDateResult = null;
        StatBasisDateParam statBasisDateParam = new StatBasisDateParam();
        statBasisDateParam.setStatDate( statDate );
        ArrayList<StatBasisDateResult> statBasisDateList = statBasisDateMapper.selectStatBasisDateList( statBasisDateParam );
        if ( statBasisDateList != null && statBasisDateList.size() > 0 ) statBasisDateResult = statBasisDateList.get(0);
        return statBasisDateResult;
    }

    public ArrayList<StatBasisDateResult> getStatBasisDateList(StatBasisDateParam statBasisDateParam ) {
        if ( statBasisDateParam == null ) throw new ParameterMissingException( "statBasisDateParam" );

        return statBasisDateMapper.selectStatBasisDateList( statBasisDateParam );
    }

    public int insertStatBasisDate ( StatBasisDate statBasisDate ) {

        if ( statBasisDate == null ) throw new ParameterMissingException( "StatBasisDate" );

        return statBasisDateMapper.insertStatBasisDate(statBasisDate);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param statBasisDateList
     * @return
     */
    public int insertsStatBasisDate( ArrayList<StatBasisDate> statBasisDateList) {
        return insertsStatBasisDate(statBasisDateList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param statBasisDateList
     * @param insertSize
     * @return
     */
    public int insertsStatBasisDate(ArrayList<StatBasisDate> statBasisDateList, int insertSize ) {
        if ( statBasisDateList == null ) throw new ParameterMissingException( "statBasisDateList" );

        int insertCount = 0;
        int remainSize = statBasisDateList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StatBasisDate> subList = (ArrayList) statBasisDateList.subList( startIndex, endIndex );
            insertCount += statBasisDateMapper.insertsStatBasisDate( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > statBasisDateList.size() ) endIndex = statBasisDateList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < statBasisDateList.size() ) {
            ArrayList<StatBasisDate> subList = (ArrayList) statBasisDateList.subList(startIndex, endIndex);
            insertCount += statBasisDateMapper.insertsStatBasisDate(subList);
        }

        return insertCount;
    }

    public StatBasisDate updateStatBasisDate( StatBasisDate statBasisDate ) {
        if ( statBasisDate == null ) throw new ParameterMissingException( "StatBasisDate" );
        statBasisDateMapper.updateStatBasisDate(statBasisDate);
        return statBasisDate;
    }

    public StatBasisDate saveStatBasisDate(StatBasisDate statBasisDate) {
        if ( statBasisDate == null ) throw new ParameterMissingException( "StatBasisDate" );

        try {
            statBasisDateMapper.insertStatBasisDate(statBasisDate);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            statBasisDateMapper.updateStatBasisDate(statBasisDate);
        }

        return statBasisDate;
    }

    public void deleteStatBasisDate( String statDate ) {
        if ( statDate == null ) throw new ParameterMissingException( "statDate" );

        StatBasisDateParam statBasisDateParam = new StatBasisDateParam();
        statBasisDateParam.setStatDate( statDate );
        statBasisDateMapper.deleteStatBasisDate( statBasisDateParam );
    }




}
