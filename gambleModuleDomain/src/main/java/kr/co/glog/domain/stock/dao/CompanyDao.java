package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.Company;
import kr.co.glog.domain.stock.mapper.CompanyMapper;
import kr.co.glog.domain.stock.model.CompanyParam;
import kr.co.glog.domain.stock.model.CompanyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class CompanyDao {

    private final CompanyMapper companyMapper;

    public CompanyResult getCompany(String companyCode ) {
        if ( companyCode == null ) throw new ParameterMissingException( "companyCode" );

        CompanyResult CompanyResult = null;
        CompanyParam CompanyParam = new CompanyParam();
        CompanyParam.setCompanyCode( companyCode );
        ArrayList<CompanyResult> CompanyList = companyMapper.selectCompanyList( CompanyParam );
        if ( CompanyList != null && CompanyList.size() > 0 ) CompanyResult = CompanyList.get(0);
        return CompanyResult;
    }

    public ArrayList<CompanyResult> getCompanyList(CompanyParam CompanyParam ) {
        if ( CompanyParam == null ) throw new ParameterMissingException( "CompanyParam" );

        return companyMapper.selectCompanyList( CompanyParam );
    }

    public int insertCompany ( Company company) {
        return companyMapper.insertCompany(company);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param companyList
     * @return
     */
    public int insertsCompany( ArrayList<Company> companyList) {
        return insertsCompany(companyList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param companyList
     * @param insertSize
     * @return
     */
    public int insertsCompany(ArrayList<Company> companyList, int insertSize ) {
        if ( companyList == null ) throw new ParameterMissingException( "CompanyList" );

        int insertCount = 0;
        int remainSize = companyList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<Company> subList = (ArrayList) companyList.subList( startIndex, endIndex );
            insertCount += companyMapper.insertsCompany( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > companyList.size() ) endIndex = companyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < companyList.size() ) {
            ArrayList<Company> subList = (ArrayList) companyList.subList(startIndex, endIndex);
            insertCount += companyMapper.insertsCompany(subList);
        }

        return insertCount;
    }

    public int updateCompany( Company company) {
        if ( company == null ) throw new ParameterMissingException( "Company" );
        if ( company.getCompanyCode() == null ) throw new ParameterMissingException( "companyCode" );
        return companyMapper.updateCompany(company);
    }

    public int updateInsetCompany( Company company ) {
        int result = updateCompany(company);
        if ( result == 0 ) {
            result = insertCompany(company);
        }
        return result;
    }

    public int deleteCompany( String companyCode ) {
        if ( companyCode == null ) throw new ParameterMissingException( "companyCode" );

        CompanyParam CompanyParam = new CompanyParam();
        CompanyParam.setCompanyCode( companyCode );
        return companyMapper.deleteCompany( CompanyParam );
    }




}
