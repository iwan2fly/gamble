package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.DailyRankStockService;
import kr.co.glog.domain.service.IndexDailyService;
import kr.co.glog.domain.stat.stock.dao.DailyRankStockDao;
import kr.co.glog.domain.stat.stock.model.DailyRankStockParam;
import kr.co.glog.domain.stat.stock.model.DailyRankStockResult;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.domain.stock.model.IndexDailyParam;
import kr.co.glog.domain.stock.model.IndexDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/krx/dailyRankStock")
public class DailyRankStockController {

    private final DailyRankStockService dailyRankStockService;
    private final DailyRankStockDao dailyRankStockDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String stockCode, String rankCode, String tradeDate ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        DailyRankStockResult dailyRankStockResult = dailyRankStockDao.getDailyRankStock( stockCode, tradeDate, rankCode );

        restResponse.putData( "result", dailyRankStockResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, DailyRankStockParam dailyRankStockParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        ArrayList<DailyRankStockResult> dailyRankStockList = dailyRankStockDao.getDailyRankStockList( dailyRankStockParam );

        restResponse.putData( "list", dailyRankStockList );
        return restResponse;
    }



}
