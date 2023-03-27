package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.PeriodRankStockService;
import kr.co.glog.domain.stat.stock.dao.PeriodRankStockDao;
import kr.co.glog.domain.stat.stock.model.PeriodRankStockParam;
import kr.co.glog.domain.stat.stock.model.PeriodRankStockResult;
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
@RequestMapping("/rest/domain/krx/periodRankStock")
public class PeriodRankStockController {

    private final PeriodRankStockService periodRankStockService;
    private final PeriodRankStockDao periodRankStockDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String stockCode, String periodCode, String yearOrder, String rankCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        PeriodRankStockResult periodRankStockResult = periodRankStockDao.getPeriodRankStock( stockCode, periodCode, yearOrder, rankCode );

        restResponse.putData( "result", periodRankStockResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, PeriodRankStockParam periodRankStockParam, PagingParam pagingParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        periodRankStockParam.setPagingParam( pagingParam );
        ArrayList<PeriodRankStockResult> periodRankStockList = periodRankStockDao.getPeriodRankStockList( periodRankStockParam );

        restResponse.putData( "list", periodRankStockList );
        return restResponse;
    }



}
