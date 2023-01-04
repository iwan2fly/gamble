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
 *  DART 단일회사 주요계정
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DartFinancialApi {

    private final DartCompanyFinancialInfoDao dartCompanyFinancialInfoDao;

    // 기본 URL
    public static String url = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.json?crtfc_key=" + DartKey.DART_CRTFC_KRY + "&corp_code=##companyCode##&bsns_year=##bsnsYear##&&reprt_code=##reprtCode##";

    /**
     * DART 단일회사 재무제표 업데이트, 없으면 인서트
     */
    public void updateCompanyFinancialInfo( String companyCode, String year, String reportCode ) {
        Document document = getDocument( companyCode, year, reportCode, 3 );
        ArrayList<DartCompanyFinancialInfo> dartCompanyFinancialInfoList = getCompanyFinancialInfo( document );
        for ( DartCompanyFinancialInfo dartCompanyFinancialInfo : dartCompanyFinancialInfoList) {
            dartCompanyFinancialInfo.setCompanyCode(companyCode);
            dartCompanyFinancialInfoDao.updateInsetCompanyFinancialInfo(dartCompanyFinancialInfo);
        }
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
    public Document getDocument( String companyCode, String year, String reportCode ) throws IOException {

        log.debug( companyCode + ":" + year + ":" + reportCode );
        String replacedUrl = url.replaceAll("##companyCode##", companyCode);
        replacedUrl = replacedUrl.replaceAll("##bsnsYear##", year);
        replacedUrl = replacedUrl.replaceAll("##reprtCode##", reportCode);
        log.debug(replacedUrl);
        Document document = Jsoup.connect(replacedUrl).ignoreContentType(true).get();
        log.debug(document.text());
        return document;
    }

    public Document getDocument( String companyCode, String year, String reportCode, int readCount ) {

        Document document = null;
        boolean isRead = false;
        int tryCount = 0;
        while ( !isRead ) {
            if ( tryCount >= readCount ) throw new ApplicationRuntimeException( "단일회사 주요계정 URL Fetch 중 오류가 발생했습니다." );

            try {
                document = getDocument( companyCode, year, reportCode );
                isRead = true;
            } catch ( Exception e ) {
                e.printStackTrace();
                tryCount++;
            }
        }

        return document;
    }


    /**
     * Document로부터 ArrayList<CompanyFinancialInfo> 객체 리턴
     * @param document
     * @return
     */
    public ArrayList<DartCompanyFinancialInfo> getCompanyFinancialInfo(Document document ) {

        log.debug( document.toString() );
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
                    dartCompanyFinancialInfo.setStockCode(jsonObject.get("stock_code") == null ? null : jsonObject.get("stock_code").toString());
                    dartCompanyFinancialInfo.setReportCode(jsonObject.get("reprt_code") == null ? null : jsonObject.get("reprt_code").toString());
                    dartCompanyFinancialInfo.setYear(jsonObject.get("bsns_year") == null ? null : jsonObject.get("bsns_year").toString());
                    dartCompanyFinancialInfo.setStockCode(jsonObject.get("stock_code") == null ? null : jsonObject.get("stock_code").toString());
                    dartCompanyFinancialInfo.setSubjectDiv(jsonObject.get("sj_div") == null ? null : jsonObject.get("sj_div").toString());
                    dartCompanyFinancialInfo.setSubjectName(jsonObject.get("sj_nm") == null ? null : jsonObject.get("sj_nm").toString());
                    dartCompanyFinancialInfo.setFsDiv(jsonObject.get("fs_div") == null ? null : jsonObject.get("fs_div").toString());
                    dartCompanyFinancialInfo.setFsName(jsonObject.get("fs_nm") == null ? null : jsonObject.get("fs_nm").toString());
                    dartCompanyFinancialInfo.setAccountName(jsonObject.get("account_nm") == null ? null : jsonObject.get("account_nm").toString());
                    dartCompanyFinancialInfo.setKiName(jsonObject.get("thstrm_nm") == null ? null : jsonObject.get("thstrm_nm").toString());
                    dartCompanyFinancialInfo.setKiDate(jsonObject.get("thstrm_dt") == null ? null : jsonObject.get("thstrm_dt").toString());
                    dartCompanyFinancialInfo.setKiAmount(jsonObject.get("thstrm_amount") == null ? null : jsonObject.get("thstrm_amount").toString());
                    dartCompanyFinancialInfo.setPrevKiName(jsonObject.get("frmtrm_nm") == null ? null : jsonObject.get("frmtrm_nm").toString());
                    dartCompanyFinancialInfo.setPrevKiDate(jsonObject.get("frmtrm_dt") == null ? null : jsonObject.get("frmtrm_dt").toString());
                    dartCompanyFinancialInfo.setPrevKiAmount(jsonObject.get("frmtrm_amount") == null ? null : jsonObject.get("frmtrm_amount").toString());
                    dartCompanyFinancialInfo.setPrev2KiName(jsonObject.get("bfefrmtrm_nm") == null ? null : jsonObject.get("bfefrmtrm_nm").toString());
                    dartCompanyFinancialInfo.setPrev2KiDate(jsonObject.get("bfefrmtrm_dt") == null ? null : jsonObject.get("bfefrmtrm_dt").toString());
                    dartCompanyFinancialInfo.setPrev2KiAmount(jsonObject.get("bfefrmtrm_amount") == null ? null : jsonObject.get("bfefrmtrm_amount").toString());
                    dartCompanyFinancialInfo.setInfoOrder(Integer.parseInt(jsonObject.get("ord") == null ? null : jsonObject.get("ord").toString()));
                    dartCompanyFinancialInfo.setCurrency(jsonObject.get("currency") == null ? null : jsonObject.get("currency").toString());
                    dartCompanyFinancialInfoList.add(dartCompanyFinancialInfo);
                }
            } else if ( status.equals("013") ) {
                // 013 : 조회된 데이터가 없습니다.
            } else {
                throw new ApplicationRuntimeException( "재무정보 파싱 중 오류가 발생했습니다. [" + status + "] " + message );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "재무정보 중 오류가 발생했습니다.");
        }

        return dartCompanyFinancialInfoList;
    }



}
