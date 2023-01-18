package kr.co.glog.external.datagokr.fsc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.common.utils.DateUtil;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
import kr.co.glog.external.ExternalKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetStockPriceInfo {

    private final CompanyDao companyDao;
    private final StockService stockService;

    // 기본 URL
    public static String baseUrl = "http://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?resultType=json&serviceKey=##serviceKey##&mrktCls=##mrktCls##&likeSrtnCd=##likeSrtnCd##&basDt=##basDt##&beginBasDt=##beginBasDt##&endBasDt=##endBasDt##&pageNo=##pageNo##&numOfRows=##numOfRows##";


    /**
     *  금융위원회_주식시세정보 : 주식시세
     *  KRX에 상장된 주식의 시세 정보를 제공
     *  @param mrktCls      특정시장
     *  @param likeSrtnCd       특정종목
     *  @param basDt       특정일자
     *  @param beginBasDt  시작일자
     *  @param endBasDt    종료일자
     *  @param pageNo       페이지 번호
     *  @param numOfRows    페이지당 건수
     * @return
     */
    public Document getDocument( String mrktCls, String likeSrtnCd, String basDt, String beginBasDt, String endBasDt, Integer pageNo, Integer numOfRows ) {

        Document document   = null;

        try {
            String replacedUrl = baseUrl.replaceAll( "##serviceKey##", ExternalKey.DATAGOKR_SERVICE_KEY);
            replacedUrl = replacedUrl.replaceAll( "##mrktCls##", mrktCls == null ? "" : mrktCls );
            replacedUrl = replacedUrl.replaceAll( "##likeSrtnCd##", likeSrtnCd == null ? "" : likeSrtnCd );
            replacedUrl = replacedUrl.replaceAll( "##basDt##", basDt == null ? "" : basDt );
            replacedUrl = replacedUrl.replaceAll( "##beginBasDt##", beginBasDt == null ? "" : beginBasDt );
            replacedUrl = replacedUrl.replaceAll( "##endBasDt##", endBasDt == null ? "" : endBasDt );
            replacedUrl = replacedUrl.replaceAll( "##pageNo##", pageNo == null ? "" : ""+pageNo );
            replacedUrl = replacedUrl.replaceAll( "##numOfRows##", numOfRows  == null ? "" : ""+numOfRows );

            log.debug( replacedUrl );
            document = Jsoup.connect(replacedUrl).header("accept", "application/json").ignoreContentType(true).get();
            log.debug( document.text() );

        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkCommunicationFailureException( "금융위원회_주식시세정보 조회 중 오류가 발생했습니다.");
        }

        return document;
    }

    public ArrayList<GetStockPriceInfoResult> getStockPriceInfoList(Document document ) {

        ArrayList<GetStockPriceInfoResult> itemList = new ArrayList<GetStockPriceInfoResult>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            JSONObject response = (JSONObject)jsonObject.get("response");

            JSONObject header = (JSONObject)response.get("header");
            JSONObject body = (JSONObject)response.get("body");

            JSONObject items = (JSONObject)body.get("items");
            JSONArray itemArray = (JSONArray)items.get("item");

            for ( int i = 0; i < itemArray.size(); i++ ) {
                JSONObject item = (JSONObject)itemArray.get(i);
                GetStockPriceInfoResult getStockPriceInfoResult = objectMapper.readValue( item.toString(), GetStockPriceInfoResult.class );
                itemList.add(getStockPriceInfoResult);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "금융위원회 주식시세정보 파싱 중 오류가 발생했습니다.");
        }

        return itemList;
    }

    /**
     * 특정 주식의 전체 데이터
     * @param stockCode
     * @return
     */
    public ArrayList<GetStockPriceInfoResult> getStockPriceInfo( String stockCode ) throws InterruptedException {

        // 특정 날짜의 전체 종목 데이터 가져옴
        int failCount = 0;
        Document document = null;
        ArrayList<GetStockPriceInfoResult> list = null;
        while ( true ) {
            try {
                document = getDocument( null, stockCode, null, null, DateUtil.getToday(), 1, 10000 );
                break;
            } catch (NetworkCommunicationFailureException ncfe) {
                failCount++;
                Thread.sleep( 5000 );
                if ( failCount >= 3 ) throw ncfe;
            }
        }

        return getStockPriceInfoList( document );
    }

    /**
     * 특정 날짜의 전체 주식 데이터
     * @param date
     * @return
     */
    public ArrayList<GetStockPriceInfoResult> getDatePriceInfo( String date ) throws InterruptedException {

        // 특정 날짜의 전체 종목 데이터 가져옴
        int failCount = 0;
        Document document = null;
        ArrayList<GetStockPriceInfoResult> list = null;
        while ( true ) {
            try {
                document = getDocument( null, null, date, null, null, 1, 10000 );
                break;
            } catch (NetworkCommunicationFailureException ncfe) {
                failCount++;
                Thread.sleep( 5000 );
                if ( failCount >= 3 ) throw ncfe;
            }
        }

        return getStockPriceInfoList( document );
    }


}

