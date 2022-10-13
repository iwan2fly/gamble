package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.Company;
import kr.co.glog.domain.stock.model.CompanyParam;
import kr.co.glog.domain.stock.model.CompanyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CompanyMapper {
    public int insertCompany( Company company);									    // 등록
    public int insertsCompany( ArrayList<Company> companyList);		                // 대량등록
    public int updateCompany( Company company);									    // 수정
    public int deleteCompany( Company company);									    // 삭제
    public ArrayList<CompanyResult> selectCompanyList(CompanyParam CompanyParam);		// 목록
    public int selectCompanyListCount(CompanyParam CompanyParam);					    // 목록건수
}
