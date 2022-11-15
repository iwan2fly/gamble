package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.stock.dao.CompanyFinanceDao;
import kr.co.glog.domain.stock.model.CompanyFinanceParam;
import kr.co.glog.domain.stock.model.CompanyFinanceResult;
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
@RequestMapping("/rest/domain/krx/companyFinance")
public class CompanyFinanceController {

    private final CompanyFinanceDao companyFinanceDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String companyCode, int year, int quarter, String subject, String account ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        CompanyFinanceResult companyFinanceResult = companyFinanceDao.getCompanyFinance( companyCode, year, quarter, subject, account );

        restResponse.putData( "companyFinance", companyFinanceResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, CompanyFinanceParam companyFinanceParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        ArrayList<CompanyFinanceResult> companyFinanceList = companyFinanceDao.getCompanyFinanceList( companyFinanceParam );

        restResponse.putData( "companyFinanceList", companyFinanceList );
        return restResponse;
    }

    /*
    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(HttpServletRequest request, HttpServletResponse response, @RequestBody CompanyFinance companyFinance ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        CompanyFinance savedCompanyFinance = companyFinanceDao.updateInsertCompanyFinance( companyFinance );

        restResponse.putData( "companyFinance", savedCompanyFinance );
        return restResponse;
    }

    // 데이터 삭제
    @PostMapping("/delete")
    public RestResponse delete(HttpServletRequest request, HttpServletResponse response, String companyFinanceCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        companyFinanceDao.deleteCompanyFinance( companyFinanceCode );

        return restResponse;
    }
    */

}
