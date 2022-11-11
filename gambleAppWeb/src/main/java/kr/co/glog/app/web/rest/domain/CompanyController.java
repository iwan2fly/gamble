package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.entity.Company;
import kr.co.glog.domain.stock.model.CompanyParam;
import kr.co.glog.domain.stock.model.CompanyResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/krx/company")
public class CompanyController {

    private final CompanyDao companyDao;


    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, String companyCode) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        CompanyResult companyResult = companyDao.getCompany( companyCode );

        restResponse.putData( "company", companyResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, CompanyParam companyParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        ArrayList<CompanyResult> companyList = companyDao.getCompanyList( companyParam );

        restResponse.putData( "companyList", companyList );
        return restResponse;
    }

    /*
    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(HttpServletRequest request, HttpServletResponse response, @RequestBody Company company ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        Company savedCompany = companyDao.updateInsertCompany( company );

        restResponse.putData( "company", savedCompany );
        return restResponse;
    }

    // 데이터 삭제
    @PostMapping("/delete")
    public RestResponse delete(HttpServletRequest request, HttpServletResponse response, String companyCode ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        companyDao.deleteCompany( companyCode );

        return restResponse;
    }
    */

}
