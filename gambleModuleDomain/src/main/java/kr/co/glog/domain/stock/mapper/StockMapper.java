package kr.co.glog.domain.stock.mapper;

import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface StockMapper {

    public ArrayList<StockResult> selectCompanyIdList();                        // 회사코드가 있는 녀석들 리턴
    public int updateCompanyCodeFromCompany();                                      // company 테이블의 companyId를 stock 의 companyId 에 업데이트
    public int insertStock( Stock stock );									    // 등록
    public int insertsStock( ArrayList<Stock> stockList );		                // 대량등록
    public int updateStock( Stock stock );									    // 수정
    public int deleteStock( Stock stock );									    // 삭제
    public ArrayList<StockResult> selectStockList(StockParam stockParam);		// 목록
    public int selectStockListCount(StockParam stockParam);					    // 목록건수
}
