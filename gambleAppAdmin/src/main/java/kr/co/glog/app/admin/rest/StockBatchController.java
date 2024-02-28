package kr.co.glog.app.admin.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/rest/stock/batch")
@RequiredArgsConstructor
public class StockBatchController {

    @GetMapping("/upateCurrentPrice/{stockCode}")
    public RestResponse updateCurrentPrice(HttpServletRequest request, HttpServletResponse response, @PathVariable("stockCode") String stockCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();


        return restResponse;
    }


}
