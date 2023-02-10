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

    /**
     * 특정 기간동안 거래되었던 주식 목록
     * @param startDate
     * @param endDate
     * @return
     */
    public ArrayList<StockDailyResult> getStockListBetween ( String marketCode, String startDate, String endDate ) {
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStartDate( startDate );
        stockDailyParam.setEndDate( endDate );
        stockDailyParam.setMarketCode( marketCode );
        return stockDailyMapper.selectStockListBetween( stockDailyParam );
    }

    /**
     * 거래일수, 최대값, 최소값, 평균을 구합니다.
     * @param stockCode 주식코드, startDate 시작일, endDate 종료일
     * @return
     */
    public StockDailyResult getStatStockCommon(String stockCode, String startDate, String endDate) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockCode( stockCode );
        stockDailyParam.setStartDate( startDate );
        stockDailyParam.setEndDate( endDate );
        return stockDailyMapper.selectStatStockCommon( stockDailyParam );
    }


    /**
     * 주식 가격에 대한 표준편차를 구합니다.
     * @param stockCode 주식코드, startDate 작일, endDate 종료일, averagePrice 평균가격
     * @return
     */
    public StockDailyResult getStatStockPriceStdDev(String stockCode, String startDate, String endDate, Integer priceAverage ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( priceAverage == null ) throw new ParameterMissingException( "priceAverage" );

        // 데이터 전체 건수가 필요함
        StockDailyResult stockDailyResult = this.getStatStockCommon( stockCode, startDate, endDate );

        return this.getStatStockPriceStdDev( stockCode, startDate, endDate, priceAverage, stockDailyResult.getDataCount() );
    }

    public StockDailyResult getStatStockPriceStdDev(String stockCode, String startDate, String endDate, Integer priceAverage, Integer dataCount ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( priceAverage == null ) throw new ParameterMissingException( "priceAverage" );
        if ( dataCount == null ) throw new ParameterMissingException( "dataCount" );

        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockCode( stockCode );
        stockDailyParam.setStartDate( startDate );
        stockDailyParam.setEndDate( endDate );
        stockDailyParam.setPriceAverage( priceAverage );
        stockDailyParam.setDataCount( dataCount );

        StockDailyResult stockDailyResult = stockDailyMapper.selectStatStockPriceStdDev( stockDailyParam );


        return stockDailyResult;
    }


    /**
     * 주식 거래량에 대한 대한 표준편차를 구합니다.
     * @param stockCode 주식코드, startDate 시작일, endDate 종료일, averageVolume 평균 거래량
     * @return
     */
    public StockDailyResult selectStatStockVolumeStdDev( String stockCode, String startDate, String endDate, Long volumeAverage ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( volumeAverage == null ) throw new ParameterMissingException( "volumeAverage" );

        // 데이터 전체 건수가 필요함
        StockDailyResult stockDailyResult = this.getStatStockCommon( stockCode, startDate, endDate );

        return this.selectStatStockVolumeStdDev( stockCode, startDate, endDate, volumeAverage, stockDailyResult.getDataCount() );
    }

    public StockDailyResult selectStatStockVolumeStdDev( String stockCode, String startDate, String endDate, Long volumeAverage, Integer dataCount ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( volumeAverage == null ) throw new ParameterMissingException( "volumeAverage" );
        if ( dataCount == null ) throw new ParameterMissingException( "dataCount" );

        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockCode( stockCode );
        stockDailyParam.setStartDate( startDate );
        stockDailyParam.setEndDate( endDate );
        stockDailyParam.setVolumeAverage( volumeAverage );
        stockDailyParam.setDataCount( dataCount );

        log.debug( stockDailyParam.toString() );
        StockDailyResult stockDailyResult = stockDailyMapper.selectStatStockVolumeStdDev( stockDailyParam );
        log.debug( stockDailyResult.toString() );

        return stockDailyResult;
    }


    /**
     * 외국인 보유량에 대한 대한 표준편차를 구합니다.
     * @param stockCode 주식코드, startDate 시작일, endDate 종료일, averageVolume 평균 거래량
     * @return
     */
    public StockDailyResult selectStatStockForeignerStdDev( String stockCode, String startDate, String endDate, Long foreignerAverage ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( foreignerAverage == null ) throw new ParameterMissingException( "foreignerAverage" );

        // 데이터 전체 건수가 필요함
        StockDailyResult stockDailyResult = this.getStatStockCommon( stockCode, startDate, endDate );

        return this.selectStatStockVolumeStdDev( stockCode, startDate, endDate, foreignerAverage, stockDailyResult.getDataCount() );
    }

    public StockDailyResult selectStatStockForeignerStdDev( String stockCode, String startDate, String endDate, Long foreignerAverage, Integer dataCount ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );
        if ( foreignerAverage == null ) throw new ParameterMissingException( "foreignerAverage" );
        if ( dataCount == null ) throw new ParameterMissingException( "dataCount" );

        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setStockCode( stockCode );
        stockDailyParam.setStartDate( startDate );
        stockDailyParam.setEndDate( endDate );
        stockDailyParam.setForeignerAverage( foreignerAverage );
        stockDailyParam.setDataCount( dataCount );

        log.debug( stockDailyParam.toString() );
        StockDailyResult stockDailyResult = stockDailyMapper.selectStatStockForeignerStdDev( stockDailyParam );
        log.debug( stockDailyResult.toString() );

        return stockDailyResult;
    }
    
    
    
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
        int startStock = 0;
        int endStock = insertSize;
        while ( remainSize > insertSize ) {

            ArrayList<StockDaily> subList = (ArrayList) stockDailyList.subList( startStock, endStock );
            insertCount += stockDailyMapper.insertsStockDaily( subList );
            remainSize -= insertSize;           // insert 숫자만큼 남은 데이터 개수 줄임
            startStock += insertSize;           // startStock 를 다음으로
            endStock += insertSize;             // endStock를 다음으로
            if ( endStock > stockDailyList.size() ) endStock = stockDailyList.size();     // 마지막 인덱스가 배열을 벗어나면 마지막 인덱스는 배열 마지막으로
        }

        if ( startStock < stockDailyList.size() ) {
            ArrayList<StockDaily> subList = (ArrayList) stockDailyList.subList(startStock, endStock);
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

    public StockDaily insertUpdateStockDaily(StockDaily stockDaily) {
        if ( stockDaily == null ) throw new ParameterMissingException( "StockDaily" );
        if ( stockDaily.getStockCode() == null || stockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");

        try {
            stockDailyMapper.insertStockDaily(stockDaily);
        } catch ( org.springframework.dao.DuplicateKeyException dke ) {
            stockDailyMapper.updateStockDaily(stockDaily);
        }

        return stockDaily;
    }

    public StockDaily updateInsertStockDaily(StockDaily stockDaily) {
        if ( stockDaily == null ) throw new ParameterMissingException( "StockDaily" );
        if ( stockDaily.getStockCode() == null || stockDaily.getTradeDate() == null ) throw new ParameterMissingException( "종목코드와 날짜는 필수값입니다.");

        try {
            if ( stockDailyMapper.updateStockDaily(stockDaily) == 0 ) {
                stockDailyMapper.insertStockDaily(stockDaily);
            };
        } catch ( Exception e ) {
            stockDailyMapper.insertStockDaily(stockDaily);
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
