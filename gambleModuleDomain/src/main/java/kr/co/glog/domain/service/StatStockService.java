package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stat.stock.dao.StatStockDao;
import kr.co.glog.domain.stat.stock.entity.StatStock;
import kr.co.glog.domain.stat.stock.model.StatIndexParam;
import kr.co.glog.domain.stat.stock.model.StatIndexResult;
import kr.co.glog.domain.stat.stock.model.StatStockParam;
import kr.co.glog.domain.stat.stock.model.StatStockResult;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.PeriodCode;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.model.StockDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;


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
     * 특정년도 통계
     * @param stockCode
     * @param year
     * @return
     */
    public StatStockResult getStatStockYear(String stockCode, Integer year ) {
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( 0 );
        statStockParam.setYearOrder( year + "00" );
        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );

        StatStockResult statStockResult = null;
        if ( statStockList != null && statStockList.size() == 1 ) statStockResult = statStockList.get(0);
        return statStockResult;
    }

    /**
     * 특정 월 통계
     * @param stockCode
     * @param year
     * @param month
     * @return
     */
    public StatStockResult getStatStockMonth(String stockCode, Integer year, Integer month ) {
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( month );
        statStockParam.setYearOrder( year + "00" );
        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );

        StatStockResult statStockResult = null;
        if ( statStockList != null && statStockList.size() == 1 ) statStockResult = statStockList.get(0);
        return statStockResult;
    }

    /**
     * 특정 주차 통계
     * @param stockCode
     * @param yearOrder
     * @return
     */
    public StatStockResult getStatStockWeek(String stockCode, String yearOrder ) {
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setYear( Integer.parseInt( yearOrder.substring(0,4) ) );
        statStockParam.setYearOrder( yearOrder );
        ArrayList<StatStockResult> statStockList =  statStockDao.getStatStockList( statStockParam );

        StatStockResult statStockResult = null;
        if ( statStockList != null && statStockList.size() == 1 ) statStockResult = statStockList.get(0);
        return statStockResult;
    }


    /**
     * 전체 연간 데이터 리턴
     * @param stockCode
     * @return
     */
    public ArrayList<StatStockResult> getStatStockYearlyList(String stockCode, Integer endYear ) {
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setPeriodCode( PeriodCode.year );
        statStockParam.setEndYear( endYear );
        log.debug( statStockParam.toString() );
        return statStockDao.getStatStockList( statStockParam );
    }

    /**
     * 특정 년도의 월간 데이터 리턴
     * @param stockCode
     * @param year
     * @return
     */
    public ArrayList<StatStockResult> getStatStockMonthlyListOfYear( String stockCode, Integer year ) {
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setYear( year );
        statStockParam.setPeriodCode( PeriodCode.month );
        statStockParam.setYearOrder( year + "00" );
        return statStockDao.getStatStockList( statStockParam );
    }

    /**
     * 시작월과 종료월 사이의 월간 데이터 리턴
     * @param stockCode
     * @param startYearMonth
     * @param endYearMonth
     * @return
     */
    public ArrayList<StatStockResult> getStatStockMonthlyList(String stockCode, String startYearMonth, String endYearMonth ) {

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setPeriodCode( PeriodCode.month );
        statStockParam.setStockCode( stockCode );
        statStockParam.setStartYearMonth( startYearMonth );
        statStockParam.setEndYearMonth(endYearMonth);

        return statStockDao.getStatStockList( statStockParam );
    }

    /**
     * 특정 년도의 주간 데이터 리턴
     * @param stockCode
     * @param year
     * @return
     */
    public ArrayList<StatStockResult> getStatStockWeeklyListOfYear(String stockCode, Integer year ) {
        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setStockCode( stockCode );
        statStockParam.setYear( year );
        statStockParam.setPeriodCode( PeriodCode.week );
        return statStockDao.getStatStockList( statStockParam );
    }

    public ArrayList<StatStockResult> getStatStockWeeklyList(String stockCode, String endYearWeek, Integer rows ) {

        PagingParam pagingParam = new PagingParam();
        pagingParam.setRows( rows );
        pagingParam.setSortIndex( "yearOrder");
        pagingParam.setSortType("desc");

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setPagingParam( pagingParam );
        statStockParam.setPeriodCode( PeriodCode.week );
        statStockParam.setStockCode( stockCode );
        statStockParam.setEndYearWeek(endYearWeek);

        ArrayList<StatStockResult> list = statStockDao.getStatStockList( statStockParam );
        Collections.reverse( list );
        return list;
    }



    /**
     *  2020년부터 연간/월간/주간 주식 upsert
     */
    public void makeStatStockAll() {

        // 연간
        makeStatStockYear( "2020" );
        makeStatStockYear( "2021" );
        makeStatStockYear( "2022" );

        // 월간
        for ( int year = 2020; year <= 2023; year++ ) {
            for (int i = 1; i <= 12; i++) {
                String month = i < 10 ? "0" + i : "" + i;
                makeStatStockMonth( "" + year + month );
            }
        }

        int date = 20221016;
        while ( date < 20230102 ) {
            String dateStr = "" + date;
            makeStatStockWeek( dateStr );

            dateStr = DateUtil.addDate( dateStr, "D", "yyyyMMdd", 7 );
            date = Integer.parseInt( dateStr );
        }
    }


    
    
    /**
     * 년간 주식 통계
     * @param yyyy
     */
    public void makeStatStockYear( String yyyy ) {
        if ( yyyy == null ) throw new ParameterMissingException( "yyyy" );

        ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockListBetween( MarketCode.kospi, yyyy + "0101", yyyy + "1231" );
        ArrayList<StockDailyResult> kosdaqList = stockDailyDao.getStockListBetween( MarketCode.kosdaq, yyyy + "0101", yyyy + "1231" );
        stockDailyList.addAll( kosdaqList );

        for ( StockDailyResult stockDailyResult : stockDailyList ) {
            makeStatStockYear( stockDailyResult.getStockCode(), yyyy );
        }
    }

    public void makeStatStockYear( String stockCode, String yyyy ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( yyyy == null ) throw new ParameterMissingException( "yyyy" );

        makeStatStockCommon( stockCode, PeriodCode.year, yyyy + "0101", yyyy + "1231" );
    }

    /**
     * 월간 주식 통계
     * @param yyyymm
     */
    public void makeStatStockMonth( String yyyymm ) {
        if ( yyyymm == null ) throw new ParameterMissingException( "yyyymm" );

        ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockListBetween( MarketCode.kospi, yyyymm + "01", yyyymm + "31" );
        ArrayList<StockDailyResult> kosdaqList = stockDailyDao.getStockListBetween( MarketCode.kosdaq, yyyymm + "01", yyyymm + "31" );
        stockDailyList.addAll( kosdaqList );

        for ( StockDailyResult stockDailyResult : stockDailyList ) {
            makeStatStockMonth( stockDailyResult.getStockCode(), yyyymm );
        }
    }
    public void makeStatStockMonth( String stockCode, String yyyymm ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( yyyymm == null ) throw new ParameterMissingException( "yyyymm" );

        makeStatStockCommon( stockCode, PeriodCode.month, yyyymm + "01", yyyymm + "31" );
    }

    /**
     * 주간 주식 통계
     * @param yyyymmdd
     * @param yyyymmdd
     */
    public void makeStatStockWeek( String yyyymmdd ) {
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );
        String startDate = DateUtil.getFirstDateOfWeek( yyyymmdd );
        String endDate = DateUtil.getLastDateOfWeek( yyyymmdd );

        ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockListBetween( MarketCode.kospi, startDate, endDate );
        ArrayList<StockDailyResult> kosdaqList = stockDailyDao.getStockListBetween( MarketCode.kosdaq, startDate, endDate );
        stockDailyList.addAll( kosdaqList );

        for ( StockDailyResult stockDailyResult : stockDailyList ) {
            makeStatStockWeek( stockDailyResult.getStockCode(), yyyymmdd );
        }
    }

    public void makeStatStockWeek( String stockCode, String yyyymmdd ) {
        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String yearOrder = DateUtil.getYearWeek( yyyymmdd );
        String startDate = DateUtil.getFirstDateOfWeek( yyyymmdd );
        String endDate = DateUtil.getLastDateOfWeek( yyyymmdd );

        makeStatStockCommon( stockCode, PeriodCode.week, startDate, endDate, yearOrder );
    }

    /**
     * 특정 기간 동안의 주식 일반 통계
     * @param startDate
     * @param endDate
     */
    public void makeStatStockCommon(String stockCode, String periodCode, String startDate, String endDate ) {
        makeStatStockCommon( stockCode, periodCode, startDate, endDate,null );
    }

    public void makeStatStockCommon(String stockCode, String periodCode, String startDate, String endDate, String yearOrder ) {

        if ( stockCode == null ) throw new ParameterMissingException( "stockCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        StatStock statStock = new StatStock();
        statStock.setStockCode( stockCode );
        statStock.setPeriodCode( periodCode);

        statStock.setMonth( 0 );
        if ( periodCode.equals( PeriodCode.year ) ) {
            statStock.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statStock.setYearOrder( statStock.getYear() + "00" );
        } else if ( periodCode.equals( PeriodCode.month ) ) {
            statStock.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statStock.setMonth( Integer.parseInt( startDate.substring(4, 6) ) );
            statStock.setYearOrder( startDate.substring(0,6) );
        } else if ( periodCode.equals( PeriodCode.week ) ) {
            statStock.setYear( Integer.parseInt( yearOrder.substring(0, 4) ) );
            statStock.setYearOrder( DateUtil.getYearWeek( startDate ) );
            statStock.setMonth( Integer.parseInt( endDate.substring(4, 6) ) );
            statStock.setWeek( DateUtil.getWeekOfYear( startDate ) );
        }

        // 일반 통계, 최소/최고/평균 등
        StockDailyResult stockDailyResult = stockDailyDao.getStatStockCommon( stockCode, startDate, endDate );

        // 평균값이 산출되지 않으면 표준편차를 구할 수 없음. 데이터가 없는 경우
        if ( stockDailyResult == null || stockDailyResult.getPriceAverage() == null ) {
            log.debug("평균값이 없습니다. 데이터가 없는 것으로 추정됩니다.");
            return;
        }

        statStock.setStockName( stockDailyResult.getStockName() );
        statStock.setMarketCode( stockDailyResult.getMarketCode() );
        statStock.setTradeDays( stockDailyResult.getDataCount() );
        statStock.setPriceLow( stockDailyResult.getPriceLow() );
        statStock.setPriceHigh( stockDailyResult.getPriceHigh() );
        statStock.setPriceAverage( stockDailyResult.getPriceAverage() );
        statStock.setVolumeTrade(( stockDailyResult.getVolumeTrade() ) );
        statStock.setVolumeLow( stockDailyResult.getVolumeLow() );
        statStock.setVolumeHigh( stockDailyResult.getVolumeHigh() );
        statStock.setVolumeAverage( stockDailyResult.getVolumeAverage() );
        statStock.setForeignerHigh( stockDailyResult.getForeignerHigh() );
        statStock.setForeignerLow( stockDailyResult.getForeignerLow() );
        statStock.setForeignerAverage(stockDailyResult.getForeignerAverage() );
        statStock.setPricePrevious( stockDailyResult.getPricePrevious() );
        statStock.setPriceTrade( stockDailyResult.getPriceTrade() );
        statStock.setPriceTotal( stockDailyResult.getPriceTotal() );
        statStock.setPriceTotalPrevious( stockDailyResult.getPriceTotalPrevious() );
        statStock.setVolumeTotal( stockDailyResult.getVolumeTotal() );
        statStock.setVolumeTotalPrevious( stockDailyResult.getVolumeTotalPrevious() );

        statStock.setPriceStart( stockDailyResult.getPriceStart() );
        statStock.setForeignerStart( stockDailyResult.getForeignerStart() );
        statStock.setPriceFinal( stockDailyResult.getPriceFinal() );
        statStock.setForeignerFinal( stockDailyResult.getForeignerFinal() );

        statStock.setPriceLowDate( stockDailyResult.getPriceLowDate() );
        statStock.setPriceHighDate( stockDailyResult.getPriceHighDate() );
        statStock.setVolumeLowDate( stockDailyResult.getVolumeLowDate() );
        statStock.setVolumeHighDate( stockDailyResult.getVolumeHighDate() );

        statStock.setRiseCount( stockDailyResult.getRiseCount() );
        statStock.setEvenCount( stockDailyResult.getEvenCount() );
        statStock.setFallCount( stockDailyResult.getFallCount() );

        statStock.setStartDate( stockDailyResult.getStartDate() );
        statStock.setEndDate( stockDailyResult.getEndDate() );



        // 가격 표준편차
        StockDailyResult priceResult = stockDailyDao.getStatStockPriceStdDev( stockCode, startDate, endDate, stockDailyResult.getPriceAverage(), stockDailyResult.getDataCount() );
        statStock.setPriceStandardDeviation( priceResult.getPriceStandardDeviation() );

        // 거래량 표준편차
        StockDailyResult volumeResult = stockDailyDao.selectStatStockVolumeStdDev( stockCode, startDate, endDate, stockDailyResult.getVolumeAverage(), stockDailyResult.getDataCount() );
        statStock.setVolumeStandardDeviation( volumeResult.getVolumeStandardDeviation() );

        // 외국인 보유량 표준편차
        log.debug( "Foreigner Average : " + statStock.getForeignerAverage() );
        if ( statStock.getForeignerAverage() != null ) {
            StockDailyResult foreignerResult = stockDailyDao.selectStatStockForeignerStdDev(stockCode, startDate, endDate, stockDailyResult.getForeignerAverage(), stockDailyResult.getDataCount() );
            statStock.setForeignerStandardDeviation(foreignerResult.getForeignerStandardDeviation());
        }

        // 변동가격 / 변동률
        statStock.setPriceChange( statStock.getPricePrevious()!=null ? statStock.getPriceFinal() - statStock.getPricePrevious() : null );

        // 가격 변동으로 변동율을 구하면 증자/감자 등에 대응이 안됨
        // 이전 시총 정보가 없으면 그냥 현재 시총으로 설정

        // 주식수가 같으면
        if ( statStock.getVolumeTotal() != null && statStock.getVolumeTotalPrevious() != null && statStock.getVolumeTotal() == statStock.getVolumeTotalPrevious() ) {
            log.debug( "priceTotal calc : " + ( ( statStock.getPriceTotal() - statStock.getPriceTotalPrevious()) / statStock.getPriceTotalPrevious() ) );
            statStock.setRateChange((statStock.getPriceTotalPrevious() != null && statStock.getPriceTotal() != null) ? Math.round(10000f * (statStock.getPriceTotal() - statStock.getPriceTotalPrevious()) / statStock.getPriceTotalPrevious()) / 100f : null);      // 소수점 2자리까지만 남김
        // 주식 수가 달라졌으면
        } else {
            log.debug( "주식수가 달라졌으면" );
            // 해당 주식의 기간 첫날과 마지막날 정보 조회
            StockDailyResult firstStock = stockDailyDao.getStockDaily( statStock.getStockCode(), statStock.getStartDate() );
            StockDailyResult lastStock = stockDailyDao.getStockDaily( statStock.getStockCode(), statStock.getEndDate() );

            long priceFirst = firstStock.getPriceTotal() / firstStock.getVolumeTotal();
            long priceLast = lastStock.getPriceTotal() / lastStock.getVolumeTotal();

            log.debug( priceFirst + " -> " + priceLast );
            log.debug( "" + ( Math.round(10000f * ( priceLast - priceFirst ) / priceFirst ) / 100f ) );
            statStock.setRateChange( Math.round(10000f * ( priceLast - priceFirst ) / priceFirst ) / 100f );
        }

        statStockDao.updateInsertStatStock( statStock );
   }



}



