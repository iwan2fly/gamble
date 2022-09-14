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
public class NaverBlogSearchResult extends NaverSearchResult {

    Date    postdate;           // 블로그 포스트를 작성한 날짜이다.
    String  bloggername;        // 검색결과 블로그 포스트를 작성한 블로거의 이름이다.
    String  bloggerlink;        // 검색 결과 블로그 포스트를 작성한 블로거의 하이퍼텍스트 link 이다.
}
