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
public class NaverCafeSearchResult extends NaverSearchResult {

    String  cafename;        // 검색 결과 문서가 작성된 까페 이름이다.
    String  cafeurl;         // 검색 결과 문서가 작성된 까페의 하이퍼텍스트 link이다.
}
