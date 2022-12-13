package kr.co.glog.domain.stock.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.mapper.StockDailyMapper;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class StockDailyDao {

    private final StockDailyMapper stockDailyMapper;

    // 키 SELECT
    public StockDailyResult getStockDaily(Long stockDailyId ) {
        if ( stockDailyId == null ) throw new ParameterMissingException( "stockDailyId" );

        StockDailyResult stockDailyResult = null;
        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockDailyId( stockDailyId );
        ArrayList<StockDailyResult> stockDailyList = stockDailyMapper.selectStockDailyList( stockDailyParam );
        if ( stockDailyList != null && stockDailyList.size() > 0 ) stockDailyResult = stockDailyList.get(0);
        return stockDailyResult;
    }

    // 유니크 SELECT
    public StockDailyResult getStockDaily( String stockCode, String tradeDate ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( tradeDate == null ) throw new ParameterMissingException( "tradeDate" );

        StockDailyResult stockDailyResult = null;
        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockCode( stockCode );
        stockDailyParam.setTradeDate( tradeDate );
        ArrayList<StockDailyResult> stockDailyList = stockDailyMapper.selectStockDailyList( stockDailyParam );
        if ( stockDailyList != null && stockDailyList.size() > 0 ) stockDailyResult = stockDailyList.get(0);
        return stockDailyResult;
    }


    public ArrayList<StockDailyResult> getStockDailyList(StockDailyParam stockDailyParam ) {
        if ( stockDailyParam == null ) throw new ParameterMissingException( "stockDailyParam" );

        return stockDailyMapper.selectStockDailyList( stockDailyParam );
    }

    public int insertStockDaily ( StockDaily stockDaily ) {

        if ( stockDaily == null ) throw new ParameterMissingException( "StockDaily" );
        if ( stockDaily.getStockCode() == null || stockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");

        return stockDailyMapper.insertStockDaily(stockDaily);
    }

    /**
     * 한번에 종목 10개씩 insert
     * @param stockDailyList
     * @return
     */
    public int insertsStockDaily( ArrayList<StockDaily> stockDailyList) {
        return insertsStockDaily(stockDailyList, 1000 );
    }

    /**
     * 한번에 지정된 insert 개수만큼 insert
     * @param stockDailyList
     * @param insertSize
     * @return
     */
    public int insertsStockDaily(ArrayList<StockDaily> stockDailyList, int insertSize ) {
        if ( stockDailyList == null ) throw new ParameterMissingException( "stockDailyList" );

        int insertCount = 0;
        int remainSize = stockDailyList.size();
        int startIndex = 0;
        int endIndex = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StockDaily> subList = (ArrayList) stockDailyList.subList( startIndex, endIndex );
            insertCount += stockDailyMapper.insertsStockDaily( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startIndex += insertSize;           // startIndex 를 다음으로
            endIndex += insertSize;             // endIndex를 다음으로
            if ( endIndex > stockDailyList.size() ) endIndex = stockDailyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startIndex < stockDailyList.size() ) {
            ArrayList<StockDaily> subList = (ArrayList) stockDailyList.subList(startIndex, endIndex);
            insertCount += stockDailyMapper.insertsStockDaily(subList);
        }

        return insertCount;
    }

    public StockDaily updateStockDaily( StockDaily stockDaily ) {
        if ( stockDaily == null ) throw new ParameterMissingException( "StockDaily" );
        if ( stockDaily.getStockCode() == null || stockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");
        stockDailyMapper.updateStockDaily(stockDaily);
        return stockDaily;
    }

    public StockDaily saveStockDaily(StockDaily stockDaily) {
        if ( stockDaily == null ) throw new ParameterMissingException( "StockDaily" );
        if ( stockDaily.getStockCode() == null || stockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");

        try {
            stockDailyMapper.insertStockDaily(stockDaily);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            stockDailyMapper.updateStockDaily(stockDaily);
        }

        return stockDaily;
    }

    public void deleteStockDaily( Long stockDailyId ) {
        if ( stockDailyId == null ) throw new ParameterMissingException( "stockDailyId" );

        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockDailyId( stockDailyId );
        stockDailyMapper.deleteStockDaily( stockDailyParam );
    }




}
