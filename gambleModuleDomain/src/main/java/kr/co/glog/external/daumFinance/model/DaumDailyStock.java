package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class DaumDailyStock {
    String  symbolCode;
    String  date;
    Integer tradePrice;
    String  tradeTime;
    String  change;
    Integer changePrice;
    String  changeRate;
    Integer prevClosingPrice;
    String  exchangeCountry;
    Integer openingPrice;
    Integer highPrice;
    Integer lowPrice;
    Long    accTradePrice;
    Long    accTradeVolume;
    Long    periodTradePrice;
    Long    periodTradeVolume;
    Integer listedSharesCount;
}
