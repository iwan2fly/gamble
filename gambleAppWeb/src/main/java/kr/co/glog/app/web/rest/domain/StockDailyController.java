package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.domain.stock.model.StockDailyParam;
import kr.co.glog.domain.stock.model.StockDailyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/krx/stockDaily")
public class StockDailyController {

    private final StockDailyService stockDailyService;
    private final StockDailyDao stockDailyDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String stockCode, String tradeDate) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        StockDailyResult stockDailyResult = stockDailyDao.getStockDaily( stockCode, tradeDate );

        restResponse.putData( "result", stockDailyResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, StockDailyParam stockDailyParam, PagingParam pagingParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        stockDailyParam.setPagingParam( pagingParam );
        log.debug( pagingParam.toString() );
        ArrayList<StockDailyResult> stockDailyList = stockDailyDao.getStockDailyList( stockDailyParam );

        restResponse.putData( "list", stockDailyList );
        return restResponse;
    }

    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(HttpServletRequest request, HttpServletResponse response, @RequestBody StockDaily stockDaily ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        StockDaily savedStockDaily = stockDailyDao.insertUpdateStockDaily( stockDaily );

        restResponse.putData( "result", savedStockDaily );
        return restResponse;
    }

    // 데이터 삭제
    /*
    @PostMapping("/delete")
    public RestResponse delete(HttpServletRequest request, HttpServletResponse response, String stockDailyCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        stockDailyDao.deleteStockDaily( stockDailyCode );

        return restResponse;
    }
    */

}
