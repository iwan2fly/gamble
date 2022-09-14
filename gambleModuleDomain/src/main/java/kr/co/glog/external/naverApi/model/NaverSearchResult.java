package kr.co.glog.external.naverApi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class NaverSearchResult {

    String  rss;                // 디버그를 쉽게 하고 RSS 리더기만으로 이용할 수 있게 하기 위해 만든 RSS 포맷의 컨테이너이며 그 외의 특별한 의미는 없다.
    String  channel;            // 검색 결과를 포함하는 컨테이너이다. 이 안에 있는 title, link, description 등의 항목은 참고용으로 무시해도 무방하다.
    Date    lastBuildDate;      // 검색 결과를 생성한 시간이다.

    Integer total;              // 검색 결과 문서의 총 개수를 의미한다.
    Integer start;              // 검색 결과 문서 중, 문서의 시작점을 의미한다.
    Integer display;            // 검색된 검색 결과의 개수이다
    String  items;              // 개별 검색 결과이며 title, link, description 을 포함한다
    String  title;              // 검색 결과 문서의 제목을 나타낸다. 제목에서 검색어와 일치하는 부분은 태그로 감싸져 있다
    String  link;               // 검색결과 문서의 하이퍼텍스트 link를 나타낸다
    String  description;        // 검색결과 문서의 내용을 요약한 구절 정보이다. 문서 전체의 내용은 link를 따라가면 읽을 수 있다. 구절에서 검색어와 일치하는 부분은 태그로 감싸져 있다

}
