/**
 *  네이버주식의 일별시세를 scrapping 해옵니다.
 *      기본 일별시세         :  http://finance.naver.com/item/sise_day.nhn?code=011040&page=1
 *      투자자별 일별시세      : https://finance.naver.com/item/frgn.naver?code=011040&page=2
 */

package kr.co.glog.external.daumFinance;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.utils.StockUtil;
import kr.co.glog.domain.stock.MarketCode;
import kr.co.glog.external.daumFinance.model.DaumIndex;
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

    /**
     * 다음 주식에서 특정 주식의 기본주가정보를 읽어옵니다.
     * @param stockCode
     * @return
     * @throws ApplicationRuntimeException
     */
    private Document getDocument(String stockCode ) throws ApplicationRuntimeException {

        String url = "https://finance.daum.net/api/quotes/" + StockUtil.toStockCode7( stockCode );


        Document document	= null;

        try {
            document = Jsoup.connect(url).header("referer", url ).ignoreContentType(true).get();
            log.debug( document.toString() );
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationRuntimeException( "다음 다음 인덱스 상세정보를 읽는 중 오류가 발생했습니다.");
        }

        return document;
    }


    /**
     *  marketCode 로 인덱스 상세정보 리턴
     * @param marketCode
     * @return
     * @throws ApplicationRuntimeException
     */
    public DaumIndex getDaumIndex( String marketCode ) throws ApplicationRuntimeException {
        return getDaumIndex( getDocument( marketCode ) );
    }

    /**
     * Document 로부터 인덱스 상세정보 리턴
     * @param document
     * @return
     * @throws ApplicationRuntimeException
     */
    public DaumIndex getDaumIndex(Document document ) throws ApplicationRuntimeException {

        DaumIndex daumIndex = new DaumIndex();

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse( document.text() );

            daumIndex.setChange( jsonObject.get("change").toString() );                     // RISE = 상승, FAll = 하락 ?
            daumIndex.setChangePrice( jsonObject.get("changePrice").toString() );            // 변동 지수
            daumIndex.setChangeRate( jsonObject.get("changeRate").toString() );              // 변동률
            daumIndex.setTradeDate( jsonObject.get("tradeDate").toString() );                // 20230109
            daumIndex.setBasePrice( jsonObject.get("basePrice").toString() );                // 전일지수
            daumIndex.setOpeningPrice( jsonObject.get("openingPrice").toString() );          // 시가
            daumIndex.setHighPrice(jsonObject.get("highPrice").toString() );                // 고가
            daumIndex.setLowPrice( jsonObject.get("lowPrice").toString() );                  // 저가
            daumIndex.setTradePrice( jsonObject.get("tradePrice").toString() );              // 종가
            daumIndex.setAccTradeVolume( jsonObject.get("accTradeVolume").toString() );      // 거래량(천주)
            daumIndex.setAccTradePrice( jsonObject.get("accTradePrice").toString() );        // 거래대금(백만)

        } catch ( Exception e ) {
            throw new ApplicationRuntimeException( "일자별 데이터 문서 파싱 중 오류가 발생했습니다.");
        }

        return daumIndex;
    }


}

