package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/krx/stock")
public class StockController {

    private final StockService stockService;
    private final StockDao stockDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String stockCode) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        StockResult stockResult = stockDao.getStock( stockCode );

        restResponse.putData( "stock", stockResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, StockParam stockParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        ArrayList<StockResult> stockList = stockDao.getStockList( stockParam );

        restResponse.putData( "stockList", stockList );
        return restResponse;
    }

    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(HttpServletRequest request, HttpServletResponse response, @RequestBody Stock stock ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        Stock savedStock = stockDao.saveStock( stock );

        restResponse.putData( "stock", savedStock );
        return restResponse;
    }

    // 데이터 삭제
    /*
    @PostMapping("/delete")
    public RestResponse delete(HttpServletRequest request, HttpServletResponse response, String stockCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        stockDao.deleteStock( stockCode );

        return restResponse;
    }
    */

}
