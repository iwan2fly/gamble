package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.DartCompany;
import kr.co.glog.domain.stock.model.DartCompanyParam;
import kr.co.glog.domain.stock.model.DartCompanyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface DartCompanyMapper {
    public int insertDartCompany( DartCompany dartCompany );									    // 등록
    public int insertsDartCompany( ArrayList<DartCompany> dartCompanyList );		                // 대량등록
    public int updateDartCompany( DartCompany dartCompany );									    // 수정
    public int deleteDartCompany( DartCompany dartCompany );									    // 삭제
    public ArrayList<DartCompanyResult> selectDartCompanyList(DartCompanyParam dartCompanyParam);		// 목록
    public int selectDartCompanyListCount(DartCompanyParam dartCompanyParam);					    // 목록건수
}
