package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.DartCompany;
import kr.co.glog.domain.stock.mapper.DartCompanyMapper;
import kr.co.glog.domain.stock.model.DartCompanyParam;
import kr.co.glog.domain.stock.model.DartCompanyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DartCompanyDao {

    private final DartCompanyMapper dartCompanyMapper;

    public DartCompanyResult getDartCompany( String corpCode ) {
        if ( corpCode == null ) throw new ParameterMissingException( "corpCode" );

        DartCompanyResult dartCompanyResult = null;
        DartCompanyParam dartCompanyParam = new DartCompanyParam();
        dartCompanyParam.setCorpCode( corpCode );
        ArrayList<DartCompanyResult> dartCompanyList = dartCompanyMapper.selectDartCompanyList( dartCompanyParam );
        if ( dartCompanyList != null && dartCompanyList.size() > 0 ) dartCompanyResult = dartCompanyList.get(0);
        return dartCompanyResult;
    }

    public ArrayList<DartCompanyResult> getDartCompanyList( DartCompanyParam dartCompanyParam ) {
        if ( dartCompanyParam == null ) throw new ParameterMissingException( "dartCompanyParam" );

        return dartCompanyMapper.selectDartCompanyList( dartCompanyParam );
    }

    public int insertDartCompany ( DartCompany dartCompany ) {
        return dartCompanyMapper.insertDartCompany( dartCompany );
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param dartCompanyList
     * @return
     */
    public int insertsDartCompany( ArrayList<DartCompany> dartCompanyList ) {
        return insertsDartCompany( dartCompanyList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param dartCompanyList
     * @param insertSize
     * @return
     */
    public int insertsDartCompany( ArrayList<DartCompany> dartCompanyList, int insertSize ) {
        if ( dartCompanyList == null ) throw new ParameterMissingException( "dartCompanyList" );

        int insertCount = 0;
        int remainSize = dartCompanyList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<DartCompany> subList = (ArrayList)dartCompanyList.subList( startIndex, endIndex );
            insertCount += dartCompanyMapper.insertsDartCompany( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > dartCompanyList.size() ) endIndex = dartCompanyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < dartCompanyList.size() ) {
            ArrayList<DartCompany> subList = (ArrayList) dartCompanyList.subList(startIndex, endIndex);
            insertCount += dartCompanyMapper.insertsDartCompany(subList);
        }

        return insertCount;
    }

    public int updateDartCompany( DartCompany dartCompany ) {
        if ( dartCompany == null ) throw new ParameterMissingException( "DartCompany" );
        if ( dartCompany.getCorpCode() == null ) throw new ParameterMissingException( "corpCode" );
        return dartCompanyMapper.updateDartCompany( dartCompany );
    }

    public int deleteDartCompany( String corpCode ) {
        if ( corpCode == null ) throw new ParameterMissingException( "corpCode" );

        DartCompanyParam dartCompanyParam = new DartCompanyParam();
        dartCompanyParam.setCorpCode( corpCode );
        return dartCompanyMapper.deleteDartCompany( dartCompanyParam );
    }




}
