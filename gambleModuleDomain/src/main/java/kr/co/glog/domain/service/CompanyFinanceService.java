package kr.co.glog.domain.service;


import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.dao.CompanyFinanceDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.domain.stock.model.CompanyFinanceParam;
import kr.co.glog.domain.stock.model.CompanyFinanceResult;
import kr.co.glog.domain.stock.model.CompanyResult;
import kr.co.glog.domain.stock.model.StockResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 *  회사 재무정보 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyFinanceService {

    private final StockDao stockDao;
    private final CompanyFinanceDao companyFinanceDao;

    /**
     *  특정 회사의 특정 년도 사업보고서 조회
     * @param code : companyCode or stockCode
     * @param year
     * @return
     */
    public ArrayList<CompanyFinanceResult> getCompanyFinance(String code, Integer year ) {

        if ( code == null ) throw new ParameterMissingException( "code" );
        if ( year == null ) throw new ParameterMissingException( "year" );

        // 종목코드일 경우 companyCode 조회
        if ( code.length() == 6 ) {
            StockResult stockResult = stockDao.getStock( code );
            code = stockResult.getCompanyCode();
        }

        return getCompanyFinance( code,  year, 5 );      // quarter 5 가 년간 사업보고사
    }

    /**
     *  특정 회사의 특정 분기 사업보고서 조회
     * @param code
     * @param year
     * @param quarter
     * @return
     */
    public ArrayList<CompanyFinanceResult> getCompanyFinance(String code, Integer year, Integer quarter ) {

        if ( code == null ) throw new ParameterMissingException( "code" );
        if ( year == null ) throw new ParameterMissingException( "year" );

        // 종목코드일 경우 companyCode 조회
        if ( code.length() == 6 ) {
            StockResult stockResult = stockDao.getStock( code );
            code = stockResult.getCompanyCode();
        }

        ArrayList<CompanyFinanceResult> companyFinanceList = null;
        CompanyFinanceParam companyFinanceParam = new CompanyFinanceParam();
        companyFinanceParam.setCompanyCode( code );
        companyFinanceParam.setYear( year );
        companyFinanceParam.setQuarter( quarter );
        companyFinanceDao.getCompanyFinanceList( companyFinanceParam );

        return companyFinanceList;
    }


}
