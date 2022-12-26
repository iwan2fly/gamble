package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.domain.stock.model.IndexDailyParam;
import kr.co.glog.domain.stock.model.IndexDailyResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface IndexDailyMapper {

    public IndexDailyResult selectStatIndexCommon( IndexDailyParam indexDailyParam  );		        // 지수 평균
    public IndexDailyResult selectStatIndexPriceStdDev(IndexDailyParam indexDailyParam );	        // 지수 가격 표준편차
    public IndexDailyResult selectStatIndexVolumeStdDev( IndexDailyParam indexDailyParam );	        // 지수 거래량 표준편차

    public int insertIndexDaily( IndexDaily indexDaily );									    // 등록
    public int insertsIndexDaily( ArrayList<IndexDaily> indexDailyList );		                // 대량등록
    public int updateIndexDaily( IndexDaily indexDaily );									    // 수정
    public int deleteIndexDaily( IndexDaily indexDaily );									    // 삭제
    public ArrayList<IndexDailyResult> selectIndexDailyList(IndexDailyParam indexDailyParam);		// 목록
    public int selectIndexDailyListCount(IndexDailyParam indexDailyParam);					    // 목록건수
}
