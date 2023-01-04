package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.StatIndexService;
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


    // 특정 연간 데이터 리턴
    @GetMapping("/yearly")
    public RestResponse yearly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatIndexResult statIndexResult = statIndexService.getStatIndexYear( marketCode, year );
        restResponse.putData( "statIndex", statIndexResult );
        return restResponse;
    }

    // 특정 월간 데이터 리턴
    @GetMapping("/monthly")
    public RestResponse monthly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year, Integer month ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatIndexResult statIndexResult = statIndexService.getStatIndexMonth( marketCode, year, month );
        restResponse.putData( "statIndex", statIndexResult );
        return restResponse;
    }

    // 특정 주간 데이터 리턴
    @GetMapping("/weekly")
    public RestResponse weekly(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, String yearWeek ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        StatIndexResult statIndexResult = statIndexService.getStatIndexWeek( marketCode, yearWeek );
        restResponse.putData( "statIndex", statIndexResult );
        return restResponse;
    }


    // 특정 년도 월간 데이터 목록 리턴
    @GetMapping("/monthlyList")
    public RestResponse monthlyList(HttpServletRequest request, HttpServletResponse response, String marketCode, String periodCode, Integer year ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();
        ArrayList<StatIndexResult> statIndexList = statIndexService.getStatIndexMonthlyList( marketCode, year );
        restResponse.putData( "statIndexList", statIndexList );
        return restResponse;
    }


}
