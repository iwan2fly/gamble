package kr.co.glog.external.naverApi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 네이버 검색 API 결과 중 블로그 검색 결과
 */
@Getter
@Setter
@ToString
public class NaverNewsSearchResult extends NaverSearchResult {

    String  originallink;        // 검색 결과 문서의 제공 언론사 하이퍼텍스트 link를 나타낸다.
    Date    pubDate;             // 검색 결과 문서가 네이버에 제공된 시간이다.
}
