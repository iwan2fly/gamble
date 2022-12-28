package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.stat.stock.dao.StatIndexDao;
import kr.co.glog.domain.stat.stock.entity.StatIndex;
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
     * 년간 지수 통계
     * @param marketCode
     * @param yyyymmdd
     */
    public void makeStatIndexYear( String marketCode, String yyyymmdd ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        String year = yyyymmdd.substring(0, 4);
        makeStatIndexCommon( marketCode, PeriodCode.year, 0, year + "0101", year + "1231" );
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
        makeStatIndexCommon( marketCode, PeriodCode.month, 0, year + month + "01", year + month + "31" );
    }

    /**
     * 월간 지수 통계
     * @param marketCode
     * @param yyyymmdd
     */
    public void makeStatIndexWeek( String marketCode, String yyyymmdd ) {
        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( yyyymmdd == null ) throw new ParameterMissingException( "yyyymmdd" );

        Integer turn = DateUtil.getWeekOfYear( yyyymmdd );
        String startDate = DateUtil.getFirstDateOfWeek( yyyymmdd );
        String endDate = DateUtil.getLastDateOfWeek( yyyymmdd );

        makeStatIndexCommon( marketCode, PeriodCode.week, turn, startDate, endDate );
    }

    /**
     * 특정 기간 동안의 지수 일반 통계
     * @param startDate
     * @param endDate
     */
    public void makeStatIndexCommon(String marketCode, String periodCode, Integer turn, String startDate, String endDate ) {

        if ( marketCode == null ) throw new ParameterMissingException( "marketCode" );
        if ( startDate == null ) throw new ParameterMissingException( "startDate" );
        if ( endDate == null ) throw new ParameterMissingException( "endDate" );

        StatIndex statIndex = new StatIndex();
        statIndex.setMarketCode( marketCode );
        statIndex.setPeriodCode( periodCode);
        statIndex.setStartDate( startDate );
        statIndex.setEndDate( endDate );

        statIndex.setMonth( 0 );
        statIndex.setYearWeek( startDate.substring(0, 4) + "00" );
        if ( periodCode.equals( PeriodCode.year ) ) {
            statIndex.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
        } else if ( periodCode.equals( PeriodCode.month ) ) {
            statIndex.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statIndex.setMonth( Integer.parseInt( startDate.substring(4, 6) ) );
        } else if ( periodCode.equals( PeriodCode.week ) ) {
            statIndex.setYear( Integer.parseInt( startDate.substring(0, 4) ) );
            statIndex.setMonth( Integer.parseInt( startDate.substring(4, 6) ) );
            statIndex.setYearWeek( DateUtil.getYearWeek( startDate ) );
            statIndex.setWeek( DateUtil.getWeekOfYear( startDate ) );
        }

        // 일반 통계, 최소/최고/평균 등
        IndexDailyResult indexDailyResult = indexDailyDao.getStatIndexCommon( marketCode, startDate, endDate );
        statIndex.setTradeDays( indexDailyResult.getDataCount() );
        statIndex.setPriceLow( indexDailyResult.getPriceLow() );
        statIndex.setPriceHigh( indexDailyResult.getPriceHigh() );
        statIndex.setPriceAverage( indexDailyResult.getPriceAverage() );
        statIndex.setVolumeLow( indexDailyResult.getVolumeLow() );
        statIndex.setVolumeHigh( indexDailyResult.getVolumeHigh() );
        statIndex.setVolumeAverage( indexDailyResult.getVolumeAverage() );

        // 가격 표준편차
        IndexDailyResult priceResult = indexDailyDao.getStatIndexPriceStdDev( marketCode, startDate, endDate, indexDailyResult.getPriceAverage() );
        statIndex.setPriceStandardDeviation( priceResult.getPriceStandardDeviation() );

        // 거래량 표준편차
        IndexDailyResult volumeResult = indexDailyDao.selectStatIndexVolumeStdDev( marketCode, startDate, endDate, indexDailyResult.getVolumeAverage() );
        statIndex.setVolumeStandardDeviation( volumeResult.getVolumeStandardDeviation() );

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
        pagingParam.setSortIndex( "volumeHigh");
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
        indexDailyParam.setEndDate( startDate );
        indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );
        statIndex.setPricePrevious( indexDailyList.get(0).getPriceFinal() );

        // 변동가격 / 변동률
        Float pricePrevious = statIndex.getPricePrevious();
        Float priceNow = statIndex.getPriceFinal();
        statIndex.setPriceChange( statIndex.getPriceFinal() - statIndex.getPricePrevious() );
        statIndex.setRateChange( Math.round( statIndex.getPriceChange() / statIndex.getPricePrevious() * 10000 ) / 100f );      // 소수점 2자리까지만 남김

        statIndexDao.saveStatIndex( statIndex );
   }



}


