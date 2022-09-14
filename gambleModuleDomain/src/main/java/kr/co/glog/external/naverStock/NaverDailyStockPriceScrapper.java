/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.naverStock;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.external.naverStock.model.SiseDay;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class NaverDailyStockPriceScrapper {


    /**
     * 네이버주식에서 일별시세를 읽어옵니다.
     * @param stockCode
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getPriceDailyDocument(String stockCode, int page ) throws ApplicationRuntimeException {
        if ( stockCode == null ) throw new ParameterMissingException();
        Document document	= null;

        String url	=  "https://finance.naver.com/item/sise_day.nhn?code=##stockCode##&page=##page##";
        url = url.replaceAll( "##stockCode##", stockCode );
        url = url.replaceAll( "##page##", ""+page );
        log.debug( url );

        int		tryCount	= 0;
        while ( tryCount < 5 ) {
            try {
                tryCount++;
                document = Jsoup.connect(url).get();
                break;
            } catch (Exception e) {
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
            throw new ApplicationRuntimeException("네이버스톡의 일별 시세 데이터를 가져오지 못했습니다.");
        }

        return document;
    }

    /**
     * 특정일자의 일별시세 Document를 파싱해서 ArrayList<SiseDay>로 리턴합니다.
     * @param stockCode
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<SiseDay> getPriceDaily( String stockCode, int page ) throws ApplicationRuntimeException {
        if (stockCode == null) throw new ParameterMissingException();

        // 네이버 일별시세 document를 받습니다.
        Document document = getPriceDailyDocument(stockCode, page);

        // Document를 파싱해서 시세배열을 가져옵니다.
        ArrayList<SiseDay> siseDayList = parsePriceDaily(document);

        return siseDayList;
    }

    /**
     * 일별시세 Document에서 가격 목록을 파싱합니다.
     * @param document
     * @return
     */
    public ArrayList<SiseDay> parsePriceDaily( Document document ) {

        ArrayList<SiseDay> siseDayList  = new ArrayList<SiseDay>();

        // 일별시세 TR 만 SELECT 합니다.
        Elements trList = document.select(".type2").select("tr");

        // TR 리스트를 돌면서, 실제 데이터만 추출
        String price;
        for ( int i = 0; i < trList.size(); i++ ) {
            Element tr = trList.get(i);
            Elements tdList	= tr.select("td");

            // 시세 데이터의 td 셀은 7개
            if ( tdList.size() == 7 ) {
                int plusMinus	= -1;

                SiseDay siseDay	= new SiseDay();
                siseDay.setDate( tdList.get(0).select("span").html().replaceAll("[.]",  "") );

                price = tdList.get(1).select("span").html().replaceAll(",",  "").trim();
                siseDay.setPriceFinal( price.equals("") ? 0 : Integer.parseInt(price) );

                Elements	spans	=  tdList.get(2).select("span");
                // 1이 아니면 빈칸(마지막임)이거나 다른 오류
                if ( spans.size() == 1 ) {
                    if ( spans.get(0).attr("class").contains("nv01") ) plusMinus	= -1;

                    price = tdList.get(2).select("span").html().replaceAll(",",  "").trim();
                    siseDay.setPriceChange( plusMinus * ( price.equals("") ? 0 : Integer.parseInt(price) ) );
                } else {
                    break;
                }

                price = tdList.get(3).select("span").html().replaceAll(",",  "").trim();
                siseDay.setPriceStart( price.equals("") ? 0 : Integer.parseInt(price) );
                price = tdList.get(4).select("span").html().replaceAll(",",  "").trim();
                siseDay.setPriceHigh( price.equals("") ? 0 : Integer.parseInt(price) );
                price = tdList.get(5).select("span").html().replaceAll(",",  "").trim();
                siseDay.setPriceLow(price.equals("") ? 0 : Integer.parseInt(price) );
                price = tdList.get(6).select("span").html().replaceAll(",",  "").trim();
                siseDay.setVolumn( price.equals("") ? 0 : Integer.parseInt(price) );

                siseDayList.add( siseDay );
            }
        }

        return siseDayList;
    }

    /**
     * 특정 주식의 전체 시세 목록을 리턴합니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<SiseDay> getPriceDailyAll( String stockCode ) throws ApplicationRuntimeException {
        return getPriceDailyAll( stockCode, 0 );
    }

    public ArrayList<SiseDay> getPriceDailyAll( String stockCode, int delaySeconds ) throws ApplicationRuntimeException {
        if ( stockCode == null ) throw new ParameterMissingException();
        ArrayList<SiseDay> allList	= new ArrayList<SiseDay>();

        int page    = 1;
        boolean hasNextPage = true;

        while ( hasNextPage ) {

            // 네이버 일별시세 첫페이지 document를 받습니다.
            Document document = getPriceDailyDocument(stockCode, page);
            ArrayList<SiseDay> siseDayList = parsePriceDaily( document );
            allList.addAll( siseDayList );

            // 다음 페이지가 있는 지 확인
            Elements elements = document.select( ".pgR");
            if ( elements == null || elements.size() == 0 ) hasNextPage = false;

            try {
                Thread.sleep(delaySeconds * 1000);
            } catch ( Exception e ) {
                throw new ApplicationRuntimeException();
            }

            page++;
        }

        return allList;
    }




    /**
     * 투자자별 일별시세 Document 리턴
     * @param stockCode
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getPriceDailyByInvestorDocument( String stockCode, int page ) throws ApplicationRuntimeException {
        if ( stockCode == null ) throw new ParameterMissingException();
        Document document	= null;

        String url	=  "https://finance.naver.com/item/frgn.naver?code=##stockCode##&page=##page##";
        url = url.replaceAll( "##stockCode##", stockCode );
        url = url.replaceAll( "##page##", "" + page );
        log.debug( url );

        int		tryCount	= 0;
        while ( tryCount < 5 ) {
            try {
                tryCount++;
                document = Jsoup.connect(url).get();
                break;
            } catch (Exception e) {
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
            throw new ApplicationRuntimeException("투자자별 일별 시세 데이터를 가져오지 못했습니다.");
        }

        return document;
    }

    /**
     * 투자자별 일별시세 ArrayList<SiseDay> 리턴
     * @param stockCode
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<SiseDay> getPriceDailyByInvestor( String stockCode, int page ) throws ApplicationRuntimeException {
        if (stockCode == null) throw new ParameterMissingException();

        // 네이버 투자자별 일별시세 document를 받습니다.
        Document document = getPriceDailyByInvestorDocument(stockCode, page);

        // Document를 파싱해서 시세배열을 가져옵니다.
        ArrayList<SiseDay> siseDayList = parsePriceDailyByInvestor(document);

        return siseDayList;
    }

    /**
     * 투자자별 일별시세 Document 파싱
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<SiseDay> parsePriceDailyByInvestor( Document document ) throws ApplicationRuntimeException {

        ArrayList<SiseDay> siseDayList = new ArrayList<SiseDay>();

        // 투자자별 일별시세 TR 만 SELECT 합니다.
        Elements trList = document.select("table").get(2).select("tr");

        // TR 리스트를 돌면서, 실제 데이터만 추출
        String price	= "";
        for ( int i = 0; i < trList.size(); i++ ) {
            Element tr = trList.get(i);
            Elements tdList	= tr.select("td");

            // 시세 데이터의 td 셀은 9개
            if ( tdList.size() == 9 ) {

                SiseDay siseDay	= new SiseDay();
                siseDay.setDate( tdList.get(0).select("span").html().replaceAll("[.]",  "") );
                // priceDaily.setPriceFinal( tdList.get(1).select("span").html().replaceAll(",",  "") );
                // priceDaily.setPriceChange( tdList.get(2).select("span").html().replaceAll(",",  "") );
                siseDay.setRateChange( tdList.get(3).select("span").html().replaceAll("%",  "") );

                price = tdList.get(4).select("span").html().replaceAll(",",  "").trim();
                siseDay.setVolumn( price.equals("") ? 0 : Integer.parseInt(price) );
                price = tdList.get(5).select("span").html().replaceAll(",",  "").trim();
                siseDay.setVolumnOrg( price.equals("") ? 0 : Integer.parseInt(price) );
                price = tdList.get(6).select("span").html().replaceAll(",",  "").trim();
                siseDay.setVolumnForeigner( price.equals("") ? 0 : Integer.parseInt(price) );
                price = tdList.get(7).select("span").html().replaceAll(",",  "").trim();
                siseDay.setOwnForeigner( price.equals("") ? 0 : Long.parseLong(price) );
                siseDay.setOwnForeignerRate( tdList.get(8).select("span").html().replaceAll(",",  "").replaceAll("%",  "") );

                siseDayList.add( siseDay );
            }
        }

       return siseDayList;
    }

    /**
     * 특정 주식의 투자자별 전체 시세 목록을 리턴합니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<SiseDay> getPriceDailyByInvestorAll( String stockCode ) throws ApplicationRuntimeException {
        return getPriceDailyByInvestorAll( stockCode, 0 );
    }

    public ArrayList<SiseDay> getPriceDailyByInvestorAll( String stockCode, int delaySeconds ) throws ApplicationRuntimeException {
        if ( stockCode == null ) throw new ParameterMissingException();
        ArrayList<SiseDay> allList	= new ArrayList<SiseDay>();

        int page    = 1;
        boolean hasNextPage = true;

        while ( hasNextPage ) {

            // 네이버 일별시세 첫페이지 document를 받습니다.
            Document document = getPriceDailyByInvestorDocument(stockCode, page);
            ArrayList<SiseDay> siseDayList = parsePriceDailyByInvestor( document );
            allList.addAll( siseDayList );

            // 다음 페이지가 있는 지 확인
            Elements elements = document.select( ".pgR");
            if ( elements == null || elements.size() == 0 ) hasNextPage = false;

            try {
                Thread.sleep(delaySeconds * 1000);
            } catch ( Exception e ) {
                throw new ApplicationRuntimeException();
            }

            page++;
        }

        return allList;
    }
}
