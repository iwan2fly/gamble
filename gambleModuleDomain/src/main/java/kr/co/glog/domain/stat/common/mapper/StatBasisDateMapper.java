package kr.co.glog.domain.stat.common.mapper;

import kr.co.glog.domain.stat.common.entity.StatBasisDate;
import kr.co.glog.domain.stat.common.model.StatBasisDateParam;
import kr.co.glog.domain.stat.common.model.StatBasisDateResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StatBasisDateMapper {
    public int insertStatBasisDate( StatBasisDate statBasisDate );									    // 등록
    public int insertsStatBasisDate( ArrayList<StatBasisDate> statBasisDateList );		                // 대량등록
    public int updateStatBasisDate( StatBasisDate statBasisDate );									    // 수정
    public int deleteStatBasisDate( StatBasisDate statBasisDate );									    // 삭제
    public ArrayList<StatBasisDateResult> selectStatBasisDateList(StatBasisDateParam statBasisDateParam);		// 목록
    public int selectStatBasisDateListCount(StatBasisDateParam statBasisDateParam);					    // 목록건수
}
