package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class RankingStock {
    String  change;
    Integer  changePrice;
    String  changeRate;
    String  code;
    String  foreignRatio;
    Long    listedShareCount;
    String  marketCap;
    String  name;
    String  rank;
    String  symbolCode;
    Integer  tradePrice;
}
