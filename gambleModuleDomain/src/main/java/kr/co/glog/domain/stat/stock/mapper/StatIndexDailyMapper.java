package kr.co.glog.domain.stat.stock.mapper;

import kr.co.glog.domain.stat.stock.entity.StatIndexDaily;
import kr.co.glog.domain.stat.stock.model.StatIndexDailyParam;
import kr.co.glog.domain.stat.stock.model.StatIndexDailyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StatIndexDailyMapper {
    public int insertStatIndexDaily( StatIndexDaily statIndexDaily );									    // 등록
    public int insertsStatIndexDaily( ArrayList<StatIndexDaily> statIndexDailyList );		                // 대량등록
    public int updateStatIndexDaily( StatIndexDaily statIndexDaily );									    // 수정
    public int deleteStatIndexDaily( StatIndexDaily statIndexDaily );									    // 삭제
    public ArrayList<StatIndexDailyResult> selectStatIndexDailyList(StatIndexDailyParam statIndexDailyParam);		// 목록
    public int selectStatIndexDailyListCount(StatIndexDailyParam statIndexDailyParam);					    // 목록건수
}
