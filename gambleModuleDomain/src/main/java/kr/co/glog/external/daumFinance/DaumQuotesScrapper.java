/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.daumFinance;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.utils.StockUtil;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.external.daumFinance.model.DaumIndex;
import kr.co.glog.external.daumFinance.model.DaumQuotes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DaumQuotesScrapper {

    private final StockDao stockDao;

    /**
     * 다음 주식에서 특정 주식의 기본주가정보를 읽어옵니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getDocument(String stockCode ) throws ApplicationRuntimeException {

        String url = "https://finance.daum.net/api/quotes/" + StockUtil.toStockCode7( stockCode ) + "?summary=false&changeStatistics=true";
        String referer = "https://finance.daum.net/quotes/" + StockUtil.toStockCode7( stockCode );
        Document document	= null;

        try {
            log.debug( url );
            document = Jsoup.connect(url)
                        .header("referer", referer )
                        .header( "user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Whale/3.18.154.13 Safari/537.36" )
                        .header( "cookie", "webid=fb979f677d3b4bfb99dba836348c5c4a; webid_ts=1663742112242; KAKAO_STOCK_CHART_ENABLED_INDICATORS=[%22sma%22%2C%22column%22]; TIARA=a5tY436wfQWU.eG-BsildW-DlSqVv7QxJb.mcW8i_RyUTFaSoLHNVroiCK..KwKb3Tt9CQl6mAEHCc4cWlqQaLnJ9Z4N_XiJ9BowHbRTtn90; _ga=GA1.2.1873757000.1674000883; __T_=1; _gid=GA1.2.1258512532.1674694088; KAKAO_STOCK_RECENT=[%22A049960%22%2C%22Q610045%22%2C%22A007460%22%2C%22A000020%22%2C%22A377300%22%2C%22Q570046%22%2C%22A010145%22%2C%22A131400%22%2C%22A025890%22%2C%22A000660%22%2C%22A005930%22]; _gat_gtag_UA_128578811_1=1; webid_sync=1674778648401; _dfs=aWpFVkl2aldiMktZVU5hVXYxUzVzSTZjOVBuak5yMWdyQ1J4blZoaVNESzZNUjNDems3VXlmelEyN3JQNlcvcVNOODhGbHdudGRKWDUvZGNuYm9YNVE9PS0tTEx1WW9Ncm5xZEhmUU1NRFhsdFBmZz09--1619ba38ff32374333c0aa4cb0b82f953ec64eca; _T_ANO=UwGsRxB10XGHLRt1BW5UeClxoL0aJScyGJQTbgjbMMrKX/l33PaPqqjfS/VTNyWkY9L0EsnJti9d03Xhml98/U4/CL2IwcEEGSJ/LnkeOt0HmGfBegsc/BvZUCL/V7a9aQUG7pjM5LDUx7NnmwGqOxl/WkZmn+iXgFOIQVeWwxOn/WQKS7uQsY37oHaxdetHOmnNKpVZEXwLQDr9izNOf64AhXvCgWoGsfHp+s3MojBX4O6m9q6nv03tKQDAZ9oZyBEZtkIJafEI+NKKSY6Lvj2UIHMmy18kAoyrRJC/EfrSGyNB7w/jUlonh8nHYGWfl1TGvyqz0roZZV3FDTP4xg==")
                        .ignoreContentType(true).get();
            log.debug( document.toString() );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "당일 상세정보를 읽는 중 오류가 발생했습니다.");
        }

        return document;
    }


    /**
     *  marketCode 로 주식 상세정보 리턴
     * @param marketCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public DaumQuotes get( String marketCode ) throws ApplicationRuntimeException {
        return get( getDocument( marketCode ) );
    }

    /**
     * Document 로부터 주식 상세정보 리턴
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public DaumQuotes get( Document document ) throws ApplicationRuntimeException {

        DaumQuotes daumQuotes = new DaumQuotes();
        String text             = null;
        try {
            JSONParser jsonParser = new JSONParser();
            text = document.getElementsByTag("body").text();
            log.debug( text );

            if ( text.contains("All rights reserved") ) return null;         // 상폐 주식의 경우 정보가 없고, 에러페이지가 나타남.. 그냥 null 리턴

            JSONObject jsonObject = (JSONObject)jsonParser.parse( text );

            int cnt = 1;
            /*
            log.debug( jsonObject.get("change").toString() );
            log.debug( jsonObject.get("accTradePrice").toString() );
            log.debug( jsonObject.get("accTradeVolume").toString() );
            log.debug( jsonObject.get("basePrice").toString() );
            log.debug( jsonObject.get("openingPrice").toString() );

            log.debug( jsonObject.get("highPrice").toString() );
            log.debug( jsonObject.get("lowPrice").toString() );
            log.debug( jsonObject.get("tradePrice").toString() );
            log.debug( jsonObject.get("tradeDate").toString() );
            log.debug( jsonObject.get("changePrice").toString() );
            log.debug( jsonObject.get("changeRate").toString() );
            log.debug( jsonObject.get("prevAccTradeVolume").toString() );
            log.debug( jsonObject.get("prevClosingPrice").toString() );
            log.debug( jsonObject.get("foreignOwnShares").toString() );
            log.debug( jsonObject.get("foreignRatio").toString() );
            log.debug( jsonObject.get("listedShareCount").toString() );
            log.debug( jsonObject.get("marketCap").toString() );
            log.debug( jsonObject.get("market").toString() );
            log.debug( jsonObject.get("name").toString() );
            log.debug( jsonObject.get("code").toString() );
            log.debug( jsonObject.get("companySummary").toString() );
            log.debug( ""+jsonObject.get("debtRatio") );
            log.debug( jsonObject.get("symbolCode").toString() );
            log.debug( jsonObject.get("wicsSectorCode").toString() );
            log.debug( jsonObject.get("wicsSectorName").toString() );
             */

            if ( jsonObject.get("change") != null ) daumQuotes.setChange( jsonObject.get("change").toString() );                     // RISE = 상승, FAll = 하락 ?
            if ( jsonObject.get("accTradePrice") != null ) daumQuotes.setAccTradePrice( Long.parseLong( jsonObject.get("accTradePrice").toString() ) );        // 거래대금(백만)
            if ( jsonObject.get("accTradeVolume") != null ) daumQuotes.setAccTradeVolume( Long.parseLong( jsonObject.get("accTradeVolume").toString() ) );      // 거래량(천주)
            if ( jsonObject.get("basePrice") != null ) daumQuotes.setBasePrice( (int)Float.parseFloat( jsonObject.get("basePrice").toString() ) );                // 기준가
            if ( jsonObject.get("openingPrice") != null ) daumQuotes.setOpeningPrice( (int)Float.parseFloat( jsonObject.get("openingPrice").toString() ) );          // 시가
            if ( jsonObject.get("highPrice") != null ) daumQuotes.setHighPrice( (int)Float.parseFloat( jsonObject.get("highPrice").toString() ) );                // 고가
            if ( jsonObject.get("lowPrice") != null ) daumQuotes.setLowPrice( (int)Float.parseFloat( jsonObject.get("lowPrice").toString() ) );                  // 저가
            if ( jsonObject.get("tradePrice") != null ) daumQuotes.setTradePrice( (int)Float.parseFloat( jsonObject.get("tradePrice").toString() ) );              // 종가
            if ( jsonObject.get("tradeDate") != null ) daumQuotes.setTradeDate( jsonObject.get("tradeDate").toString() );                // 20230109
            if ( jsonObject.get("changePrice") != null ) daumQuotes.setChangePrice( (int)Float.parseFloat( jsonObject.get("changePrice").toString() ) );            // 변동 지수
            if ( jsonObject.get("changeRate") != null ) daumQuotes.setChangeRate( (int)( Float.parseFloat( jsonObject.get("changeRate").toString() ) * 10000 ) / 100f );              // 변동률
            if ( jsonObject.get("prevAccTradeVolume") != null ) daumQuotes.setPrevAccTradeVolume( Long.parseLong( jsonObject.get("prevAccTradeVolume").toString() ) );        // 전일거래량
            if ( jsonObject.get("prevClosingPrice") != null ) daumQuotes.setPrevClosingPrice( (int)Float.parseFloat( jsonObject.get("prevClosingPrice").toString() ) );        // 전일종가
            if ( jsonObject.get("foreignOwnShares") != null ) daumQuotes.setForeignOwnShares(Long.parseLong(jsonObject.get("foreignOwnShares").toString()));        // 외국인보유량
            if ( jsonObject.get("foreignRatio") != null ) daumQuotes.setForeignRatio(Float.parseFloat(jsonObject.get("foreignRatio").toString()));        // 외국인보유율
            if ( jsonObject.get("listedShareCount") != null ) daumQuotes.setListedShareCount(Long.parseLong(jsonObject.get("listedShareCount").toString()));        // 상장주식수
            if ( jsonObject.get("marketCap") != null ) daumQuotes.setMarketCap((long)Float.parseFloat(jsonObject.get("marketCap").toString()));        // 시가총액
            if ( jsonObject.get("market") != null ) daumQuotes.setMarket( jsonObject.get("market").toString() );        // 상장일     ( 2022-12-13 )
            if ( jsonObject.get("name") != null ) daumQuotes.setName( jsonObject.get("name").toString() );        // 시장 ( KOSDAQ )
            if ( jsonObject.get("code") != null ) daumQuotes.setCode( jsonObject.get("code").toString() );        // 이름
            if ( jsonObject.get("companySummary") != null ) daumQuotes.setCompanySummary( jsonObject.get("companySummary").toString() );        // KR7049960008
            if ( jsonObject.get("debtRatio") != null ) daumQuotes.setDebtRate(Float.parseFloat(jsonObject.get("debtRatio").toString()));        // 회사요약
            if ( jsonObject.get("symbolCode") != null ) daumQuotes.setSymbolCode( jsonObject.get("symbolCode").toString() );        // ( A049960 )
            if ( jsonObject.get("wicsSectorCode") != null ) daumQuotes.setWicsSectorCode( jsonObject.get("wicsSectorCode").toString() );        // G352010
            if ( jsonObject.get("wicsSectorName") != null ) daumQuotes.setWicsSectorName( jsonObject.get("wicsSectorName").toString() );        // 생물공학

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "당일 데이터 문서 파싱 중 오류가 발생했습니다.");
        }

        return daumQuotes;
    }


}

