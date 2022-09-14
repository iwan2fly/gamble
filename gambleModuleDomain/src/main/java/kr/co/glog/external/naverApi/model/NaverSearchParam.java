package kr.co.glog.external.naverApi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NaverSearchParam {

    String  query;              // 검색을 원하는 문자열로서 UTF-8 로 인코딩한다.
    Integer display = 100;      // 검색 결과 출력 건수 지정 ( 기본값 10, 최대값 100 )
    Integer start   = 1;        // 검색 시작 위치로 최대 1000까지 가능 ( 기본값 1, 최대값 1000 )
    String  sort    = NaverSortOption.sim;    // 정렬옵션 : sim = 유사도순, date = 날짜순
}
