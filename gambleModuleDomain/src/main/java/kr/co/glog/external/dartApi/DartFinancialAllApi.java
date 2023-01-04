package kr.co.glog.external.dartApi;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.stock.dao.DartCompanyFinancialInfoDao;
import kr.co.glog.domain.stock.entity.DartCompanyFinancialInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

/**
 *  DART 단일회사 전체 재무제표
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DartFinancialAllApi {

    private final DartCompanyFinancialInfoDao dartCompanyFinancialInfoDao;

    // 기본 URL

    // public static String url = "https://opendart.fss.or.kr/api/fnlttSinglAcntAll.json?crtfc_key=" + DartKey.DART_CRTFC_KRY + "&corp_code=##companyCode##&bsns_year=##bsnsYear##&&reprt_code=##reprtCode##&fs_div=##fsDiv##";
    public static String url = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=" + DartKey.DART_CRTFC_KRY + "&corp_code=##companyCode##&bsns_year=##bsnsYear##&&reprt_code=##reprtCode##&fs_div=##fsDiv##";

    /**
     * DART 단일회사 재무제표 업데이트, 없으면 인서트
     */
    public void updateCompanyFinancialInfo( String companyCode, String year, String reportCode, String fsDiv ) {
        ArrayList<DartCompanyFinancialInfo> dartCompanyFinancialInfoList = getCompanyFinancialInfo( getDocument( companyCode, year, reportCode, fsDiv, 3 ) );
        for ( DartCompanyFinancialInfo dartCompanyFinancialInfo : dartCompanyFinancialInfoList)
            dartCompanyFinancialInfoDao.updateInsetCompanyFinancialInfo(dartCompanyFinancialInfo);
    }

    /**
     * companyCode 로 회사 기본정보 가져옴
     * @param
     *  companyCode : 공시대상회사고유번호
     *  year : 사업연도
     *  reportCode : 보고서 코드
     *  fsDiv : 개별/연결구분
     *
     * @return
     */
    public Document getDocument( String companyCode, String year, String reportCode, String fsDiv ) throws IOException {

        log.debug( companyCode + ":" + year + ":" + reportCode + ":" + fsDiv );
        String replacedUrl = url.replaceAll("##companyCode##", companyCode);
        replacedUrl = replacedUrl.replaceAll("##bsnsYear##", year);
        replacedUrl = replacedUrl.replaceAll("##reprtCode##", reportCode);
        replacedUrl = replacedUrl.replaceAll("##fsDiv##", fsDiv);
        log.debug(replacedUrl);
        Document document = Jsoup.connect(replacedUrl).ignoreContentType(true).get();
        log.debug(document.text());
        return document;
    }

    public Document getDocument( String companyCode, String year, String reportCode, String fsDiv, int readCount ) {

        Document document = null;
        boolean isRead = false;
        int tryCount = 0;
        while ( !isRead ) {
            if ( tryCount >= readCount ) throw new ApplicationRuntimeException( "단일회사 전체 재무제표 URL Fetch 중 오류가 발생했습니다." );

            try {
                document = getDocument( companyCode, year, reportCode, fsDiv );
                isRead = true;
            } catch ( Exception e ) {
                e.printStackTrace();
                tryCount++;
            }
        }

        return document;
    }


    /**
     * Document로부터 DartFinancialInfo 객체 리턴
     * @param document
     * @return
     */
    public ArrayList<DartCompanyFinancialInfo> getCompanyFinancialInfo(Document document ) {

        ArrayList<DartCompanyFinancialInfo> dartCompanyFinancialInfoList = new ArrayList<DartCompanyFinancialInfo>();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            String status = jsonObject.get("status").toString();
            String message = jsonObject.get("message").toString();

            if ( status.equals("000") ) {
                JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                for (int i = 0; i < jsonArray.size(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    DartCompanyFinancialInfo dartCompanyFinancialInfo = new DartCompanyFinancialInfo();
                    dartCompanyFinancialInfo.setReceiptNumber(jsonObject.get("rcept_no") == null ? null : jsonObject.get("rcept_no").toString());
                    dartCompanyFinancialInfo.setCompanyCode(jsonObject.get("corp_code") == null ? null : jsonObject.get("corp_code").toString());
                    dartCompanyFinancialInfo.setReportCode(jsonObject.get("reprt_code") == null ? null : jsonObject.get("reprt_code").toString());
                    dartCompanyFinancialInfo.setYear(jsonObject.get("bsns_year") == null ? null : jsonObject.get("bsns_year").toString());
                    dartCompanyFinancialInfo.setSubjectDiv(jsonObject.get("sj_div") == null ? null : jsonObject.get("sj_div").toString());
                    dartCompanyFinancialInfo.setSubjectName(jsonObject.get("sj_nm") == null ? null : jsonObject.get("sj_nm").toString());
                    dartCompanyFinancialInfo.setAccountId(jsonObject.get("account_id") == null ? null : jsonObject.get("account_id").toString());
                    dartCompanyFinancialInfo.setAccountName(jsonObject.get("account_nm") == null ? null : jsonObject.get("account_nm").toString());
                    dartCompanyFinancialInfo.setAccountDetail(jsonObject.get("account_detail") == null ? null : jsonObject.get("account_detail").toString());
                    dartCompanyFinancialInfo.setKiName(jsonObject.get("thstrm_nm") == null ? null : jsonObject.get("thstrm_nm").toString());
                    dartCompanyFinancialInfo.setKiAmount(jsonObject.get("thstrm_amount") == null ? null : jsonObject.get("thstrm_amount").toString());
                    dartCompanyFinancialInfo.setPrevKiName(jsonObject.get("frmtrm_nm") == null ? null : jsonObject.get("frmtrm_nm").toString());
                    dartCompanyFinancialInfo.setPrevKiAmount(jsonObject.get("frmtrm_amount") == null ? null : jsonObject.get("frmtrm_amount").toString());
                    dartCompanyFinancialInfo.setPrev2KiName(jsonObject.get("bfefrmtrm_nm") == null ? null : jsonObject.get("bfefrmtrm_nm").toString());
                    dartCompanyFinancialInfo.setPrev2KiAmount(jsonObject.get("bfefrmtrm_amount") == null ? null : jsonObject.get("bfefrmtrm_amount").toString());
                    dartCompanyFinancialInfo.setInfoOrder(Integer.parseInt(jsonObject.get("ord") == null ? null : jsonObject.get("ord").toString()));
                    dartCompanyFinancialInfo.setCurrency(jsonObject.get("currency") == null ? null : jsonObject.get("currency").toString());
                    dartCompanyFinancialInfoList.add(dartCompanyFinancialInfo);
                }
            } else if ( status.equals("013") ) {
                // 013 : 조회된 데이터가 없습니다.
            } else {
                throw new ApplicationRuntimeException( "단일회사 전체 재무제표 파싱 중 오류가 발생했습니다. [" + status + "] " + message );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "단일회사 전체 재무제표 중 오류가 발생했습니다.");
        }

        return dartCompanyFinancialInfoList;
    }



}
