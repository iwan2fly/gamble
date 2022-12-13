package kr.co.glog.domain.stat.stock.mapper;

import kr.co.glog.domain.stat.stock.entity.StatStockDaily;
import kr.co.glog.domain.stat.stock.model.StatStockDailyParam;
import kr.co.glog.domain.stat.stock.model.StatStockDailyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StatStockDailyMapper {
    public int insertStatStockDaily( StatStockDaily statStockDaily );									    // 등록
    public int insertsStatStockDaily( ArrayList<StatStockDaily> statStockDailyList );		                // 대량등록
    public int updateStatStockDaily( StatStockDaily statStockDaily );									    // 수정
    public int deleteStatStockDaily( StatStockDaily statStockDaily );									    // 삭제
    public ArrayList<StatStockDailyResult> selectStatStockDailyList(StatStockDailyParam statStockDailyParam);		// 목록
    public int selectStatStockDailyListCount(StatStockDailyParam statStockDailyParam);					    // 목록건수
}
