package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.DartCorp;
import kr.co.glog.domain.stock.model.DartCorpParam;
import kr.co.glog.domain.stock.model.DartCorpResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface DartCorpMapper {
    public int insertDartCorp( DartCorp dartCorp );									    // 등록
    public int insertsDartCorp( ArrayList<DartCorp> dartCorpList );		                // 대량등록
    public int updateDartCorp( DartCorp dartCorp );									    // 수정
    public int deleteDartCorp( DartCorp dartCorp );									    // 삭제
    public ArrayList<DartCorpResult> selectDartCorpList(DartCorpParam dartCorpParam);		// 목록
    public int selectDartCorpListCount(DartCorpParam dartCorpParam);					    // 목록건수
}
