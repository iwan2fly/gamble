package kr.co.glog.external.dartApi;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.entity.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

/**
 *  DART 고유번호 다운로드
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DartCompanyApi {

    private final CompanyDao companyDao;

    // 기본 URL
    public static String url = "https://opendart.fss.or.kr/api/company.json?crtfc_key=" + DartKey.DART_CRTFC_KRY + "&corp_code=##companyCode##";

    /**
     * DART 기업개황 받아서 있으면 업데이트, 없으면 인서트
     */
    public void updateCompanyFromDart(String companyCode ) {
        Company company = getCompany( getDocument( companyCode ) );
        company.setCompanyCode( companyCode );
        companyDao.updateInsertCompany( company );
    }

    /**
     * companyCode 로 회사 기본정보 가져옴
     * @param companyCode
     * @return
     */
    public Document getDocument(String companyCode ) {

        Document document   = null;

        try {
            String replacedUrl = url.replaceAll( "##companyCode##", companyCode );
            log.debug( replacedUrl );
            document = Jsoup.connect(replacedUrl).ignoreContentType(true).get();

            log.debug( document.text() );

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "회사 기본정보를 읽는 중 오류가 발생했습니다.");

        }

        return document;
    }


    /**
     * Document로부터 DartCompany 객체 리턴
     * @param document
     * @return
     */
    public Company getCompany(Document document ) {

        Company company = null;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            String status = jsonObject.get("status").toString();
            String message = jsonObject.get("message").toString();

            if ( status.equals("000") ) {
                company = new Company();
                company.setCompanyName(jsonObject.get("corp_name").toString());
                company.setCompanyNameEng(jsonObject.get("corp_name_eng").toString());
                company.setStockName(jsonObject.get("stock_name").toString());
                company.setStockCode(jsonObject.get("stock_code").toString());
                company.setCeoName(jsonObject.get("ceo_nm").toString());
                company.setCompanyClass(jsonObject.get("corp_cls").toString());
                company.setCompanyNo(jsonObject.get("jurir_no").toString());
                company.setBusinessNo(jsonObject.get("bizr_no").toString().replaceAll("-", ""));
                company.setAddress(jsonObject.get("adres").toString());
                company.setHomepage(jsonObject.get("hm_url").toString());
                company.setIr(jsonObject.get("ir_url").toString());
                company.setPhone(jsonObject.get("phn_no").toString().replaceAll("-", "").replaceAll(".", "").replaceAll("\\(", "").replaceAll( "\\)", ""));
                company.setFax(jsonObject.get("fax_no").toString().replaceAll("-", "").replaceAll(".", "").replaceAll("\\(", "").replaceAll( "\\)", ""));
                company.setIndustryCode(jsonObject.get("induty_code").toString());
                company.setEstablishDate(jsonObject.get("est_dt").toString());
                company.setAccountsMonth(jsonObject.get("acc_mt").toString());
            } else {
                throw new ApplicationRuntimeException( "회사 기본정보 파싱 중 오류가 발생했습니다. [" + status + "] " + message );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "회사 기본정보 파싱 중 오류가 발생했습니다.");
        }

        return company;
    }



}
