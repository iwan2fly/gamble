package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.DartCompanyFinancialInfo;
import kr.co.glog.domain.stock.mapper.DartCompanyFinancialInfoMapper;
import kr.co.glog.domain.stock.model.DartCompanyFinancialInfoParam;
import kr.co.glog.domain.stock.model.DartCompanyFinancialInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DartCompanyFinancialInfoDao {

    private final DartCompanyFinancialInfoMapper dartCompanyFinancialInfoMapper;

    public DartCompanyFinancialInfoResult getCompanyFinancialInfo(String companyCode, String reportCode, String year, String subjectDiv, String accountName ) {
        if ( companyCode == null ) throw new ParameterMissingException( "companyCode" );
        if ( reportCode == null ) throw new ParameterMissingException( "reportCode" );
        if ( year == null ) throw new ParameterMissingException( "year" );
        if ( subjectDiv == null ) throw new ParameterMissingException( "subjectDiv" );
        if ( accountName == null ) throw new ParameterMissingException( "accountName" );

        DartCompanyFinancialInfoResult CompanyFinancialInfoResult = null;
        DartCompanyFinancialInfoParam CompanyFinancialInfoParam = new DartCompanyFinancialInfoParam();
        CompanyFinancialInfoParam.setCompanyCode( companyCode );
        CompanyFinancialInfoParam.setReportCode( reportCode );
        CompanyFinancialInfoParam.setYear( year );
        CompanyFinancialInfoParam.setSubjectDiv( subjectDiv );
        CompanyFinancialInfoParam.setAccountName( accountName );
        ArrayList<DartCompanyFinancialInfoResult> CompanyFinancialInfoList = dartCompanyFinancialInfoMapper.selectDartCompanyFinancialInfoList( CompanyFinancialInfoParam );
        if ( CompanyFinancialInfoList != null && CompanyFinancialInfoList.size() > 0 ) CompanyFinancialInfoResult = CompanyFinancialInfoList.get(0);
        return CompanyFinancialInfoResult;
    }

    public ArrayList<DartCompanyFinancialInfoResult> getCompanyFinancialInfoList(DartCompanyFinancialInfoParam CompanyFinancialInfoParam ) {
        if ( CompanyFinancialInfoParam == null ) throw new ParameterMissingException( "CompanyFinancialInfoParam" );

        return dartCompanyFinancialInfoMapper.selectDartCompanyFinancialInfoList( CompanyFinancialInfoParam );
    }

    public int insertCompanyFinancialInfo ( DartCompanyFinancialInfo dartCompanyFinancialInfo) {
        return dartCompanyFinancialInfoMapper.insertDartCompanyFinancialInfo(dartCompanyFinancialInfo);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param dartCompanyFinancialInfoList
     * @return
     */
    public int insertsCompanyFinancialInfo( ArrayList<DartCompanyFinancialInfo> dartCompanyFinancialInfoList) {
        return insertsCompanyFinancialInfo(dartCompanyFinancialInfoList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param dartCompanyFinancialInfoList
     * @param insertSize
     * @return
     */
    public int insertsCompanyFinancialInfo(ArrayList<DartCompanyFinancialInfo> dartCompanyFinancialInfoList, int insertSize ) {
        if ( dartCompanyFinancialInfoList == null ) throw new ParameterMissingException( "CompanyFinancialInfoList" );

        int insertCount = 0;
        int remainSize = dartCompanyFinancialInfoList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<DartCompanyFinancialInfo> subList = (ArrayList) dartCompanyFinancialInfoList.subList( startIndex, endIndex );
            insertCount += dartCompanyFinancialInfoMapper.insertsDartCompanyFinancialInfo( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > dartCompanyFinancialInfoList.size() ) endIndex = dartCompanyFinancialInfoList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < dartCompanyFinancialInfoList.size() ) {
            ArrayList<DartCompanyFinancialInfo> subList = (ArrayList) dartCompanyFinancialInfoList.subList(startIndex, endIndex);
            insertCount += dartCompanyFinancialInfoMapper.insertsDartCompanyFinancialInfo(subList);
        }

        return insertCount;
    }

    public int updateCompanyFinancialInfo( DartCompanyFinancialInfo dartCompanyFinancialInfo) {
        if ( dartCompanyFinancialInfo == null ) throw new ParameterMissingException( "CompanyFinancialInfo" );
        if ( dartCompanyFinancialInfo.getCompanyCode() == null ) throw new ParameterMissingException( "companyCode" );
        if ( dartCompanyFinancialInfo.getReportCode() == null ) throw new ParameterMissingException( "reportCode" );
        if ( dartCompanyFinancialInfo.getYear() == null ) throw new ParameterMissingException( "year" );
        if ( dartCompanyFinancialInfo.getSubjectDiv() == null ) throw new ParameterMissingException( "subjectDiv" );
        if ( dartCompanyFinancialInfo.getAccountName() == null ) throw new ParameterMissingException( "accountName" );

        return dartCompanyFinancialInfoMapper.updateDartCompanyFinancialInfo(dartCompanyFinancialInfo);
    }
    
    public int updateInsetCompanyFinancialInfo( DartCompanyFinancialInfo dartCompanyFinancialInfo) {
        int result = updateCompanyFinancialInfo(dartCompanyFinancialInfo);
        if ( result == 0 ) {
            result = insertCompanyFinancialInfo(dartCompanyFinancialInfo);
        }
        return result;
    }

    public int deleteCompanyFinancialInfo( DartCompanyFinancialInfo dartCompanyFinancialInfo) {
        if ( dartCompanyFinancialInfo == null ) throw new ParameterMissingException( "CompanyFinancialInfo" );
        if ( dartCompanyFinancialInfo.getCompanyCode() == null ) throw new ParameterMissingException( "companyCode" );
        if ( dartCompanyFinancialInfo.getReportCode() == null ) throw new ParameterMissingException( "reportCode" );
        if ( dartCompanyFinancialInfo.getYear() == null ) throw new ParameterMissingException( "year" );
        if ( dartCompanyFinancialInfo.getSubjectDiv() == null ) throw new ParameterMissingException( "subjectDiv" );
        if ( dartCompanyFinancialInfo.getAccountName() == null ) throw new ParameterMissingException( "accountName" );

        return dartCompanyFinancialInfoMapper.deleteDartCompanyFinancialInfo(dartCompanyFinancialInfo);
    }




}
