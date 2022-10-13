package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.CompanyFinancialInfo;
import kr.co.glog.domain.stock.model.CompanyFinancialInfoParam;
import kr.co.glog.domain.stock.model.CompanyFinancialInfoResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CompanyFinancialInfoMapper {
    public int insertCompanyFinancialInfo( CompanyFinancialInfo companyFinancialInfo);									    // 등록
    public int insertsCompanyFinancialInfo( ArrayList<CompanyFinancialInfo> companyFinancialInfoList);		                // 대량등록
    public int updateCompanyFinancialInfo( CompanyFinancialInfo companyFinancialInfo);									    // 수정
    public int deleteCompanyFinancialInfo( CompanyFinancialInfo companyFinancialInfo);									    // 삭제
    public ArrayList<CompanyFinancialInfoResult> selectCompanyFinancialInfoList(CompanyFinancialInfoParam CompanyFinancialInfoParam);		// 목록
    public int selectCompanyFinancialInfoListCount(CompanyFinancialInfoParam CompanyFinancialInfoParam);					    // 목록건수
}
