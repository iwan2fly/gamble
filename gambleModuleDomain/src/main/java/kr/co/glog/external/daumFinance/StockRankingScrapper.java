/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.daumFinance;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.external.daumFinance.model.RankingStock;
import kr.co.glog.external.naverStock.model.NaverToron;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockRankingScrapper {

    private final StockService stockService;
    private final StockDao stockDao;

    /**
     * 다음 주식에서 시총순위 목록을 읽어옵니다.
     * @param market
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getDocument(String market, int page ) throws ApplicationRuntimeException {

        Document document	= null;
        String url	=  "https://finance.daum.net/api/trend/market_capitalization?page=##page##&perPage=30&fieldName=marketCap&order=desc&market=##market##&pagination=true";
        url = url.replaceAll( "##market##", market );
        url = url.replaceAll( "##page##", ""+page );
        log.debug( url );

        try {
            document = Jsoup.connect(url).header("referer", "https://finance.daum.net/domestic/market_cap").ignoreContentType(true).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "다음 시총순위 데이터를 읽는 중 오류가 발생했습니다.");
        }

        return document;
    }

    /**
     * 네이버 종목토론실 전체 페이지 개수를 리턴합니다.
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
            log.debug( jsonObject.get("totalPages").toString() );
            totalPages =  Integer.parseInt( jsonObject.get("totalPages").toString() );             // 목록의 전체 페이지
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "다음 시총순위 데이터 분석 중 오류가 발생했습니다.");
        }

        return totalPages;
    }

    /**
     * 시총상위종목 목록의 전체 페이지 개수를 리턴합니다.
     * @param market
     * @return
     * @throws ApplicationRuntimeException
     */
    public int getTotalPages( String market ) throws ApplicationRuntimeException {

        Document document	= getDocument( market, 1);;
        return  getTotalPages( document );

    }


    /**
     * Document 로부터 시총상위 목록을 리턴
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<RankingStock> getRankingStockList( Document document ) throws ApplicationRuntimeException {

        ArrayList<RankingStock> rankingStockList = new ArrayList<RankingStock>();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(document.text());
            JSONArray dataArray = (JSONArray) jsonObject.get("data");        // 현재 페이지의 주식 목록

            ObjectMapper objectMapper = new ObjectMapper();
            int count = 0;

            for (int i = 0; i < dataArray.size(); i++) {
                RankingStock rankingStock = objectMapper.readValue(dataArray.get(i).toString(), RankingStock.class);
                log.debug(count++ + " : " + rankingStock.toString());
                rankingStockList.add(rankingStock);
            }
        } catch ( Exception e ) {
            throw new ApplicationRuntimeException( "시가총액상위 종목 문서 파싱 중 오류가 발생했습니다.");
        }

        return rankingStockList;
    }

    /**
     * 특정 시장 & 특정 페이지의 시총상위 목록을 리턴합니다.
     * @param market
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<RankingStock> getRankingStockList(String market, int page ) throws ApplicationRuntimeException {

        Document document	= getDocument(market, page);;
        return  getRankingStockList( document );

    }


    /**
     * 새로 생긴 데이터만 stock 테이블에 insert
     * @param market
     */
    public void registerStock( String market ) {

        ArrayList<Stock> stockList = new ArrayList<Stock>();
        int totalPages = getTotalPages( market );

        for ( int page = 1; page <= totalPages; page++ ) {
            ArrayList<RankingStock> rankingStockList = getRankingStockList( market, page );

            for ( RankingStock rankingStock : rankingStockList ) {
                Stock stock = stockService.getStockFromRankingStock( rankingStock );
                stockList.add( stock );
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for ( Stock stock : stockList ) {
            stock.setMarketCode( market.toLowerCase() );
            log.debug( stock.toString() );
            try {
                stockDao.insertStock(stock);
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                log.debug( stock.getStockName() + "[" + stock.getStockCode() + "] Already Registered" );
            }
        }

    }
}
