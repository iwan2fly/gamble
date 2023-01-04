package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.service.IndexDailyService;
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
@RequestMapping("/rest/domain/krx/indexDaily")
public class IndexDailyController {

    private final IndexDailyService indexDailyService;
    private final IndexDailyDao indexDailyDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String stockCode, String tradeDate) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        IndexDailyResult indexDailyResult = indexDailyDao.getIndexDaily( stockCode, tradeDate );

        restResponse.putData( "indexDaily", indexDailyResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, IndexDailyParam indexDailyParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        ArrayList<IndexDailyResult> indexDailyList = indexDailyDao.getIndexDailyList( indexDailyParam );

        restResponse.putData( "indexDailyList", indexDailyList );
        return restResponse;
    }

    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(HttpServletRequest request, HttpServletResponse response, @RequestBody IndexDaily indexDaily ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        IndexDaily savedIndexDaily = indexDailyDao.saveIndexDaily( indexDaily );

        restResponse.putData( "indexDaily", savedIndexDaily );
        return restResponse;
    }



}
