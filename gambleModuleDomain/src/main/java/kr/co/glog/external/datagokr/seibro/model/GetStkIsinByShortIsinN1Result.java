package kr.co.glog.external.datagokr.seibro.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetStkIsinByShortIsinN1Result {

    String  eltscYn;            // ?
    String  engSecnNm;          // 영문 종목명
    String  isin;               // 표준코드
    String  issuDt;             // 주식발행일자
    String  issucoCustno;       // 발행회사번호
    String  korSecnNm;          // 국문종목명
    String  secnKacdNm;         // 주식종류 ( 보통주... )
    String  shotnIsin;          // 단축코드
    String  numOfRows;
    String  pageNo;
}
