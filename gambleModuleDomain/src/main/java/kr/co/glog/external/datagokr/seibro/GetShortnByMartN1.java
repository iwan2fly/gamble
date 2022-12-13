package kr.co.glog.external.datagokr.seibro;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.external.ExternalKey;
import kr.co.glog.external.datagokr.seibro.model.GetShortByMartN1Result;
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
public class GetShortnByMartN1 {

    private final CompanyDao companyDao;
    private final StockService stockService;

    // 기본 URL
    public static String url = "http://api.seibro.or.kr/openapi/service/StockSvc/getShotnByMartN1?serviceKey=##serviceKey##&martTpcd=##martTpcd##&pageNo=##pageNo##&numOfRows=##numOfRows##";

    public void upsertMarket() {

        ArrayList<GetShortByMartN1Result> kospiList = getShorByMartN1List( "11", 1, 2000 );
        for ( GetShortByMartN1Result result : kospiList ) {
            Stock stock = new Stock();
            stock.setStockCode( result.getShotnIsin() );
            stock.setStockName( result.getKorSecnNm() );
            stock.setMarketCode("kospi");

            stockService.updateInsert( stock );
        }

        ArrayList<GetShortByMartN1Result> kosdaqList = getShorByMartN1List( "12", 1, 2000 );
        for ( GetShortByMartN1Result result : kosdaqList ) {
            Stock stock = new Stock();
            stock.setStockCode( result.getShotnIsin() );
            stock.setStockName( result.getKorSecnNm() );
            stock.setMarketCode("kosdaq");

            stockService.updateInsert( stock );
        }


        ArrayList<GetShortByMartN1Result> kotcList = getShorByMartN1List( "13", 1, 2000 );
        for ( GetShortByMartN1Result result : kotcList ) {
            Stock stock = new Stock();
            stock.setStockCode( result.getShotnIsin() );
            stock.setStockName( result.getKorSecnNm() );
            stock.setMarketCode("kotc");

            stockService.updateInsert( stock );
        }

        ArrayList<GetShortByMartN1Result> konexList = getShorByMartN1List( "14", 1, 2000 );
        for ( GetShortByMartN1Result result : konexList ) {
            Stock stock = new Stock();
            stock.setStockCode( result.getShotnIsin() );
            stock.setStockName( result.getKorSecnNm() );
            stock.setMarketCode("konex");

            stockService.updateInsert( stock );
        }

        ArrayList<GetShortByMartN1Result> etcList = getShorByMartN1List( "50", 1, 2000 );
        for ( GetShortByMartN1Result result : etcList ) {
            Stock stock = new Stock();
            stock.setStockCode( result.getShotnIsin() );
            stock.setStockName( result.getKorSecnNm() );
            stock.setMarketCode("etc");

            stockService.updateInsert( stock );
        }

    }

    /**
     *  한국예탁결제원_주식정보서비스 : 시장별 단축코드 전체 조회
     *  시장구분을 기준으로 단축종목번호와 종목명 조회
     * @param martTpcd
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public Document getDocument( String martTpcd, int pageNo, int numOfRows ) {

        Document document   = null;

        try {
            String replacedUrl = url.replaceAll( "##serviceKey##", ExternalKey.DATAGOKR_SERVICE_KEY);
            replacedUrl = replacedUrl.replaceAll( "##martTpcd##", martTpcd );
            replacedUrl = replacedUrl.replaceAll( "##pageNo##", ""+pageNo );
            replacedUrl = replacedUrl.replaceAll( "##numOfRows##", ""+numOfRows );

            log.debug( replacedUrl );
            document = Jsoup.connect(replacedUrl).header("accept", "application/json").ignoreContentType(true).get();
            log.debug( document.text() );

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "시장별 단축코드 전체 조회 중 오류가 발생했습니다.");
        }

        return document;
    }


    /**
     *  특정 시장, 특정 페이지의 종목코드 목록 조회... numOfRows를 크게 줘서 한번에 조회하자
     * @param martTpcd
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public ArrayList<GetShortByMartN1Result> getShorByMartN1List( String martTpcd, int pageNo, int numOfRows ) {
        Document document = getDocument( martTpcd, pageNo, numOfRows );
        return getShorByMartN1List(  document );
    }

    public ArrayList<GetShortByMartN1Result> getShorByMartN1List( Document document ) {

        ArrayList<GetShortByMartN1Result> itemList = new ArrayList<GetShortByMartN1Result>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            jsonObject = (JSONObject)jsonObject.get("response");
            jsonObject = (JSONObject)jsonObject.get("body");
            jsonObject = (JSONObject)jsonObject.get("items");
            JSONArray itemArray = (JSONArray)jsonObject.get("item");

            for ( int i = 0; i < itemArray.size(); i++ ) {
                JSONObject item = (JSONObject)itemArray.get(i);
                GetShortByMartN1Result getShortByMartN1Result = objectMapper.readValue( item.toString(), GetShortByMartN1Result.class );
                itemList.add( getShortByMartN1Result );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "시장별 단축코드 파싱 중 오류가 발생했습니다.");
        }

        return itemList;
    }


}

