package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class IncludedStock {
    String  code;
    String  name;
    String  symbolCode;
    Integer    tradePrice;
    String  change;
    Integer changePrice;
    String  changeRate;
    String  accTradeVolume;
    String  accTradePrice;
    String  marketCap;
    String  foreignRatio;
}
