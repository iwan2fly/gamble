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
import kr.co.glog.external.daumFinance.model.IncludedStock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllStockScrapper {

    private final StockService stockService;
    private final StockDao stockDao;

    /**
     * 다음 주식에서 전종목 시세를 읽어옵니다.
     * @param market
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<IncludedStock> getAllStock(String market ) throws ApplicationRuntimeException {

        Document document	= null;
        ArrayList<IncludedStock> includedStockList = new ArrayList<IncludedStock>();

        String url	=  "https://finance.daum.net/api/quotes/sectors?fieldName=&order=&perPage=&market=##market##&page=&changes=UPPER_LIMIT%2CRISE%2CEVEN%2CFALL%2CLOWER_LIMIT";
        url = url.replaceAll( "##market##", market );
        log.debug( url );

        int		tryCount	= 0;
        while ( tryCount < 5 ) {
            try {
                tryCount++;

                document = Jsoup.connect(url).header("referer", "https://finance.daum.net/domestic/market_cap").ignoreContentType(true).get();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );
                JSONArray dataArray = (JSONArray)jsonObject.get("data");

                ObjectMapper objectMapper = new ObjectMapper();
                int count = 0;

                for ( int i = 0; i < dataArray.size(); i++ ) {
                    jsonObject = (JSONObject)dataArray.get(i);
                    JSONArray stockArray = (JSONArray)jsonObject.get("includedStocks");

                    for ( int j = 0; j < stockArray.size(); j++ ) {
                        IncludedStock includedStock = objectMapper.readValue( stockArray.get(j).toString(), IncludedStock.class );
                        log.debug( count++ + " : " + includedStock.toString() );
                        includedStockList.add( includedStock );
                    }
                }

                break;
            } catch (Exception e) {
                e.printStackTrace();
                log.debug( "TRY " + tryCount + " FAIL" );
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        if ( tryCount == 5 ) {
            throw new ApplicationRuntimeException("다음 전종목 시세 데이터를 가져오지 못했습니다 : " + market );
        }

        return includedStockList;
    }

    // Stock 테이블에 신규 등록
    public void registerStock( String market ) {

        ArrayList<Stock> stockList = new ArrayList<Stock>();
        ArrayList<IncludedStock> includedStockList = getAllStock( market );

        for ( IncludedStock includedStock : includedStockList ) {
            Stock stock = new Stock( includedStock );
            stockList.add( stock );
        }

        for ( Stock stock : stockList ) {
            stock.setMarketCode( market.toLowerCase() );
            log.debug( stock.toString() );
            try {
                stockDao.insertStock(stock);
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {

            }
        }

    }
}
