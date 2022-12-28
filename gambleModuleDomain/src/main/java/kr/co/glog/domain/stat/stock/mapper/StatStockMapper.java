package kr.co.glog.domain.stat.stock.mapper;

import kr.co.glog.domain.stat.stock.entity.StatStock;
import kr.co.glog.domain.stat.stock.model.StatStockParam;
import kr.co.glog.domain.stat.stock.model.StatStockResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StatStockMapper {
    public int insertStatStock( StatStock statStock );									    // 등록
    public int insertsStatStock( ArrayList<StatStock> statStockList );		                // 대량등록
    public int updateStatStock( StatStock statStock );									    // 수정
    public int deleteStatStock( StatStock statStock );									    // 삭제
    public ArrayList<StatStockResult> selectStatStockList(StatStockParam statStockParam);		// 목록
    public int selectStatStockListCount(StatStockParam statStockParam);					    // 목록건수
}
