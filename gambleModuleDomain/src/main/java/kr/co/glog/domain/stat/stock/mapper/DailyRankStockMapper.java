package kr.co.glog.domain.stat.stock.mapper;

import kr.co.glog.domain.stat.stock.entity.DailyRankStock;
import kr.co.glog.domain.stat.stock.model.DailyRankStockParam;
import kr.co.glog.domain.stat.stock.model.DailyRankStockResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface DailyRankStockMapper {
    public int  insertDailyRankFromStockDaily( DailyRankStock dailyRankStock);                              // stockDaily 에서 순위를 뽑아서 insert
    public int insertDailyRankStock( DailyRankStock dailyRankStock);									    // 등록
    public int insertsDailyRankStock( ArrayList<DailyRankStock> dailyRankStockList);		                // 대량등록
    public int updateDailyRankStock( DailyRankStock dailyRankStock);									    // 수정
    public int deleteDailyRankStock( DailyRankStock dailyRankStock);									    // 삭제
    public ArrayList<DailyRankStockResult> selectDailyRankStockList(DailyRankStockParam dailyRankStockParam);		// 목록
    public int selectDailyRankStockListCount(DailyRankStockParam dailyRankStockParam);					    // 목록건수
}
