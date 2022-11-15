/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.daumFinance;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.domain.service.IndexDailyService;
import kr.co.glog.domain.stock.dao.IndexDailyDao;
import kr.co.glog.domain.stock.dao.StockDao;
import kr.co.glog.domain.stock.entity.Stock;
import kr.co.glog.domain.stock.entity.IndexDaily;
import kr.co.glog.external.daumFinance.model.DaumDailyIndex;
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
public class DaumDailyIndexScrapper {

    private final IndexDailyService indexDailyService;
    private final IndexDailyDao indexDailyDao;
    private final StockDao stockDao;

    /**
     * 다음 주식에서 특정 종목의 일자별 가격 문서를 읽어옵니다.
     * @param marketTypeCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getDocument(String marketTypeCode, int perPage, int page ) throws ApplicationRuntimeException {
        if ( perPage > 100 ) perPage = 100;     // 페이지당 100개가 한계

        Document document	= null;
        String url = "https://finance.daum.net/api/market_index/days?page=##page##&perPage=##perPage##&market=##marketTypeCode##&pagination=true";
        url = url.replaceAll( "##marketTypeCode##", marketTypeCode );
        url = url.replaceAll( "##page##", ""+page );
        url = url.replaceAll( "##perPage##", ""+perPage );

        try {
            document = Jsoup.connect(url).header("referer", "https://finance.daum.net/domestic/" + marketTypeCode ).ignoreContentType(true).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "다음 KRD 인덱스 데이터를 읽는 중 오류가 발생했습니다.");
        }

        return document;
    }

    /**
     * 목록의 전체 페이지 개수를 리턴합니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public int getTotalPages( String marketTypeCode, int perPage ) throws ApplicationRuntimeException {

        Document document = getDocument( marketTypeCode, perPage, 1);
        return  getTotalPages( document );            // 목록의 전체 페이지

    }

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
            throw new ApplicationRuntimeException( "페이지 개수를 읽는 중 오류가 발생했습니다.");
        }

        return totalPages;
    }


    /**
     * Document 로부터 일자별 데이터 목록을 리턴
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<DaumDailyIndex> getDailyIndexList( Document document ) throws ApplicationRuntimeException {

        ArrayList<DaumDailyIndex> rankingStockList = new ArrayList<DaumDailyIndex>();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(document.text());
            JSONArray dataArray = (JSONArray) jsonObject.get("data");        // 현재 페이지의 주식 목록

            ObjectMapper objectMapper = new ObjectMapper();
            int count = 0;

            for (int i = 0; i < dataArray.size(); i++) {
                DaumDailyIndex daumDailyIndex = objectMapper.readValue(dataArray.get(i).toString(), DaumDailyIndex.class);
                log.debug(count++ + " : " + daumDailyIndex.toString());
                rankingStockList.add(daumDailyIndex);
            }
        } catch ( Exception e ) {
            throw new ApplicationRuntimeException( "일자별 데이터 문서 파싱 중 오류가 발생했습니다.");
        }

        return rankingStockList;
    }

    /**
     * 일자별 가격 데이터 목록을 가져옵니다.
     * @param stockCode
     * @param perPage
     * @param page
     * @return
     * @throws ApplicationRuntimeException
     */
    public ArrayList<DaumDailyIndex> getDailyIndexList(String marketTypeCode, int perPage, int page ) throws ApplicationRuntimeException {

        Document document	= getDocument( marketTypeCode, perPage, page);;
        return  getDailyIndexList( document );
    }



    /**
     * 전체 일별 데이터를 IndexDaily 테이블에 인서트
     * @param marketTypeCode
     */
    public void insertDailyIndexFullData( String marketTypeCode  ) {

        int perPage = 100;

        ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
        int totalPages = getTotalPages( marketTypeCode, perPage );

        log.debug( "totalPages : " + totalPages );

        for ( int page = 1; page <= totalPages; page++ ) {

            ArrayList<DaumDailyIndex> daumDailyStockList = getDailyIndexList( marketTypeCode, perPage, page );

            for ( DaumDailyIndex daumDailyIndex : daumDailyStockList ) {
                IndexDaily indexDaily = indexDailyService.getIndexDailyFromDaumDailyIndex( marketTypeCode, daumDailyIndex );
                indexDailyList.add( indexDaily );
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for ( IndexDaily indexDaily : indexDailyList ) {
            log.debug( indexDaily.toString() );
            try {
                indexDailyDao.insertIndexDaily( indexDaily );
            } catch ( org.springframework.dao.DuplicateKeyException dke ) {
                log.debug( dke.getMessage() );
                break;
            }
        }

    }

    /**
     * 일별 데이터 첫 페이지를 테이블에 업서트
     * @param marketTypeCode
     */
    public void upsertDailyStock( String marketTypeCode  ) throws InterruptedException {

        int perPage = 10;

        // 다음
        ArrayList<IndexDaily> indexDailyList = new ArrayList<IndexDaily>();
        ArrayList<DaumDailyIndex> daumDailyIndexList = null;

        int readCount = 0;
        boolean isRead = false;
        while ( !isRead ) {

            try {
                daumDailyIndexList = getDailyIndexList(marketTypeCode, perPage, 1);
                isRead = true;
            } catch (Exception e) {
                e.printStackTrace();
                readCount++;
                Thread.sleep( 3000 );
            }
        }

        // 우리 DB에 맞게 컨버트
        for ( DaumDailyIndex daumDailyIndex : daumDailyIndexList ) {
            IndexDaily indexDaily = indexDailyService.getIndexDailyFromDaumDailyIndex( marketTypeCode, daumDailyIndex );
            indexDailyList.add( indexDaily );
        }

        for ( int i = 0; i < indexDailyList.size(); i++ ) {
            indexDailyDao.saveIndexDaily( indexDailyList.get(i) );
        }

    }
}
