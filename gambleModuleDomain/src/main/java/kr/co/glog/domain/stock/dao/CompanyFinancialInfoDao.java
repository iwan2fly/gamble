package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.CompanyFinancialInfo;
import kr.co.glog.domain.stock.mapper.CompanyFinancialInfoMapper;
import kr.co.glog.domain.stock.model.CompanyFinancialInfoParam;
import kr.co.glog.domain.stock.model.CompanyFinancialInfoResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class CompanyFinancialInfoDao {

    private final CompanyFinancialInfoMapper companyFinancialInfoMapper;

    public CompanyFinancialInfoResult getCompanyFinancialInfo( String companyCode, String reportCode, String year, String subjectDiv, String accountName ) {
        if ( companyCode == null ) throw new ParameterMissingException( "companyCode" );
        if ( reportCode == null ) throw new ParameterMissingException( "reportCode" );
        if ( year == null ) throw new ParameterMissingException( "year" );
        if ( subjectDiv == null ) throw new ParameterMissingException( "subjectDiv" );
        if ( accountName == null ) throw new ParameterMissingException( "accountName" );

        CompanyFinancialInfoResult CompanyFinancialInfoResult = null;
        CompanyFinancialInfoParam CompanyFinancialInfoParam = new CompanyFinancialInfoParam();
        CompanyFinancialInfoParam.setCompanyCode( companyCode );
        CompanyFinancialInfoParam.setReportCode( reportCode );
        CompanyFinancialInfoParam.setYear( year );
        CompanyFinancialInfoParam.setSubjectDiv( subjectDiv );
        CompanyFinancialInfoParam.setAccountName( accountName );
        ArrayList<CompanyFinancialInfoResult> CompanyFinancialInfoList = companyFinancialInfoMapper.selectCompanyFinancialInfoList( CompanyFinancialInfoParam );
        if ( CompanyFinancialInfoList != null && CompanyFinancialInfoList.size() > 0 ) CompanyFinancialInfoResult = CompanyFinancialInfoList.get(0);
        return CompanyFinancialInfoResult;
    }

    public ArrayList<CompanyFinancialInfoResult> getCompanyFinancialInfoList(CompanyFinancialInfoParam CompanyFinancialInfoParam ) {
        if ( CompanyFinancialInfoParam == null ) throw new ParameterMissingException( "CompanyFinancialInfoParam" );

        return companyFinancialInfoMapper.selectCompanyFinancialInfoList( CompanyFinancialInfoParam );
    }

    public int insertCompanyFinancialInfo ( CompanyFinancialInfo companyFinancialInfo) {
        return companyFinancialInfoMapper.insertCompanyFinancialInfo(companyFinancialInfo);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param companyFinancialInfoList
     * @return
     */
    public int insertsCompanyFinancialInfo( ArrayList<CompanyFinancialInfo> companyFinancialInfoList) {
        return insertsCompanyFinancialInfo(companyFinancialInfoList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param companyFinancialInfoList
     * @param insertSize
     * @return
     */
    public int insertsCompanyFinancialInfo(ArrayList<CompanyFinancialInfo> companyFinancialInfoList, int insertSize ) {
        if ( companyFinancialInfoList == null ) throw new ParameterMissingException( "CompanyFinancialInfoList" );

        int insertCount = 0;
        int remainSize = companyFinancialInfoList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<CompanyFinancialInfo> subList = (ArrayList) companyFinancialInfoList.subList( startIndex, endIndex );
            insertCount += companyFinancialInfoMapper.insertsCompanyFinancialInfo( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > companyFinancialInfoList.size() ) endIndex = companyFinancialInfoList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < companyFinancialInfoList.size() ) {
            ArrayList<CompanyFinancialInfo> subList = (ArrayList) companyFinancialInfoList.subList(startIndex, endIndex);
            insertCount += companyFinancialInfoMapper.insertsCompanyFinancialInfo(subList);
        }

        return insertCount;
    }

    public int updateCompanyFinancialInfo( CompanyFinancialInfo companyFinancialInfo) {
        if ( companyFinancialInfo == null ) throw new ParameterMissingException( "CompanyFinancialInfo" );
        if ( companyFinancialInfo.getCompanyCode() == null ) throw new ParameterMissingException( "companyCode" );
        if ( companyFinancialInfo.getReportCode() == null ) throw new ParameterMissingException( "reportCode" );
        if ( companyFinancialInfo.getYear() == null ) throw new ParameterMissingException( "year" );
        if ( companyFinancialInfo.getSubjectDiv() == null ) throw new ParameterMissingException( "subjectDiv" );
        if ( companyFinancialInfo.getAccountName() == null ) throw new ParameterMissingException( "accountName" );

        return companyFinancialInfoMapper.updateCompanyFinancialInfo(companyFinancialInfo);
    }
    
    public int updateInsetCompanyFinancialInfo( CompanyFinancialInfo companyFinancialInfo ) {
        int result = updateCompanyFinancialInfo(companyFinancialInfo);
        if ( result == 0 ) {
            result = insertCompanyFinancialInfo(companyFinancialInfo);
        }
        return result;
    }

    public int deleteCompanyFinancialInfo( CompanyFinancialInfo companyFinancialInfo ) {
        if ( companyFinancialInfo == null ) throw new ParameterMissingException( "CompanyFinancialInfo" );
        if ( companyFinancialInfo.getCompanyCode() == null ) throw new ParameterMissingException( "companyCode" );
        if ( companyFinancialInfo.getReportCode() == null ) throw new ParameterMissingException( "reportCode" );
        if ( companyFinancialInfo.getYear() == null ) throw new ParameterMissingException( "year" );
        if ( companyFinancialInfo.getSubjectDiv() == null ) throw new ParameterMissingException( "subjectDiv" );
        if ( companyFinancialInfo.getAccountName() == null ) throw new ParameterMissingException( "accountName" );

        return companyFinancialInfoMapper.deleteCompanyFinancialInfo( companyFinancialInfo );
    }




}
