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

    /**
     * 다음 주식에서 특정 종목의 일자별 외국인/기관 데이터를 읽어옵니다.
     * @param stockCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getDocument(String stockCode, int perPage, int page ) throws ApplicationRuntimeException {
        if ( perPage > 100 ) perPage = 100;     // 페이지당 100개가 한계

        String header = "A";                    // 일반 주식은 다음에서 A로 시작
        if ( stockCode.charAt(0) == '5' || stockCode.charAt(0) == '6' || stockCode.charAt(0) == '7' ) header = "Q";         // ETN은 Q로 시작하고 500000번대..
        String symbolCode = header + stockCode;

        Document document	= null;
        String url = "https://finance.daum.net/api/investor/days?page=##page##&perPage=##perPage##&symbolCode=##symbolCode##&pagination=true";
        url = url.replaceAll( "##symbolCode##", symbolCode );
        url = url.replaceAll( "##page##", ""+page );
        url = url.replaceAll( "##perPage##", ""+perPage );
        log.debug( url );

        try {
            String referer = "https://finance.daum.net/quotes/" + symbolCode;
            boolean isRead = false;
            int retryCount = 0;
            while ( !isRead ) {
                if ( retryCount >= 3 ) break;
                try {
                    document = Jsoup.connect(url).header("referer", referer ).ignoreContentType(true).get();
                    isRead = true;
                    break;
                } catch ( Exception e ) {
                    e.printStackTrace();
                    retryCount++;
                    Thread.sleep( 3000 );
                }
            }

            if ( !isRead ) {
                throw new ApplicationRuntimeException( "다음 특정 종목의 투자자별 데이터를 읽는 중 오류가 발생했습니다.");
            }

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
            log.debug( jsonObject.get("totalPages").toString() );
            totalPages = Integer.parseInt( jsonObject.get("totalPages").toString() );             // 목록의 전체 페이지
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

        ArrayList<DaumInvestorStock> list = null;
        boolean isRead = false;
        int retryCount = 0;

        while ( !isRead ) {
            if ( retryCount >=3 ) {
                throw new ApplicationRuntimeException( "투자자별월별 데이터를 제대로 읽지 못했습니다.");
            }

            try {
                Document document = getDocument(stockCode, perPage, page);
                list = getDailyInvestorList(document);
                isRead = true;
            } catch (Exception e) {
                retryCount++;
                try {
                    Thread.sleep(3000);
                } catch ( Exception ee ) {}
            }
        }

        return list;

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





}
