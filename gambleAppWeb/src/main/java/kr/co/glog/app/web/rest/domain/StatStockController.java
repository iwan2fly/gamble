package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.StatStockService;
import kr.co.glog.domain.stat.stock.dao.StatStockDao;
import kr.co.glog.domain.stat.stock.model.StatIndexResult;
import kr.co.glog.domain.stat.stock.model.StatStockParam;
import kr.co.glog.domain.stat.stock.model.StatStockResult;
import kr.co.glog.domain.stock.PeriodCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/krx/statStock")
public class StatStockController {

    private final StatStockService statStockService;
    private final StatStockDao statStockDao;


    // 특정 연간 데이터 리턴
    @GetMapping("/yearly")
    public RestResponse yearly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatStockResult statStockResult = statStockService.getStatStockYear( marketCode, year );
        restResponse.putData( "result", statStockResult );
        return restResponse;
    }

    // 특정 월간 데이터 리턴
    @GetMapping("/monthly")
    public RestResponse monthly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatStockResult statStockResult = statStockService.getStatStockMonth( marketCode, year, month );
        restResponse.putData( "result", statStockResult );
        return restResponse;
    }

    // 특정 주간 데이터 리턴
    @GetMapping("/weekly")
    public RestResponse weekly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, String yearOrder ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatStockResult statStockResult = statStockService.getStatStockWeek( marketCode, yearOrder );
        restResponse.putData( "result", statStockResult );
        return restResponse;
    }


    // 특정 연간 데이터 목록 리턴
    @GetMapping("/yearlyList")
    public RestResponse yearlyList(HttpServletRequest request, HttpServletResponse response, String stockCode, String periodCode, Integer endYear) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockYearlyList( stockCode, endYear );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }


    // 특정 년도 월간 데이터 목록 리턴
    @GetMapping("/monthlyListOfYear")
    public RestResponse monthlyListOfYear(HttpServletRequest request, HttpServletResponse response, String stockCode, String periodCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockMonthlyListOfYear( stockCode, year );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }

    // startMonth ~ endMonth 사이의 월간 데이터 리스트
    @GetMapping("/monthlyList")
    public RestResponse monthlyList(HttpServletRequest request, HttpServletResponse response, String stockCode, String startYearMonth, String endYearMonth ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockMonthlyList( stockCode, startYearMonth, endYearMonth );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }

    // 특정 년도 주간 데이터 목록 리턴
    @GetMapping("/weeklyListOfYear")
    public RestResponse weeklyListOfYear(HttpServletRequest request, HttpServletResponse response, String marketCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockWeeklyListOfYear( marketCode, year );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }

    // 특정 년도 월간 데이터 목록 리턴
    @GetMapping("/weeklyList")
    public RestResponse weeklyList(HttpServletRequest request, HttpServletResponse response, String stockCode, String periodCode, String endYearWeek, Integer rows ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockWeeklyList( stockCode, endYearWeek, rows );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }


    // 특정년도 주식 가격변동률 목록 리턴
    @GetMapping("/rateOfChangePriceList")
    public RestResponse rateOfChangePriceList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month, String yearOrder, PagingParam pagingParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        if ( pagingParam.getRows() == null || pagingParam.getRows() > 100 ) pagingParam.setRows(10);
        pagingParam.setSortIndex( "rateChange" );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( month );
        statStockParam.setYearOrder( yearOrder );

        statStockParam.setPagingParam( pagingParam );

        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }

    // 특정년도 주식 거래량 목록 리턴
    @GetMapping("/volumeTradeList")
    public RestResponse volumeTradeList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month, String yearOrder, PagingParam pagingParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        if ( pagingParam.getRows() == null || pagingParam.getRows() > 100 ) pagingParam.setRows(10);
        pagingParam.setSortIndex( "volumeTrade" );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( month );
        statStockParam.setYearOrder( yearOrder );
        statStockParam.setTradeYn( "Y" );

        statStockParam.setPagingParam( pagingParam );

        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }

    // 특정년도 주식 거래대금 목록 리턴
    @GetMapping("/priceTotalList")
    public RestResponse priceTotalList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month, String yearOrder, PagingParam pagingParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        if ( pagingParam.getRows() == null || pagingParam.getRows() > 100 ) pagingParam.setRows(10);
        pagingParam.setSortIndex( "priceTotal" );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( month );
        statStockParam.setYearOrder( yearOrder );
        statStockParam.setTradeYn( "Y" );

        statStockParam.setPagingParam( pagingParam );

        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );
        restResponse.putData( "list", statStockList );
        return restResponse;
    }

    // 특정기간의 주식 상승/하락 스프레드 목록 리턴
    @GetMapping("/changeSpreadList")
    public RestResponse changeSpreadList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, String yearOrder, Integer rangeSize, PagingParam pagingParam ) throws JsonProcessingException {

        RestResponse restResponse = new RestResponse();

        ArrayList<StatStockResult> statStockList = statStockDao.getRateChangeSpreadList(marketCode, periodCode, yearOrder, rangeSize);
        restResponse.putData("list", statStockList);
        return restResponse;
    }
}
