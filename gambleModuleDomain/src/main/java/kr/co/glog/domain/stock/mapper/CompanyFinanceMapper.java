package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.CompanyFinance;
import kr.co.glog.domain.stock.model.CompanyFinanceParam;
import kr.co.glog.domain.stock.model.CompanyFinanceResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface CompanyFinanceMapper {
    public int insertCompanyFinance( CompanyFinance companyFinance);									            // 등록
    public int insertsCompanyFinance( ArrayList<CompanyFinance> companyFinanceList);		                        // 대량등록
    public int deleteCompanyFinance( CompanyFinance companyFinance);									            // 삭제
    public ArrayList<CompanyFinanceResult> selectCompanyFinanceList(CompanyFinanceParam CompanyFinanceParam);		// 목록
    public int selectCompanyFinanceListCount(CompanyFinanceParam CompanyFinanceParam);					            // 목록건수
}
