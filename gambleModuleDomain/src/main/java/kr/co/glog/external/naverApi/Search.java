package kr.co.glog.external.naverApi;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.external.naverApi.model.NaverSearchParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *  네이버 검색
 */
public class Search {

    // 기본 URL
    public static String base = "https://openapi.naver.com/v1/search/";

    public static String blog = base + "blog.json";             // 블로그
    public static String news = base + "news.json";             // 뉴스
    public static String cafe = base + "cafearticle.json";      // 까페
    public static String web  = base + "webkr.json";            // 웹문서
    public static String prof = base + "doc.json";              // 전문자료

    /**
     * 블로그 검색
     * @param naverSearchParam
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getBlog( NaverSearchParam naverSearchParam ) throws ApplicationRuntimeException {

        Document document   = null;

        try {
            document = Jsoup.connect(blog)
                    .header("X-Naver-Client-Id", "rZnGK10CzEnUBIhjrn78")
                    .header("X-Naver-Client-Secret", "dIcamWYiqz")
                    .data( "query", naverSearchParam.getQuery() )
                    .data( "display", ""+naverSearchParam.getDisplay() )
                    .data( "start", ""+naverSearchParam.getStart() )
                    .data( "sort", naverSearchParam.getSort() )
                    .ignoreContentType(true).get();

        } catch (Exception e) {
            throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");

        }

        return document;
    }

    /**
     * 뉴스 검색
     * @param naverSearchParam
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getNews( NaverSearchParam naverSearchParam ) throws ApplicationRuntimeException {

        Document document   = null;

        try {
            document = Jsoup.connect(news)
                    .header("X-Naver-Client-Id", "rZnGK10CzEnUBIhjrn78")
                    .header("X-Naver-Client-Secret", "dIcamWYiqz")
                    .data( "query", naverSearchParam.getQuery() )
                    .data( "display", ""+naverSearchParam.getDisplay() )
                    .data( "start", ""+naverSearchParam.getStart() )
                    .data( "sort", naverSearchParam.getSort() )
                    .ignoreContentType(true).get();

        } catch (Exception e) {
            throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");

        }

        return document;
    }

    /**
     * 까페 검색
     * @param naverSearchParam
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getCafe( NaverSearchParam naverSearchParam ) throws ApplicationRuntimeException {

        Document document   = null;

        try {
            document = Jsoup.connect(cafe)
                    .header("X-Naver-Client-Id", "rZnGK10CzEnUBIhjrn78")
                    .header("X-Naver-Client-Secret", "dIcamWYiqz")
                    .data( "query", naverSearchParam.getQuery() )
                    .data( "display", ""+naverSearchParam.getDisplay() )
                    .data( "start", ""+naverSearchParam.getStart() )
                    .data( "sort", naverSearchParam.getSort() )
                    .ignoreContentType(true).get();

        } catch (Exception e) {
            throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");

        }

        return document;
    }

    /**
     * 웹 문서 검색
     * @param naverSearchParam
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getWeb( NaverSearchParam naverSearchParam ) throws ApplicationRuntimeException {

        Document document   = null;

        try {
            document = Jsoup.connect(prof)
                    .header("X-Naver-Client-Id", "rZnGK10CzEnUBIhjrn78")
                    .header("X-Naver-Client-Secret", "dIcamWYiqz")
                    .data( "query", naverSearchParam.getQuery() )
                    .data( "display", ""+naverSearchParam.getDisplay() )
                    .data( "start", ""+naverSearchParam.getStart() )
                    .ignoreContentType(true).get();

        } catch (Exception e) {
            throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");

        }

        return document;
    }

    /**
     * 전문자료 검색
     * @param naverSearchParam
     * @return
     * @throws ApplicationRuntimeException
     */
    public Document getProf( NaverSearchParam naverSearchParam ) throws ApplicationRuntimeException {

        Document document   = null;

        try {
            document = Jsoup.connect(web)
                    .header("X-Naver-Client-Id", "rZnGK10CzEnUBIhjrn78")
                    .header("X-Naver-Client-Secret", "dIcamWYiqz")
                    .data( "query", naverSearchParam.getQuery() )
                    .data( "display", ""+naverSearchParam.getDisplay() )
                    .data( "start", ""+naverSearchParam.getStart() )
                    .ignoreContentType(true).get();

        } catch (Exception e) {
            throw new ApplicationRuntimeException( "특정 종목의 일자별 가격 데이터를 읽는 중 오류가 발생했습니다.");

        }

        return document;
    }
}
