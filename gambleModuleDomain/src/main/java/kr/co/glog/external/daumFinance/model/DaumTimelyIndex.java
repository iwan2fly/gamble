package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class DaumTimelyIndex {

    Long    accTradePrice;      // 거래대금
    Long    accTradeVolume;     // 거래량
    String  change;             // RISE, FALL
    Float   changePrice;        // 변동가격, 변동지수
    Float   changeRate;         // 변동률
    String  date;               // 2022-11-15 00:00:00
    Long    periodTradeVolume;  // 기간거래량
    Float   tradePrice;         // 거래지수
    String  tradeTime;          // 거래시간
}

