package kr.co.glog.external.datagokr.fsc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetStockPriceInfoResult {
    // {"basDt":"20221125","srtnCd":"000020","isinCd":"KR7000020008","itmsNm":"동화약품","mrktCtg":"KOSPI","clpr":"8980","vs":"10","fltRt":".11","mkp":"9040","hipr":"9090","lopr":"8970","trqu":"60793","trPrc":"547624830","lstgStCnt":"27931470","mrktTotAmt":"250824600600"}

    String  basDt;              // 기준일자
    String  srtnCd;             // 단축코드
    String  isinCd;             // isinCode : 국제 채권 식별 번호
    String  itmsNm;             // 종목명
    String  mrktCtg;            // 시장구분
    Integer mkp;                // 시가
    Integer hipr;               // 고가
    Integer lopr;               // 저가
    Integer clpr;               // 종가
    Integer vs;                 // 대비
    Float   fltRt;              // 등락율
    Long    trqu;               // 거래량
    Long    trPrc;              // 거래대금
    Long    lstgStCnt;          // 상장주식수
    Long    mrktTotAmt;         // 시가총액
}


