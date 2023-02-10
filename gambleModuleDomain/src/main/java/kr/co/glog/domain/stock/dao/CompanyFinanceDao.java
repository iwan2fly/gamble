package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.CompanyFinance;
import kr.co.glog.domain.stock.mapper.CompanyFinanceMapper;
import kr.co.glog.domain.stock.model.CompanyFinanceParam;
import kr.co.glog.domain.stock.model.CompanyFinanceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class CompanyFinanceDao {

    private final CompanyFinanceMapper companyFinanceMapper;

    // 최근 분기 데이터 목록
    public ArrayList<CompanyFinanceResult> getRecentQuarterList( String companyCode ) {
        if ( companyCode == null ) return new ArrayList<CompanyFinanceResult>();

        CompanyFinanceParam companyFinanceParam = new CompanyFinanceParam();
        companyFinanceParam.setCompanyCode( companyCode );
        companyFinanceParam.setRecentQuarter("Y");
        return companyFinanceMapper.selectCompanyFinanceList( companyFinanceParam );
    }

    // 최근 년도 데이터 목록
    public ArrayList<CompanyFinanceResult> getRecentYearList( String companyCode ) {
        if ( companyCode == null ) return new ArrayList<CompanyFinanceResult>();

        CompanyFinanceParam companyFinanceParam = new CompanyFinanceParam();
        companyFinanceParam.setCompanyCode( companyCode );
        companyFinanceParam.setRecentYear("Y");
        return companyFinanceMapper.selectCompanyFinanceList( companyFinanceParam );
    }


    public CompanyFinanceResult getCompanyFinance(String companyCode, Integer year, Integer quarter, String subject, String account ) {
        if ( companyCode == null ) throw new ParameterMissingException( "companyCode" );
        if ( year == null ) throw new ParameterMissingException( "year" );
        if ( quarter == null ) throw new ParameterMissingException( "quarter" );
        if ( subject == null ) throw new ParameterMissingException( "subject" );
        if ( account == null ) throw new ParameterMissingException( "account" );

        CompanyFinanceResult CompanyFinanceResult = null;
        CompanyFinanceParam companyFinanceParam = new CompanyFinanceParam();
        companyFinanceParam.setCompanyCode( companyCode );
        companyFinanceParam.setYear( year );
        companyFinanceParam.setQuarter( quarter );
        companyFinanceParam.setSubject( subject );
        companyFinanceParam.setAccount( account );
        ArrayList<CompanyFinanceResult> CompanyFinanceList = companyFinanceMapper.selectCompanyFinanceList( companyFinanceParam );
        if ( CompanyFinanceList != null && CompanyFinanceList.size() > 0 ) CompanyFinanceResult = CompanyFinanceList.get(0);
        return CompanyFinanceResult;
    }

    public ArrayList<CompanyFinanceResult> getCompanyFinanceList(CompanyFinanceParam CompanyFinanceParam ) {
        if ( CompanyFinanceParam == null ) throw new ParameterMissingException( "CompanyFinanceParam" );

        return companyFinanceMapper.selectCompanyFinanceList( CompanyFinanceParam );
    }

    public int insertCompanyFinance ( CompanyFinance companyFinance) {
        return companyFinanceMapper.insertCompanyFinance(companyFinance);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param companyFinanceList
     * @return
     */
    public int insertsCompanyFinance( ArrayList<CompanyFinance> companyFinanceList) {
        return insertsCompanyFinance(companyFinanceList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param companyFinanceList
     * @param insertSize
     * @return
     */
    public int insertsCompanyFinance(ArrayList<CompanyFinance> companyFinanceList, int insertSize ) {
        if ( companyFinanceList == null ) throw new ParameterMissingException( "CompanyFinanceList" );

        int insertCount = 0;
        int remainSize = companyFinanceList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<CompanyFinance> subList = (ArrayList) companyFinanceList.subList( startIndex, endIndex );
            insertCount += companyFinanceMapper.insertsCompanyFinance( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > companyFinanceList.size() ) endIndex = companyFinanceList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < companyFinanceList.size() ) {
            ArrayList<CompanyFinance> subList = (ArrayList) companyFinanceList.subList(startIndex, endIndex);
            insertCount += companyFinanceMapper.insertsCompanyFinance(subList);
        }

        return insertCount;
    }

    // 특정 회사의 특정년도, 특정 분기 데이터를 모두 지움
    public int deleteCompanyFinance( String companyCode, Integer year, Integer quarter ) {
        if ( companyCode == null ) throw new ParameterMissingException( "companyCode" );
        if ( year == null ) throw new ParameterMissingException( "year" );
        if ( quarter == null ) throw new ParameterMissingException( "quarter" );

        CompanyFinanceParam companyFinanceParam = new CompanyFinanceParam();
        companyFinanceParam.setCompanyCode( companyCode );
        companyFinanceParam.setYear( year );
        companyFinanceParam.setQuarter( quarter );
        return companyFinanceMapper.deleteCompanyFinance( companyFinanceParam );
    }




}

