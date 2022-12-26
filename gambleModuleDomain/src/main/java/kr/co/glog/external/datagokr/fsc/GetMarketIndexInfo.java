package kr.co.glog.external.datagokr.fsc;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.NetworkCommunicationFailureException;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.external.ExternalKey;
import kr.co.glog.external.datagokr.fsc.model.GetMarketIndexInfoResult;
import kr.co.glog.external.datagokr.fsc.model.GetStockPriceInfoResult;
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
public class GetMarketIndexInfo {

    private final CompanyDao companyDao;
    private final StockService stockService;

    // 기본 URL
    public static String baseUrl = "http://apis.data.go.kr/1160100/service/GetMarketIndexInfoService/getStockMarketIndex?resultType=json&serviceKey=##serviceKey##&idxNm=##idxNm##&basDt=##basDt##&beginBasDt=##beginBasDt##&endBasDt=##endBasDt##&pageNo=##pageNo##&numOfRows=##numOfRows##";


    /**
     *  금융위원회_지수시세정보 : 지수시세
     *  KRX에 상장된 지수의 시세 정보를 제공
     *  @param idxNm      지수명
     *  @param basDt       특정일자
     *  @param beginBasDt  시작일자
     *  @param endBasDt    종료일자
     *  @param pageNo       페이지 번호
     *  @param numOfRows    페이지당 건수
     * @return
     */
    public Document getDocument( String idxNm, String basDt, String beginBasDt, String endBasDt, Integer pageNo, Integer numOfRows ) {

        Document document   = null;

        try {
            String replacedUrl = baseUrl.replaceAll( "##serviceKey##", ExternalKey.DATAGOKR_SERVICE_KEY);
            replacedUrl = replacedUrl.replaceAll( "##idxNm##", idxNm == null ? "" : idxNm );
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
            throw new NetworkCommunicationFailureException( "금융위원회_지수시세정보 조회 중 오류가 발생했습니다.");
        }

        return document;
    }

    public ArrayList<GetMarketIndexInfoResult> getMarketIndexInfoList(Document document ) {

        ArrayList<GetMarketIndexInfoResult> itemList = new ArrayList<GetMarketIndexInfoResult>();
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
                GetMarketIndexInfoResult getMarketIndexInfoResult = objectMapper.readValue( item.toString(), GetMarketIndexInfoResult.class );
                itemList.add(getMarketIndexInfoResult);
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "금융위원회 지수시세정보 파싱 중 오류가 발생했습니다.");
        }

        return itemList;
    }


}

