/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.daumFinance;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.service.StockService;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.daumFinance.model.DaumDailyStock;
import kr.co.glog.external.daumFinance.model.RankingStock;
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
public class DaumDailyStockScrapper {

    private final StockDailyService stockDailyService;
    private final StockDailyDao stockDailyDao;

    /**
     * 다음 주식에서 특정 종목의 일자별 가격 데이터를 읽어옵니다.
     * @param stockCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getDailyStockPriceDocument( String stockCode, int perPage, int page ) throws ApplicationRuntimeException {
        if ( perPage > 100 ) perPage = 100;     // 페이지당 100개가 한계

        Document document	= null;
        String url	=  "https://finance.daum.net/api/quote/A##stockCode##/days?symbolCode=A##stockCode##&page=##page##&perPage=##perPage##&pagination=true";
        url = url.replaceAll( "##stockCode##", stockCode );
        url = url.replaceAll( "##page##", ""+page );
        url = url.replaceAll( "##perPage##", ""+perPage );
        log.debug( url );

        int tryCount = 0;
        boolean isSuccess = false;
        while ( tryCount < 5 ) {

            try {
                document = Jsoup.connect(url).header("referer", "https://finance.daum.net/domestic/market_cap").ignoreContentType(true).get();
                isSuccess = true;
            } catch (Exception e) {
                tryCount++;
                log.debug("TRY 1 FAIL : RETRY");

                if ( tryCount >= 5 ) {
                    throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            if ( isSuccess ) break;
        }



        return document;
    }

    /**
     * 특정 종목 일자별 가격 데이터 목록의 전체 페이지 개수를 리턴합니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public int getDailyStockPriceTotalPages( String stockCode, int perPage ) throws ApplicationRuntimeException {
        Document document	= null;
        int totalPages = 0;
        try {
            document = getDailyStockPriceDocument( stockCode, perPage, 1);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
            log.debug( jsonObject.get("totalPages").toString() );
            totalPages = Integer.parseInt( jsonObject.get("totalPages").toString() );             // 목록의 전체 페이지
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "특정 종목 일자별 가격 데이터 페이지 개수를 읽는 중 오류가 발생했습니다.");
        }

        return totalPages;
    }


    /**
     * 특정 종목 일자별 가격 데이터 목록을 가져옵니다.
     * @param stockCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<DaumDailyStock> getDailyStockPriceList( String stockCode, int perPage, int page ) throws ApplicationRuntimeException {

        Document document	= null;
        ArrayList<DaumDailyStock> daumDailyStockList = new ArrayList<DaumDailyStock>();

        JSONArray dataArray = null;
        try {
            int tryCount = 0;
            boolean isSuccess = false;
            while ( tryCount < 5 ) {
                try {
                    document = getDailyStockPriceDocument(stockCode, perPage, page);
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(document.text());
                    dataArray = (JSONArray) jsonObject.get("data");        // 현재 페이지의 주식 목록
                    isSuccess = true;
                } catch ( Exception e ) {
                    tryCount++;     // 파싱이 실패할 경우 5번 더 읽어요.
                }

                if ( isSuccess ) break;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            int count = 0;

            for ( int i = 0; i < dataArray.size(); i++ ) {
                DaumDailyStock daumDailyStock = objectMapper.readValue( dataArray.get(i).toString(), DaumDailyStock.class );
                log.debug( count++ + " : " + daumDailyStock.toString() );
                daumDailyStockList.add( daumDailyStock );
            }

        } catch ( ApplicationRuntimeException are ) {
            throw are;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "다음 특정종목 일별 시세 데이터 파싱 중 오류가 발생했습니다.");
        }

        return daumDailyStockList;
    }


    /**
     * 특정 종목의 전체 일별 데이터를 StockDaily 테이블에 인서트
     * @param stockCode
     */
    public void insertDailyStockFullData( String stockCode  ) {

        int perPage = 100;

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        int totalPages = getDailyStockPriceTotalPages( stockCode, perPage );

        log.debug( "totalPages : " + totalPages );

        for ( int page = 1; page <= totalPages; page++ ) {

            ArrayList<DaumDailyStock> daumDailyStockList = getDailyStockPriceList( stockCode, perPage, page );

            for ( DaumDailyStock daumDailyStock : daumDailyStockList ) {
                StockDaily stockDaily = stockDailyService.getStockDailyFromDaumDailyStock( daumDailyStock );
                if ( stockDaily.getStockCode() == null || stockDaily.getStockCode().equals("") ) stockDaily.setStockCode( stockCode );
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
                stockDailyDao.insertStockDaily( stockDaily );
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

        ArrayList<StockDaily> stockDailyList = new ArrayList<StockDaily>();
        ArrayList<DaumDailyStock> daumDailyStockList = getDailyStockPriceList( stockCode, perPage, 1 );

        for ( DaumDailyStock daumDailyStock : daumDailyStockList ) {
            StockDaily stockDaily = stockDailyService.getStockDailyFromDaumDailyStock( daumDailyStock );
            stockDailyList.add( stockDaily );
        }

        for ( StockDaily stockDaily : stockDailyList ) {
            log.debug( stockDaily.toString() );
            try {
                stockDailyDao.saveStockDaily( stockDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {

            }
        }

    }
}
