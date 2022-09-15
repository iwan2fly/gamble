/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.naverStock;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.service.StockDailyService;
import kr.co.glog.domain.stock.dao.StockDailyDao;
import kr.co.glog.domain.stock.entity.StockDaily;
import kr.co.glog.external.daumFinance.model.DaumInvestorStock;
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
public class NaverToronScrapper {

    private final StockDailyService stockDailyService;
    private final StockDailyDao stockDailyDao;

    /**
     * 네이버 주식에서 특정 종목의 토론 데이터를 읽어옵니다. 페이지당 20개 고정, HTML
     * @param stockCode
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getToronDocument( String stockCode, int page ) throws ApplicationRuntimeException {

        Document document	= null;
        String url	=  "https://finance.naver.com/item/board.naver?code=##stockCode##&page=##page##";
        url = url.replaceAll( "##stockCode##", stockCode );
        url = url.replaceAll( "##page##", ""+page );
        log.debug( url );

        try {
            document = Jsoup.connect(url).ignoreContentType(true).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");
        }

        return document;
    }

    /**
     * 네이버 종목토론실 전체 페이지 개수를 리턴합니다.
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public int getToronTotalPages( Document document ) throws ApplicationRuntimeException {
        int totalPages = 0;
        try {
            Elements elements = document.getElementsByClass("Nnavi");       // 종목토론실 페이지 네비게이션, 1개만 존재
            if ( elements == null || elements.size() != 1 ) {
                throw new ApplicationRuntimeException( "종목 토론실 페이지 네비게이션 정보가 없거나 해당 클래스가 2개 이상입니다.");
            }

            Elements pgRR = elements.select(".pgRR");                       // 마지막 페이지
            if ( pgRR == null || pgRR.size() != 1 ) {
                throw new ApplicationRuntimeException( "종목 토론실 마지막 페이지 정보가 없거나 해당 클래스가 2개 이상입니다.");
            }

            Elements a = pgRR.select("a");                                  // 마지막 페이지 링크
            if ( a == null || a.size() != 1 ) {
                throw new ApplicationRuntimeException( "종목 토론실 마지막 페이지 링크가 없거나 해당 클래스가 2개 이상입니다.");
            }

            String url = a.attr("href");
            String [] urlSplit = url.split("&");
            String page = null;
            for ( String pageStr : urlSplit ) {
                if ( pageStr.startsWith( "page") ) {
                    String [] pageSplit = pageStr.split("=");
                    page = pageSplit[1];
                }
            }

            totalPages = Integer.parseInt( page );             // 목록의 전체 페이지
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "종목 토론실 전체 페이지 정보 분석 중 오류가 발생했습니다.");
        }

        return totalPages;
    }

    /**
     * Document 로부터 네이버 토론 목록을 리턴
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<NaverToron> getDailyInvestorList( Document document ) throws ApplicationRuntimeException {

        ArrayList<NaverToron> naverToronList = new ArrayList<NaverToron>();
        try {
            Elements elements = document.getElementsByClass("table > .type2");       // 종목토론실 페이지 네비게이션, 1개만 존재
            if ( elements == null || elements.size() != 1 ) {
                throw new ApplicationRuntimeException( "종목 토론실 데이터 정보가 없거나 해당 클래스가 2개 이상입니다.");
            }

            Elements trList = elements.select("tr");                       // 게시글 목록
            if ( trList == null || trList.size() == 0 ) {
                throw new ApplicationRuntimeException( "종목 토론실 데이터 정보가 없습니다.");
            }

            for ( Element tr : trList ) {
                Elements tdList = tr.select("td");
                if ( tdList == null || tdList.size() != 6 ) {
                    throw new ApplicationRuntimeException( "종목 토론실 데이터 정보에 오류가 있습니다.");
                }

                NaverToron naverToron = new NaverToron();
                naverToron.setDate( tdList.get(0).select("span").text() );
                naverToron.setTitle( tdList.get(1).select("a").attr("title") );
                naverToron.setWriter( tdList.get(2).text() );
                naverToron.setRead( tdList.get(3).select("span").text() );
                naverToron.setSympathy( tdList.get(4).select("span").text() );
                naverToron.setAnti( tdList.get(5).select("span").text() );
                naverToronList.add( naverToron );
            }


        } catch ( Exception e ) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "종목 토론실 전체 페이지 정보 분석 중 오류가 발생했습니다.");
        }

        return naverToronList;
    }

    /**
     * 네이버 종목토론실 특정 페이지의 목록을 가져옵니다.
     * @param stockCode
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<NaverToron> getToronList(String stockCode, int page ) throws ApplicationRuntimeException {
        Document document	= getToronDocument(stockCode, page);;
        return getDailyInvestorList( document );
    }

    /**
     * 네이버 종목토론실 특정 페이지의 전체 목록을 가져옵니다.
     * 데이터가 너무 많으므로 최대 200개만 가져옵니다.
     * @param stockCode
     * @return
     */
    public ArrayList<NaverToron> getToronList( String stockCode ) {
        Document document = getToronDocument( stockCode, 1);
        int totalPages = getToronTotalPages( document );
        if ( totalPages > 200 ) totalPages = 200;

        ArrayList<NaverToron> naverToronList = new ArrayList<NaverToron>();
        for ( int i = 0; i < totalPages; i++ ) {
            ArrayList<NaverToron> list = getToronList( stockCode, i );
            naverToronList.addAll( list );
        }

        return naverToronList;
    }
}
