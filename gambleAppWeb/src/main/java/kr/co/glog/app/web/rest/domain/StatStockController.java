package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.StatStockService;
import kr.co.glog.domain.stat.stock.dao.StatStockDao;
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
        restResponse.putData( "statStock", statStockResult );
        return restResponse;
    }

    // 특정 월간 데이터 리턴
    @GetMapping("/monthly")
    public RestResponse monthly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatStockResult statStockResult = statStockService.getStatStockMonth( marketCode, year, month );
        restResponse.putData( "statStock", statStockResult );
        return restResponse;
    }

    // 특정 주간 데이터 리턴
    @GetMapping("/weekly")
    public RestResponse weekly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, String yearWeek ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatStockResult statStockResult = statStockService.getStatStockWeek( marketCode, yearWeek );
        restResponse.putData( "statStock", statStockResult );
        return restResponse;
    }


    // 특정 연간 데이터 목록 리턴
    @GetMapping("/yearlyList")
    public RestResponse yearlyList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer endYear) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockYearlyList( marketCode, endYear );
        restResponse.putData( "statStockList", statStockList );
        return restResponse;
    }


    // 특정 년도 월간 데이터 목록 리턴
    @GetMapping("/monthlyList")
    public RestResponse monthlyList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockMonthlyList( marketCode, year );
        restResponse.putData( "statStockList", statStockList );
        return restResponse;
    }

    // 특정 년도 월간 데이터 목록 리턴
    @GetMapping("/weeklyList")
    public RestResponse weeklyList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatStockResult> statStockList = statStockService.getStatStockWeeklyList( marketCode, year );
        restResponse.putData( "statStockList", statStockList );
        return restResponse;
    }


    // 특정년도 주식 가격변동률 목록 리턴
    @GetMapping("/rateOfChangePriceList")
    public RestResponse rateOfChangePriceList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month, String yearWeek, String sortType, Integer dataCount ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        if ( dataCount == null || dataCount > 100 ) dataCount = 10;

        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "rateChange" );
        pagingParam.setSortType( sortType );
        pagingParam.setRows( dataCount );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( month );
        statStockParam.setYearWeek( yearWeek );

        statStockParam.setPagingParam( pagingParam );

        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );
        restResponse.putData( "statStockList", statStockList );
        return restResponse;
    }

    // 특정년도 주식 거래량 목록 리턴
    @GetMapping("/volumeTradeList")
    public RestResponse volumeTradeList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month, String yearWeek, String sortType, Integer dataCount ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        if ( dataCount == null || dataCount > 100 ) dataCount = 10;

        PagingParam pagingParam = new PagingParam();
        pagingParam.setSortIndex( "volumeTrade" );
        pagingParam.setSortType( sortType );
        pagingParam.setRows( dataCount );

        StatStockParam statStockParam = new StatStockParam();
        statStockParam.setMarketCode( marketCode );
        statStockParam.setPeriodCode( periodCode );
        statStockParam.setYear( year );
        statStockParam.setMonth( month );
        statStockParam.setYearWeek( yearWeek );
        statStockParam.setTradeYn( "Y" );

        statStockParam.setPagingParam( pagingParam );

        ArrayList<StatStockResult> statStockList = statStockDao.getStatStockList( statStockParam );
        restResponse.putData( "statStockList", statStockList );
        return restResponse;
    }
}
