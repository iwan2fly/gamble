package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stat.stock.dao.StatIndexDao;
import kr.co.glog.domain.stat.stock.entity.StatIndex;
import kr.co.glog.domain.stat.stock.model.StatIndexParam;
import kr.co.glog.domain.stat.stock.model.StatIndexResult;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.PeriodCode;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.model.IndexDailyParam;
import kr.co.glog.domain.stock.model.IndexDailyResult;
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
public class StatIndexService {

    private final StatIndexDao statIndexDao;
    private final IndexDailyDao indexDailyDao;


    /**
     * 특정년도 통계
     * @param marketCode
     * @param year
     * @return
     */
    public StatIndexResult getStatIndexYear(String marketCode, Integer year ) {
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setPeriodCode( PeriodCode.year );
        statIndexParam.setYear( year );
        statIndexParam.setYearWeek( year + "00" );
        ArrayList<StatIndexResult> statIndexList = statIndexDao.getStatIndexList( statIndexParam );

        StatIndexResult statIndexResult = null;
        if ( statIndexList != null && statIndexList.size() == 1 ) statIndexResult = statIndexList.get(0);
        return statIndexResult;
    }

    /**
     * 특정 월 통계
     * @param marketCode
     * @param year
     * @param month
     * @return
     */
    public StatIndexResult getStatIndexMonth(String marketCode, Integer year, Integer month ) {
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setPeriodCode( PeriodCode.month );
        statIndexParam.setYear( year );
        statIndexParam.setMonth( month );
        statIndexParam.setYearWeek( year + "00" );
        ArrayList<StatIndexResult> statIndexList = statIndexDao.getStatIndexList( statIndexParam );

        StatIndexResult statIndexResult = null;
        if ( statIndexList != null && statIndexList.size() == 1 ) statIndexResult = statIndexList.get(0);
        return statIndexResult;
    }

    /**
     * 특정 주차 통계
     * @param marketCode
     * @param yearWeek
     * @return
     */
    public StatIndexResult getStatIndexWeek(String marketCode, String yearWeek ) {
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setPeriodCode( PeriodCode.week );
        statIndexParam.setYear( Integer.parseInt( yearWeek.substring(0,4) ) );
        statIndexParam.setYearWeek( yearWeek );
        ArrayList<StatIndexResult> statIndexList =  statIndexDao.getStatIndexList( statIndexParam );

        StatIndexResult statIndexResult = null;
        if ( statIndexList != null && statIndexList.size() == 1 ) statIndexResult = statIndexList.get(0);
        return statIndexResult;
    }


    /**
     * 전체 연간 데이터 리턴
     * @param marketCode
     * @return
     */
    public ArrayList<StatIndexResult> getStatIndexYearlyList( String marketCode, Integer endYear ) {
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setPeriodCode( PeriodCode.year );
        statIndexParam.setEndYear( endYear );
        log.debug( statIndexParam.toString() );
        return statIndexDao.getStatIndexList( statIndexParam );
    }

    /**
     * 특정 년도의 월간 데이터 리턴
     * @param marketCode
     * @param year
     * @return
     */
    public ArrayList<StatIndexResult> getStatIndexMonthlyList( String marketCode, Integer year ) {
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setYear( year );
        statIndexParam.setPeriodCode( PeriodCode.month );
        statIndexParam.setYearWeek( year + "00" );
        return statIndexDao.getStatIndexList( statIndexParam );
    }

    /**
     * 특정 년도의 주간 데이터 리턴
     * @param marketCode
     * @param year
     * @return
     */
    public ArrayList<StatIndexResult> getStatIndexWeeklyList( String marketCode, Integer year ) {
        StatIndexParam statIndexParam = new StatIndexParam();
        statIndexParam.setMarketCode( marketCode );
        statIndexParam.setYear( year );
        statIndexParam.setPeriodCode( PeriodCode.week );
        return statIndexDao.getStatIndexList( statIndexParam );
    }



    /**
     *  2020년부터 연간/월간/주간 지수통계 upsert
     */
    public void makeStatIndexAll() {
        makeStatIndexYear( MarketCode.kospi, "20200101" );
        makeStatIndexYear( MarketCode.kosdaq, "20200101" );
        makeStatIndexYear( MarketCode.kospi, "20210101" );
        makeStatIndexYear( MarketCode.kosdaq, "20210101" );
        makeStatIndexYear( MarketCode.kospi, "20220101" );
        makeStatIndexYear( MarketCode.kosdaq, "20220101" );
        makeStatIndexYear( MarketCode.kospi, "20230101" );
        makeStatIndexYear( MarketCode.kosdaq, "20230101" );

        for ( int year = 2020; year <= 2023; year++ ) {
            for (int i = 1; i <= 12; i++) {
                String month = i < 10 ? "0" + i : "" + i;
                makeStatIndexMonth(MarketCode.kospi, year+ month + "01");
                makeStatIndexMonth(MarketCode.kosdaq, year + month + "01");
            }
        }

        int date = 20200101;
        while ( date <= 20230113 ) {
            String dateStr = "" + date;
            makeStatIndexWeek(MarketCode.kospi, dateStr);
            makeStatIndexWeek(MarketCode.kosdaq, dateStr);

            dateStr = DateUtil.addDate( dateStr, "D", "yyyyMMdd", 7 );
            date = Integer.parseInt( dateStr );
        }
    }



    /**
     * 년간 지수 통계
     * @param marketCode
     * @param yyyymmdd
     */
    public void makeStatIndexYear( String marketCode, String yyyymmdd ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String year = yyyymmdd.substring(0, 4);
        makeStatIndexCommon( marketCode, PeriodCode.year,year + "0101", year + "1231", null );
    }

    /**
     * 월간 지수 통계
     * @param marketCode
     * @param yyyymmdd
     */
    public void makeStatIndexMonth( String marketCode, String yyyymmdd ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String year = yyyymmdd.substring(0, 4);
        String month = yyyymmdd.substring(4, 6);
        makeStatIndexCommon( marketCode, PeriodCode.month, year + month + "01", year + month + "31", null );
    }

    /**
     * 주간 지수 통계
     * @param marketCode
     * @param yyyymmdd
     */
    public void makeStatIndexWeek( String marketCode, String yyyymmdd ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String yearWeek = DateUtil.getYearWeek( yyyymmdd );
        String startDate = DateUtil.getFirstDateOfWeek( yyyymmdd );
        String endDate = DateUtil.getLastDateOfWeek( yyyymmdd );

        makeStatIndexCommon( marketCode, PeriodCode.week, startDate, endDate, yearWeek );
    }


    /**
     * 특정 기간 동안의 지수 일반 통계
     * @param startDate
     * @param endDate
     */
  //  public void makeStatIndexCommon(String marketCode, String periodCode, String startDate, String endDate ) {
  //      makeStatIndexCommon( marketCode, periodCode, startDate, endDate, null );
   // }

    public void makeStatIndexCommon(String marketCode, String periodCode, String startDate, String endDate, String yearWeek ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        StatIndex statIndex = new StatIndex();
        statIndex.setMarketCode( marketCode );
        statIndex.setPeriodCode( periodCode);
        statIndex.setYearWeek( yearWeek == null ? startDate.substring(0, 4) + "00" : yearWeek );
        statIndex.setWeek( yearWeek == null ? null : Integer.parseInt(yearWeek.substring(4,6) ) );

        statIndex.setMonth( 0 );

        if ( periodCode.equals( PeriodCode.year ) ) {
            statIndex.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
        } else if ( periodCode.equals( PeriodCode.month ) ) {
            statIndex.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statIndex.setMonth( Integer.parseInt( startDate.substring(4, 6) ) );
        } else if ( periodCode.equals( PeriodCode.week ) ) {
            statIndex.setYear( Integer.parseInt( yearWeek.substring(0, 4) ) );
            statIndex.setMonth( Integer.parseInt( yearWeek.substring(4, 6) ) );
            statIndex.setYearWeek( DateUtil.getYearWeek( startDate ) );
            statIndex.setWeek( DateUtil.getWeekOfYear( startDate ) );
        }

        // 일반 통계, 최소/최고/평균 등
        IndexDailyResult indexDailyResult = indexDailyDao.getStatIndexCommon( marketCode, startDate, endDate );

        // 평균값이 산출되지 않으면 표준편차를 구할 수 없음. 데이터가 없는 경우
        if ( indexDailyResult.getPriceAverage() == null ) {
            log.debug("평균값이 없습니다. 데이터가 없는 것으로 추정됩니다.");
            return;
        }

        statIndex.setTradeDays( indexDailyResult.getDataCount() );
        statIndex.setPriceLow( indexDailyResult.getPriceLow() );
        statIndex.setPriceHigh( indexDailyResult.getPriceHigh() );
        statIndex.setPriceAverage( indexDailyResult.getPriceAverage() );
        statIndex.setVolumeLow( indexDailyResult.getVolumeLow() );
        statIndex.setVolumeHigh( indexDailyResult.getVolumeHigh() );
        statIndex.setVolumeAverage( indexDailyResult.getVolumeAverage() );
        statIndex.setPricePrevious( indexDailyResult.getPricePrevious() );
        statIndex.setPriceStart( indexDailyResult.getPriceStart() );
        statIndex.setPriceFinal( indexDailyResult.getPriceFinal() );
        statIndex.setPriceLowDate( indexDailyResult.getPriceLowDate() );
        statIndex.setPriceHighDate( indexDailyResult.getPriceHighDate() );
        statIndex.setVolumeTrade(( indexDailyResult.getVolumeTrade() ) );
        statIndex.setVolumeLowDate( indexDailyResult.getVolumeLowDate() );
        statIndex.setVolumeHighDate( indexDailyResult.getVolumeHighDate() );

        statIndex.setRiseCount( indexDailyResult.getRiseCount() );
        statIndex.setEvenCount( indexDailyResult.getEvenCount() );
        statIndex.setFallCount( indexDailyResult.getFallCount() );

        statIndex.setStartDate( indexDailyResult.getStartDate() );
        statIndex.setEndDate( indexDailyResult.getEndDate() );


        // 가격 표준편차
        IndexDailyResult priceResult = indexDailyDao.getStatIndexPriceStdDev( marketCode, startDate, endDate, indexDailyResult.getPriceAverage() );
        statIndex.setPriceStandardDeviation( priceResult.getPriceStandardDeviation() );

        // 거래량 표준편차
        IndexDailyResult volumeResult = indexDailyDao.selectStatIndexVolumeStdDev( marketCode, startDate, endDate, indexDailyResult.getVolumeAverage() );
        statIndex.setVolumeStandardDeviation( volumeResult.getVolumeStandardDeviation() );

        /*
        // 최저가 일자
        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "priceLow");
        pagingParam.setSortType("asc");
        pagingParam.setRows(1);
        IndexDailyParam indexDailyParam = new IndexDailyParam();
        indexDailyParam.setPagingParam( pagingParam );
        indexDailyParam.setMarketCode( marketCode );
        indexDailyParam.setStartDate( startDate );
        indexDailyParam.setEndDate( endDate );
        ArrayList<IndexDailyResult> indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        statIndex.setPriceLowDate( indexDailyList.get(0).getTradeDate() );

        // 최고가 일자
        pagingParam.setSortIndex( "priceHigh");
        pagingParam.setSortType("desc");
        pagingParam.setRows(1);
        indexDailyParam.setPagingParam( pagingParam );
        indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        statIndex.setPriceHighDate( indexDailyList.get(0).getTradeDate() );

        // 최저거래량 / 최고거래량 일자
        pagingParam.setSortIndex( "volumeTrade");
        pagingParam.setSortType("desc");
        pagingParam.setRows(0);
        indexDailyParam.setPagingParam( pagingParam );
        indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        statIndex.setVolumeHighDate( indexDailyList.get(0).getTradeDate() );
        statIndex.setVolumeLowDate( indexDailyList.get( indexDailyList.size()-1).getTradeDate() );

        // 시가 / 종가
        pagingParam.setSortIndex( "tradeDate");
        pagingParam.setSortType("asc");
        pagingParam.setRows(0);
        indexDailyParam.setPagingParam( pagingParam );
        indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        statIndex.setPriceStart( indexDailyList.get(0).getPriceStart() );
        statIndex.setPriceFinal( indexDailyList.get( indexDailyList.size()-1).getPriceFinal() );

        // 이전 종가 ( 전일 종가 )
        pagingParam.setSortIndex( "tradeDate");
        pagingParam.setSortType("desc");
        pagingParam.setRows(1);
        indexDailyParam.setPagingParam( pagingParam );
        indexDailyParam.setStartDate( null );
        indexDailyParam.setEndDate( null );
        indexDailyParam.setBeforeDate( startDate );
        indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        statIndex.setPricePrevious( indexDailyList.get(0).getPriceFinal() );
        */

        // 변동가격 / 변동률
        Float pricePrevious = statIndex.getPricePrevious();
        Float priceNow = statIndex.getPriceFinal();
        statIndex.setPriceChange( Math.round( 100 * ( statIndex.getPriceFinal() - statIndex.getPricePrevious() ) ) / 100f );     // 소수점 2자리까지만 남김
        statIndex.setRateChange( Math.round( 10000 * statIndex.getPriceChange() / statIndex.getPricePrevious()  ) / 100f );      // 소수점 2자리까지만 남김

        // 상승종목, 하락종목, 보합종목 숫자
        // code required



        statIndexDao.saveStatIndex( statIndex );
   }



}


