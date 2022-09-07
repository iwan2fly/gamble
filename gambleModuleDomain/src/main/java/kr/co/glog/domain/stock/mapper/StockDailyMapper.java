package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StockDailyMapper {
    public int insertStockDaily( StockDaily stockDaily );									    // 등록
    public int insertsStockDaily( ArrayList<StockDaily> stockDailyList );		                // 대량등록
    public int updateStockDaily( StockDaily stockDaily );									    // 수정
    public int deleteStockDaily( StockDaily stockDaily );									    // 삭제
    public ArrayList<StockDailyResult> selectStockDailyList(StockDailyParam stockDailyParam);		// 목록
    public int selectStockDailyListCount(StockDailyParam stockDailyParam);					    // 목록건수
}
