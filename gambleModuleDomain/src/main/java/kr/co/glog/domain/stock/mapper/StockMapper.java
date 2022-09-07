package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StockMapper {
    public int insertStock( Stock stock );									    // 등록
    public int insertsStock( ArrayList<Stock> stockList );		                // 대량등록
    public int updateStock( Stock stock );									    // 수정
    public int deleteStock( Stock stock );									    // 삭제
    public ArrayList<StockResult> selectStockList(StockParam stockParam);		// 목록
    public int selectStockListCount(StockParam stockParam);					    // 목록건수
}
