package kr.co.glog.external.dartApi;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.stock.dao.DartCompanyDao;
import kr.co.glog.domain.stock.entity.DartCompany;
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

    private final DartCompanyDao dartCompanyDao;

    // 기본 URL
    public static String url = "https://opendart.fss.or.kr/api/company.json?crtfc_key=4324bafbe5c2c9436bc1bdc4ccb907adc3c41110&corp_code=##corpCode##";

    /**
     * DART 기업개황 받아서 있으면 업데이트, 없으면 인서트
     */
    public void updateDartCompany( String corpCode ) {
        DartCompany dartCompany = getDartCompany( getCompanyDocument( corpCode ) );
        dartCompany.setCorpCode( corpCode );

        if ( dartCompanyDao.updateDartCompany( dartCompany ) == 0 ) {
            dartCompanyDao.insertDartCompany( dartCompany );
        }
    }

    /**
     * corpCode 로 회사 기본정보 가져옴
     * @param corpCode
     * @return
     */
    public Document getCompanyDocument(String corpCode ) {

        Document document   = null;

        try {
            String replacedUrl = url.replaceAll( "##corpCode##", corpCode );
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
    public DartCompany getDartCompany( Document document ) {

        DartCompany dartCompany = null;

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            String status = jsonObject.get("status").toString();
            String message = jsonObject.get("message").toString();

            if ( status.equals("000") ) {
                dartCompany = new DartCompany();
                dartCompany.setCompanyName(jsonObject.get("corp_name").toString());
                dartCompany.setCompanyNameEng(jsonObject.get("corp_name_eng").toString());
                dartCompany.setStockName(jsonObject.get("stock_name").toString());
                dartCompany.setStockCode(jsonObject.get("stock_code").toString());
                dartCompany.setCeoName(jsonObject.get("ceo_nm").toString());
                dartCompany.setCompanyClass(jsonObject.get("corp_cls").toString());
                dartCompany.setCompanyNo(jsonObject.get("jurir_no").toString());
                dartCompany.setBusinessNo(jsonObject.get("bizr_no").toString().replaceAll("-", ""));
                dartCompany.setAddress(jsonObject.get("adres").toString());
                dartCompany.setHomepage(jsonObject.get("hm_url").toString());
                dartCompany.setIr(jsonObject.get("ir_url").toString());
                dartCompany.setPhone(jsonObject.get("phn_no").toString().replaceAll("-", "").replaceAll(".", "").replaceAll("\\(", "").replaceAll( "\\)", ""));
                dartCompany.setFax(jsonObject.get("fax_no").toString().replaceAll("-", "").replaceAll(".", "").replaceAll("\\(", "").replaceAll( "\\)", ""));
                dartCompany.setIndustryCode(jsonObject.get("induty_code").toString());
                dartCompany.setEstablishDate(jsonObject.get("est_dt").toString());
                dartCompany.setAccountsMonth(jsonObject.get("acc_mt").toString());
            } else {
                throw new ApplicationRuntimeException( "회사 기본정보 파싱 중 오류가 발생했습니다. [" + status + "] " + message );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "회사 기본정보 파싱 중 오류가 발생했습니다.");
        }

        return dartCompany;
    }



}
