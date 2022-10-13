package kr.co.glog.external.datagokr.seibro;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.UnexpectedDataException;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.CompanyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.model.StockParam;
import kr.co.glog.domain.stock.model.StockResult;
import kr.co.glog.external.datagokr.seibro.model.GetStkIsinByShortIsinN1Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetStkIsinByShortIsinN1 {

    private final CompanyDao companyDao;
    private final StockService stockService;
    private final StockDao stockDao;

    // 기본 URL
    public static String url = "http://api.seibro.or.kr/openapi/service/StockSvc/getStkIsinByShortIsinN1?serviceKey=##serviceKey##&shortIsin=##shortIsin##&pageNo=##pageNo##&numOfRows=##numOfRows##";

    public void upsertMarket() throws Exception {

        ArrayList<StockResult> stockList = new ArrayList<StockResult>();

        // 코스피 시장의 종목들에 대해서
        StockParam stockParam = new StockParam();
        stockParam.setMarketTypeCode("kospi");
        stockList = stockDao.getStockList(stockParam );
        for ( StockResult stockResult : stockList ) {

            // ETN / ETF 같은 것들인데, 900000 번 이상은 또 주식같은데
            char firstChar = stockResult.getStockCode().charAt(0);
            if ( firstChar == '5' || firstChar == '6' || firstChar == '7' ) {
                log.debug( "stock code > 500000 : continue");
                continue;
            }

            ArrayList<GetStkIsinByShortIsinN1Result> getShorByMartN1List = getShortnByMartN1List( stockResult.getStockCode(), 1, 10, 3 );
            if ( getShorByMartN1List == null || getShorByMartN1List.size() != 1 ) {
                log.debug( stockResult.getStockCode() + " : size = " + getShorByMartN1List.size() );
                continue;
            }
            GetStkIsinByShortIsinN1Result result = getShorByMartN1List.get(0);

            Stock stock = new Stock();
            stock.setStockCode( stockResult.getStockCode() );
            stock.setIsin( result.getIsin() );
            stock.setIssueDate( result.getIssuDt() );
            stock.setStockTypeCode( result.getSecnKacdNm() );

            stockService.updateInsert( stock );
            Thread.sleep(300);
        }

        stockParam.setMarketTypeCode("kosdaq");
        stockList = stockDao.getStockList(stockParam );
        for ( StockResult stockResult : stockList ) {

            ArrayList<GetStkIsinByShortIsinN1Result> getShorByMartN1List = getShortnByMartN1List( stockResult.getStockCode(), 1, 10, 3 );
            if ( getShorByMartN1List == null || getShorByMartN1List.size() != 1 ) {
                log.debug( stockResult.getStockCode() + " : size = " + getShorByMartN1List.size() );
                continue;
            }
            GetStkIsinByShortIsinN1Result result = getShorByMartN1List.get(0);

            Stock stock = new Stock();
            stock.setStockCode( stockResult.getStockCode() );
            stock.setIsin( result.getIsin() );
            stock.setIssueDate( result.getIssuDt() );
            stock.setStockTypeCode( result.getSecnKacdNm() );

            stockService.updateInsert( stock );
            Thread.sleep(300);
        }

        stockParam.setMarketTypeCode("konex");
        stockList = stockDao.getStockList(stockParam );
        for ( StockResult stockResult : stockList ) {

            ArrayList<GetStkIsinByShortIsinN1Result> getShorByMartN1List = getShortnByMartN1List( stockResult.getStockCode(), 1, 10, 3 );
            if ( getShorByMartN1List == null || getShorByMartN1List.size() != 1 ) {
                log.debug( stockResult.getStockCode() + " : size = " + getShorByMartN1List.size() );
                continue;
            }
            GetStkIsinByShortIsinN1Result result = getShorByMartN1List.get(0);

            Stock stock = new Stock();
            stock.setStockCode( stockResult.getStockCode() );
            stock.setIsin( result.getIsin() );
            stock.setIssueDate( result.getIssuDt() );
            stock.setStockTypeCode( result.getSecnKacdNm() );

            stockService.updateInsert( stock );
            Thread.sleep(300);
        }

    }

    /**
     *  단축번호(종목코드)로 주식종목코드(폴코드) 조회
     * @param shortIsin
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public Document getDocument( String shortIsin, int pageNo, int numOfRows ) {

        Document document   = null;

        try {
            String replacedUrl = url.replaceAll( "##serviceKey##", SeibroKey.SEIBRO_SERVICE_KEY );
            replacedUrl = replacedUrl.replaceAll( "##shortIsin##", shortIsin );
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


    public ArrayList<GetStkIsinByShortIsinN1Result> getShortnByMartN1List(String shortIsin, int pageNo, int numOfRows, int retryCount ) throws Exception {

        ArrayList<GetStkIsinByShortIsinN1Result> list = null;
        int count = 0;
        while ( count < retryCount ) {
            try {
                Document document = getDocument(shortIsin, pageNo, numOfRows);
                list = getShortnByMartN1List(document);
                if ( list != null ) break;
            } catch (Exception e) {
                count++;
                Thread.sleep( 1000 );
            }
        }

        if ( list == null ) {
            throw new UnexpectedDataException( retryCount + "회 시도 실패");
        }

        return list;
    }
    /**
     *  특정 시장, 특정 페이지의 종목코드 목록 조회... numOfRows를 크게 줘서 한번에 조회하자
     * @param shortIsin
     * @param pageNo
     * @param numOfRows
     * @return
     */
    public ArrayList<GetStkIsinByShortIsinN1Result> getShortnByMartN1List(String shortIsin, int pageNo, int numOfRows ) {
        Document document = getDocument( shortIsin, pageNo, numOfRows );
        return getShortnByMartN1List(  document );
    }

    public ArrayList<GetStkIsinByShortIsinN1Result> getShortnByMartN1List(Document document ) {

        ArrayList<GetStkIsinByShortIsinN1Result> itemList = new ArrayList<GetStkIsinByShortIsinN1Result>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );

            try {
                JSONObject response = (JSONObject) jsonObject.get("response");
                jsonObject = (JSONObject) response.get("header");
                String resultCode = (String) jsonObject.get("resultCode");
                if ( resultCode.equals("00") ) {
                    jsonObject = (JSONObject) response.get("body");
                    jsonObject = (JSONObject) jsonObject.get("items");
                    jsonObject = (JSONObject) jsonObject.get("item");

                    GetStkIsinByShortIsinN1Result getStkIsinByShortIsinN1Result = objectMapper.readValue( jsonObject.toJSONString(), GetStkIsinByShortIsinN1Result.class );
                    itemList.add( getStkIsinByShortIsinN1Result );
                }


            } catch ( ClassCastException cce ) {
                log.debug( "ClassCastException PASS");
            }

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "시장별 단축코드 파싱 중 오류가 발생했습니다.");
        }

        return itemList;
    }


}

