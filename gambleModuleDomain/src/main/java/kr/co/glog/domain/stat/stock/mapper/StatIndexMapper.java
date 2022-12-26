package kr.co.glog.domain.stat.stock.mapper;

import kr.co.glog.domain.stat.stock.entity.StatIndex;
import kr.co.glog.domain.stat.stock.model.StatIndexParam;
import kr.co.glog.domain.stat.stock.model.StatIndexResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StatIndexMapper {
    public int insertStatIndex( StatIndex statIndex );									    // 등록
    public int insertsStatIndex( ArrayList<StatIndex> statIndexList );		                // 대량등록
    public int updateStatIndex( StatIndex statIndex );									    // 수정
    public int deleteStatIndex( StatIndex statIndex );									    // 삭제
    public ArrayList<StatIndexResult> selectStatIndexList(StatIndexParam statIndexParam);		// 목록
    public int selectStatIndexListCount(StatIndexParam statIndexParam);					    // 목록건수
}
