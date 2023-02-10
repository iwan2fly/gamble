package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.StatIndexService;
import kr.co.glog.domain.stat.stock.dao.StatStockDao;
import kr.co.glog.domain.stat.stock.model.StatIndexResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/krx/statIndex")
public class StatIndexController {

    private final StatIndexService statIndexService;
    private final StatStockDao statStockDao;


    // 특정연도 지수 데이터 리턴
    @GetMapping("/year")
    public RestResponse year(HttpServletRequest request, HttpServletResponse response, String marketCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatIndexResult statIndexResult = statIndexService.getStatIndexYear( marketCode, year );
        restResponse.putData( "result", statIndexResult );
        return restResponse;
    }

    // 특정월 지수 데이터 리턴
    @GetMapping("/month")
    public RestResponse month(HttpServletRequest request, HttpServletResponse response, String marketCode, Integer year, Integer month ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatIndexResult statIndexResult = statIndexService.getStatIndexMonth( marketCode, year, month );
        restResponse.putData( "result", statIndexResult );
        return restResponse;
    }

    // 특정주 지수 데이터 리턴
    @GetMapping("/week")
    public RestResponse week(HttpServletRequest request, HttpServletResponse response, String marketCode, String yearWeek ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatIndexResult statIndexResult = statIndexService.getStatIndexWeek( marketCode, yearWeek );
        restResponse.putData( "result", statIndexResult );
        return restResponse;
    }


    // 특정년도 이전의 연간 지수 데이터 목록 리턴
    @GetMapping("/yearlyList")
    public RestResponse yearlyListOfYear(HttpServletRequest request, HttpServletResponse response, String marketCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatIndexResult> statIndexList = statIndexService.getStatIndexYearlyList( marketCode );
        restResponse.putData( "list", statIndexList );
        return restResponse;
    }


    // 특정 년도 월간 데이터 목록 리턴
    @GetMapping("/monthlyListOfYear")
    public RestResponse monthlyListOfYear(HttpServletRequest request, HttpServletResponse response, String marketCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatIndexResult> statIndexList = statIndexService.getStatIndexMonthlyListOfYear( marketCode, year );
        restResponse.putData( "list", statIndexList );
        return restResponse;
    }

    // startMonth ~ endMonth 사이의 월간 데이터 리스트
    @GetMapping("/monthlyList")
    public RestResponse monthlyList(HttpServletRequest request, HttpServletResponse response, String marketCode, String startYearMonth, String endYearMonth ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatIndexResult> statIndexList = statIndexService.getStatIndexMonthlyList( marketCode, startYearMonth, endYearMonth );
        restResponse.putData( "list", statIndexList );
        return restResponse;
    }

    // 특정 년도 주간 데이터 목록 리턴
    @GetMapping("/weeklyListOfYear")
    public RestResponse weeklyListOfYear(HttpServletRequest request, HttpServletResponse response, String marketCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatIndexResult> statIndexList = statIndexService.getStatIndexWeeklyListOfYear( marketCode, year );
        restResponse.putData( "list", statIndexList );
        return restResponse;
    }

    // startMonth ~ endMonth 사이의 월간 데이터 리스트
    @GetMapping("/weeklyList")
    public RestResponse weeklyList(HttpServletRequest request, HttpServletResponse response, String marketCode, String endYearWeek, Integer rows ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatIndexResult> statIndexList = statIndexService.getStatIndexWeeklyList( marketCode, endYearWeek, rows );
        restResponse.putData( "list", statIndexList );
        return restResponse;
    }




}
