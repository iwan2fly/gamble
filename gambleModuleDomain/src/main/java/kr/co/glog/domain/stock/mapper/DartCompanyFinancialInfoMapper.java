package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.DartCompanyFinancialInfo;
import kr.co.glog.domain.stock.model.DartCompanyFinancialInfoParam;
import kr.co.glog.domain.stock.model.DartCompanyFinancialInfoResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface DartCompanyFinancialInfoMapper {
    public int insertDartCompanyFinancialInfo(DartCompanyFinancialInfo dartCompanyFinancialInfo);									    // 등록
    public int insertsDartCompanyFinancialInfo(ArrayList<DartCompanyFinancialInfo> dartCompanyFinancialInfoList);		                // 대량등록
    public int updateDartCompanyFinancialInfo(DartCompanyFinancialInfo dartCompanyFinancialInfo);									    // 수정
    public int deleteDartCompanyFinancialInfo(DartCompanyFinancialInfo dartCompanyFinancialInfo);									    // 삭제
    public ArrayList<DartCompanyFinancialInfoResult> selectDartCompanyFinancialInfoList(DartCompanyFinancialInfoParam CompanyFinancialInfoParam);		// 목록
    public int selectDartCompanyFinancialInfoListCount(DartCompanyFinancialInfoParam CompanyFinancialInfoParam);					    // 목록건수
}
