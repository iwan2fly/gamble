package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class DaumDailyIndex {

    Long    accTradePrice;      // 거래대금
    Long    accTradeVolume;     // 거래량
    String  change;             // RISE, FALL
    Float   changePrice;        // 변동가격, 변동지수
    String  date;               // 2022-11-15 00:00:00
    Long    foreignStraightPurchasePrice;           // 외인거래대금
    Long    individualStraightPurchasePrice;        // 개인거래대금
    Long    institutionStraightPurchasePrice;       // 기관거래대금
    Float   tradePrice;         // 종가
}

