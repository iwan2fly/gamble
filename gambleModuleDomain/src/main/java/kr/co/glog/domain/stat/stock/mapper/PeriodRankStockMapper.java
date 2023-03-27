package kr.co.glog.domain.stat.stock.mapper;

import kr.co.glog.domain.stat.stock.entity.PeriodRankStock;
import kr.co.glog.domain.stat.stock.model.PeriodRankStockParam;
import kr.co.glog.domain.stat.stock.model.PeriodRankStockResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface PeriodRankStockMapper {

    public int insertPeriodRankFromStatStock( PeriodRankStockParam periodRankStockParam );
    public int insertPeriodRankStock( PeriodRankStock periodRankStock);									    // 등록
    public int insertsPeriodRankStock( ArrayList<PeriodRankStock> periodRankStockList);		                // 대량등록
    public int updatePeriodRankStock( PeriodRankStock periodRankStock);									    // 수정
    public int deletePeriodRankStock( PeriodRankStock periodRankStock);									    // 삭제
    public ArrayList<PeriodRankStockResult> selectPeriodRankStockList(PeriodRankStockParam periodRankStockParam);		// 목록
    public int selectPeriodRankStockListCount(PeriodRankStockParam periodRankStockParam);					    // 목록건수
}
