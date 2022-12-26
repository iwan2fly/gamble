package kr.co.glog.external.datagokr.fsc.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetMarketIndexInfoResult {

    // {"basDt":"20200110","idxNm":"코스피","idxCsf":"KOSPI시리즈","epyItmsCnt":"792","clpr":"2206.39","vs":"19.94","fltRt":".91","mkp":"2189.48","hipr":"2206.92","lopr":"2188.1",
    // "trqu":"594536948","trPrc":"6459299778577","lstgMrktTotAmt":"1484063380523097","lsYrEdVsFltRg":"9","lsYrEdVsFltRt":".4",
    // "yrWRcrdHgst":"2873.47","yrWRcrdHgstDt":"20201230","yrWRcrdLwst":"1457.64","yrWRcrdLwstDt":"20200319","basPntm":"19800104","basIdx":"100"}

    String  basDt;              // 기준일자
    String  idxNm;              // 인덱스 이름
    String  idxCsf;             // 인덱스 구분(?)
    String  epyItmsCnt;         // 지수가 채용한 종목 수
    Float   clpr;               // 종가
    Float   vs;                 // 대비
    Float   fltRt;              // 등락율
    Float   mkp;                // 시가
    Float   hipr;               // 고가
    Float   lopr;               // 저가
    Long    trqu;               // 거래량
    Long    trPrc;              // 거래대금
    Long    lstgMrktTotAmt;     // 지수에 포함된 종목의 시가총액
    Float   lsYrEdVsFltRt;      // 지수의 전년말대비 등락율
    Float   lsYrEdVsFltRg;      // 전년말대비 등락폭
    Float   yrWRcrdHgst;        // 연중최고기록
    Float   yrWRcrdHgstDt;      // 연중최고기록 일자
    Float   yrWRcrdLwst;        // 연중최고저록
    Float   yrWRcrdLwstDt;      // 연중최고저록 일자
    String  basPntm;            // 기준시점
    Float  basIdx;             // 기준지수
}


