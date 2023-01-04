package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StockDailyMapper {


    public ArrayList<StockDailyResult> selectStockListBetween(StockDailyParam stockDailyParam);		// 특정 기간 거래된 주식 목록

    public StockDailyResult selectStatStockCommon(StockDailyParam indexDailyParam  );		        // 주식 평균
    public StockDailyResult selectStatStockPriceStdDev(StockDailyParam indexDailyParam );	        // 주식 가격 표준편차
    public StockDailyResult selectStatStockVolumeStdDev( StockDailyParam indexDailyParam );	        // 주식 거래량 표준편차
    public StockDailyResult selectStatStockForeignerStdDev( StockDailyParam indexDailyParam );	        // 외국인 보유량 표준편차


    public int insertStockDaily( StockDaily stockDaily );									    // 등록
    public int insertsStockDaily( ArrayList<StockDaily> stockDailyList );		                // 대량등록
    public int updateStockDaily( StockDaily stockDaily );									    // 수정
    public int deleteStockDaily( StockDaily stockDaily );									    // 삭제
    public ArrayList<StockDailyResult> selectStockDailyList(StockDailyParam stockDailyParam);		// 목록
    public int selectStockDailyListCount(StockDailyParam stockDailyParam);					    // 목록건수
}
