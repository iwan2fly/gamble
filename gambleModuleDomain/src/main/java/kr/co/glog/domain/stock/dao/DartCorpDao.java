package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.DartCorp;
import kr.co.glog.domain.stock.mapper.DartCorpMapper;
import kr.co.glog.domain.stock.model.DartCorpParam;
import kr.co.glog.domain.stock.model.DartCorpResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DartCorpDao {

    private final DartCorpMapper dartCorpMapper;

    public DartCorpResult getDartCorp( String corpCode ) {
        if ( corpCode == null ) throw new ParameterMissingException( "corpCode" );

        DartCorpResult dartCorpResult = null;
        DartCorpParam dartCorpParam = new DartCorpParam();
        dartCorpParam.setCorpCode( corpCode );
        ArrayList<DartCorpResult> dartCorpList = dartCorpMapper.selectDartCorpList( dartCorpParam );
        if ( dartCorpList != null && dartCorpList.size() > 0 ) dartCorpResult = dartCorpList.get(0);
        return dartCorpResult;
    }

    public ArrayList<DartCorpResult> getDartCorpList( DartCorpParam dartCorpParam ) {
        if ( dartCorpParam == null ) throw new ParameterMissingException( "dartCorpParam" );

        return dartCorpMapper.selectDartCorpList( dartCorpParam );
    }

    public int insertDartCorp ( DartCorp dartCorp ) {
        return dartCorpMapper.insertDartCorp( dartCorp );
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param dartCorpList
     * @return
     */
    public int insertsDartCorp( ArrayList<DartCorp> dartCorpList ) {
        return insertsDartCorp( dartCorpList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param dartCorpList
     * @param insertSize
     * @return
     */
    public int insertsDartCorp( ArrayList<DartCorp> dartCorpList, int insertSize ) {
        if ( dartCorpList == null ) throw new ParameterMissingException( "dartCorpList" );

        int insertCount = 0;
        int remainSize = dartCorpList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<DartCorp> subList = (ArrayList)dartCorpList.subList( startIndex, endIndex );
            insertCount += dartCorpMapper.insertsDartCorp( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > dartCorpList.size() ) endIndex = dartCorpList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < dartCorpList.size() ) {
            ArrayList<DartCorp> subList = (ArrayList) dartCorpList.subList(startIndex, endIndex);
            insertCount += dartCorpMapper.insertsDartCorp(subList);
        }

        return insertCount;
    }

    public int updateDartCorp( DartCorp dartCorp ) {
        if ( dartCorp == null ) throw new ParameterMissingException( "DartCorp" );
        if ( dartCorp.getCorpCode() == null ) throw new ParameterMissingException( "corpCode" );
        return dartCorpMapper.updateDartCorp( dartCorp );
    }

    public int deleteDartCorp( String corpCode ) {
        if ( corpCode == null ) throw new ParameterMissingException( "corpCode" );

        DartCorpParam dartCorpParam = new DartCorpParam();
        dartCorpParam.setCorpCode( corpCode );
        return dartCorpMapper.deleteDartCorp( dartCorpParam );
    }




}
