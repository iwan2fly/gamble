package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class DaumQuotes {

    // 더 많은 정보가 있지만, 일단 필요한 것은 아래와 같습니다.

    String change;                  // 변동 RISE, FALL
    Long accTradePrice;           // 거래대금
    Long accTradeVolume;          // 거래량
    Integer basePrice;               // 기준가
    Integer openingPrice;            // 시가
    Integer highPrice;               // 고가
    Integer lowPrice;                // 저가
    Integer tradePrice;              // 종가
    String tradeDate;               // 거래일 ( 2023-01-26 )
    Integer changePrice;             // 가격변동
    Float changeRate;              // 변동률 ( x 100 해야 비율 )
    Long prevAccTradeVolume;      // 전일거래량
    Integer prevClosingPrice;        // 전일종가
    Long foreignOwnShares;        // 외국인보유량
    Float foreignRatio;            // 외국인보유율
    Long listedShareCount;        // 상장주식수
    Long marketCap;               // 시가총액
    String listingDate;             // 상장일     ( 2022-12-13 )
    String market;                  // 시장 ( KOSDAQ )
    String name;                    // 이름
    String code;                    // KR7049960008
    String companySummary;          // 회사요약
    Float debtRate;                // 부채비율
    String symbolCode;              // ( A049960 )
    String wicsSectorCode;          // G352010
    String wicsSectorName;          // 생물공학





}

