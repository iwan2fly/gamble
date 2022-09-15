/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.daumFinance;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
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
public class DaumDailyInvestorScrapper {

    private final StockDailyService stockDailyService;
    private final StockDailyDao stockDailyDao;
    private final StockDao stockDao;

    /**
     * 다음 주식에서 특정 종목의 일자별 외국인/기관 데이터를 읽어옵니다.
     * @param stockCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getDocument(String stockCode, int perPage, int page ) throws ApplicationRuntimeException {
        if ( perPage > 100 ) perPage = 100;     // 페이지당 100개가 한계

        Document document	= null;
        String url	=  "https://finance.daum.net/api/charts/investors/days?symbolCode=##header####stockCode##&page=##page##&perPage=##perPage##";
        url = url.replaceAll( "##stockCode##", stockCode );
        url = url.replaceAll( "##page##", ""+page );
        url = url.replaceAll( "##perPage##", ""+perPage );

        String header = "A";                    // 일반 주식은 다음에서 A로 시작
        if ( stockCode.charAt(0) == '5' || stockCode.charAt(0) == '6' || stockCode.charAt(0) == '7' ) header = "Q";         // ETN은 Q로 시작하고 500000번대..
        url = url.replaceAll( "##header##", ""+header );
        log.debug( url );

        try {
            document = Jsoup.connect(url).header("referer", "https://finance.daum.net/domestic/market_cap").ignoreContentType(true).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "다음 특정 종목의 투자자별 데이터를 읽는 중 오류가 발생했습니다.");
        }

        return document;
    }

    /**
     * 특정 종목 일자별 가격 데이터 목록의 전체 페이지 개수를 리턴합니다.
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public int getTotalPages( Document document ) throws ApplicationRuntimeException {

        if ( document == null ) throw new ApplicationRuntimeException("Document data is null");

        int totalPages = 0;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            log.debug( jsonObject.get("totalPage").toString() );
            totalPages = Integer.parseInt( jsonObject.get("totalPage").toString() );             // 목록의 전체 페이지
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "특정 종목 투자자별 가격 데이터 페이지 개수를 읽는 중 오류가 발생했습니다.");
        }

        return totalPages;
    }

    /**
     * 특정 종목 투자자별 가격 데이터 목록의 전체 페이지 개수를 리턴합니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public int getTotalPages( String stockCode, int perPage ) throws ApplicationRuntimeException {

        Document document = getDocument( stockCode, perPage, 1);
        return  getTotalPages( document );            // 목록의 전체 페이지

    }



    /**
     * 특정 종목 일자별 가격 데이터 목록을 가져옵니다.
     * @param stockCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<DaumInvestorStock> getDailyInvestorList( String stockCode, int perPage, int page ) throws ApplicationRuntimeException {

        Document document	= getDocument(stockCode, perPage, page);;
        return getDailyInvestorList( document );

    }

    public ArrayList<DaumInvestorStock> getDailyInvestorList( Document document ) throws ApplicationRuntimeException {

        ArrayList<DaumInvestorStock> daumInvestorStockList = new ArrayList<DaumInvestorStock>();
        JSONArray dataArray = null;
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(document.text());
            dataArray = (JSONArray) jsonObject.get("data");        // 현재 페이지의 주식 목록

            ObjectMapper objectMapper = new ObjectMapper();
            int count = 0;

            for ( int i = 0; i < dataArray.size(); i++ ) {
                DaumInvestorStock daumInvestorStock = objectMapper.readValue( dataArray.get(i).toString(), DaumInvestorStock.class );
                log.debug( count++ + " : " + daumInvestorStock.toString() );
                daumInvestorStockList.add( daumInvestorStock );
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "Document 파싱 중 오류가 발생했습니다.");
        }

        return daumInvestorStockList;
    }



    /**
     * 특정 종목의 전체 투자자별 데이터를 StockDaily 테이블에 업데이트
     * @param stockCode
     */
    public void updateDailyInvestorFullData( String stockCode  ) {

        int perPage = 100;

        Document document = getDocument( stockCode, perPage, 1 );
        int totalPages = getTotalPages( document );
        log.debug( "totalPages : " + totalPages );

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        for ( int page = 1; page <= totalPages; page++ ) {

            ArrayList<DaumInvestorStock> daumInvestorStockList = getDailyInvestorList( stockCode, perPage, page );

            for ( DaumInvestorStock daumInvestorStock : daumInvestorStockList ) {
                StockDaily stockDaily = stockDailyService.getStockDailyFromDaumInvestorStock( daumInvestorStock );
                stockDaily.setStockCode( stockCode );
                stockDailyList.add( stockDaily );
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for ( StockDaily stockDaily : stockDailyList ) {
            log.debug( stockDaily.toString() );
            try {
                stockDailyDao.updateStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                log.debug( dke.getMessage() );
                break;
            }
        }

    }

    /**
     * 특정 종목의 일별 데이터 첫 페이지를 테이블에 업서트
     * @param stockCode
     */
    public void upsertDailyStock( String stockCode  ) {

        int perPage = 10;

        ArrayList<DaumInvestorStock> daumInvestorStockList = getDailyInvestorList( stockCode, perPage, 1 );

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        for ( DaumInvestorStock daumInvestorStock : daumInvestorStockList ) {
            StockDaily stockDaily = stockDailyService.getStockDailyFromDaumInvestorStock( daumInvestorStock );
            if ( stockDaily.getStockCode() == null ) stockDaily.setStockCode( stockCode );      // 해당 페이지의 데이터에는 stockCode가 없어서 세팅
            stockDailyList.add( stockDaily );
        }

        for ( StockDaily stockDaily : stockDailyList ) {
            log.debug( stockDaily.toString() );
            try {
                stockDailyDao.updateStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {

            }
        }

        // 일간 데이터가 있을 경우, 그 첫 번째 데이터는 가장 최근 데이터임
        // 그 최근 데이터를 stock 에 업데이트
        if ( stockDailyList.size() > 0 ) {
            StockDaily stockDaily = stockDailyList.get(0);
            Stock stock = new Stock();
            stock.setStockCode( stockCode );
            stock.setCurrentPrice( stockDaily.getPriceFinal() );
            stock.setForeignersHoldRate( stockDaily.getForeignerHoldRate() );
            stock.setForeignersStockCount( stockDaily.getForeignerStockCount() );
            stockDao.updateStock( stock );
        }

    }
}
