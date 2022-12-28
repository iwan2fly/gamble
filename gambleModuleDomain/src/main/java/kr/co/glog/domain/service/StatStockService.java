package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stat.stock.dao.StatStockDao;
import kr.co.glog.domain.stat.stock.entity.StatStock;
import kr.co.glog.domain.stock.PeriodCode;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 *  주식 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatStockService {

    private final StatStockDao statStockDao;
    private final StockDailyDao stockDailyDao;


    /**
     * @param stockCode
     * 년간 지수 통계
     * @param yyyymmdd
     */
    public void makeStatStockYear( String stockCode, String yyyymmdd ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String year = yyyymmdd.substring(0, 4);
        makeStatStockCommon( stockCode, PeriodCode.year, 0, year + "0101", year + "1231" );
    }

    /**
     * 월간 지수 통계
     * @param stockCode
     * @param yyyymmdd
     */
    public void makeStatStockMonth( String stockCode, String yyyymmdd ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String year = yyyymmdd.substring(0, 4);
        String month = yyyymmdd.substring(4, 6);
        makeStatStockCommon( stockCode, PeriodCode.month, 0, year + month + "01", year + month + "31" );
    }

    /**
     * 월간 지수 통계
     * @param stockCode
     * @param yyyymmdd
     */
    public void makeStatStockWeek( String stockCode, String yyyymmdd ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        Integer turn = DateUtil.getWeekOfYear( yyyymmdd );
        String startDate = DateUtil.getFirstDateOfWeek( yyyymmdd );
        String endDate = DateUtil.getLastDateOfWeek( yyyymmdd );

        makeStatStockCommon( stockCode, PeriodCode.week, turn, startDate, endDate );
    }

    /**
     * 특정 기간 동안의 지수 일반 통계
     * @param startDate
     * @param endDate
     */
    public void makeStatStockCommon(String stockCode, String periodCode, Integer turn, String startDate, String endDate ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        StatStock statStock = new StatStock();
        statStock.setStockCode( stockCode );
        statStock.setPeriodCode( periodCode);
        statStock.setStartDate( startDate );
        statStock.setEndDate( endDate );

        statStock.setMonth( 0 );
        statStock.setYearWeek( startDate.substring(0, 4) + "00" );
        if ( periodCode.equals( PeriodCode.year ) ) {
            statStock.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
        } else if ( periodCode.equals( PeriodCode.month ) ) {
            statStock.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statStock.setMonth( Integer.parseInt( startDate.substring(4, 6) ) );
        } else if ( periodCode.equals( PeriodCode.week ) ) {
            statStock.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statStock.setMonth( Integer.parseInt( startDate.substring(4, 6) ) );
            statStock.setYearWeek( DateUtil.getYearWeek( startDate ) );
            statStock.setWeek( DateUtil.getWeekOfYear( startDate ) );
        }

        // 일반 통계, 최소/최고/평균 등
        StockDailyResult stockDailyResult = stockDailyDao.getStatStockCommon( stockCode, startDate, endDate );
        statStock.setTradeDays( stockDailyResult.getDataCount() );
        statStock.setPriceLow( stockDailyResult.getPriceLow() );
        statStock.setPriceHigh( stockDailyResult.getPriceHigh() );
        statStock.setPriceAverage( stockDailyResult.getPriceAverage() );
        statStock.setVolumeLow( stockDailyResult.getVolumeLow() );
        statStock.setVolumeHigh( stockDailyResult.getVolumeHigh() );
        statStock.setVolumeAverage( stockDailyResult.getVolumeAverage() );

        // 가격 표준편차
        StockDailyResult priceResult = stockDailyDao.getStatStockPriceStdDev( stockCode, startDate, endDate, stockDailyResult.getPriceAverage() );
        statStock.setPriceStandardDeviation( priceResult.getPriceStandardDeviation() );

        // 거래량 표준편차
        StockDailyResult volumeResult = stockDailyDao.selectStatStockVolumeStdDev( stockCode, startDate, endDate, stockDailyResult.getVolumeAverage() );
        statStock.setVolumeStandardDeviation( volumeResult.getVolumeStandardDeviation() );

        // 최저가 일자
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "priceLow");
        pagingParam.setSortType("asc");
        pagingParam.setRows(1);
        StockDailyParam stockDailyParam = new StockDailyParam();
        stockDailyParam.setPagingParam( pagingParam );
        stockDailyParam.setStockCode( stockCode );
        stockDailyParam.setStartDate( startDate );
        stockDailyParam.setEndDate( endDate );
        ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
        statStock.setPriceLowDate( stockDailyList.get(0).getTradeDate() );

        // 최고가 일자
        pagingParam.setSortIndex( "priceHigh");
        pagingParam.setSortType("desc");
        pagingParam.setRows(1);
        stockDailyParam.setPagingParam( pagingParam );
        stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
        statStock.setPriceHighDate( stockDailyList.get(0).getTradeDate() );

        // 최저거래량 / 최고거래량 일자
        pagingParam.setSortIndex( "volumeHigh");
        pagingParam.setSortType("desc");
        pagingParam.setRows(0);
        stockDailyParam.setPagingParam( pagingParam );
        stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
        statStock.setVolumeHighDate( stockDailyList.get(0).getTradeDate() );
        statStock.setVolumeLowDate( stockDailyList.get( stockDailyList.size()-1).getTradeDate() );

        // 시가 / 종가
        pagingParam.setSortIndex( "tradeDate");
        pagingParam.setSortType("asc");
        pagingParam.setRows(0);
        stockDailyParam.setPagingParam( pagingParam );
        stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
        statStock.setPriceStart( stockDailyList.get(0).getPriceStart() );
        statStock.setPriceFinal( stockDailyList.get( stockDailyList.size()-1).getPriceFinal() );

        // 이전 종가 ( 전일 종가 )
        pagingParam.setSortIndex( "tradeDate");
        pagingParam.setSortType("desc");
        pagingParam.setRows(1);
        stockDailyParam.setPagingParam( pagingParam );
        stockDailyParam.setStartDate( null );
        stockDailyParam.setEndDate( startDate );
        stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );
        statStock.setPricePrevious( stockDailyList.get(0).getPriceFinal() );

        // 변동가격 / 변동률
        Integer pricePrevious = statStock.getPricePrevious();
        Integer priceNow = statStock.getPriceFinal();
        statStock.setPriceChange( statStock.getPriceFinal() - statStock.getPricePrevious() );
        statStock.setRateChange( Math.round( statStock.getPriceChange() / statStock.getPricePrevious() * 10000 ) / 100f );      // 소수점 2자리까지만 남김

        statStockDao.saveStatStock( statStock );
   }



}


