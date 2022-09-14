package kr.co.glog.external.daumFinance.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=false)
public class DaumInvestorStock {
    String  date;
    Long    foreignOwnShares;
    Float   foreignOwnSharesRate;
    Long    foreignStraightPurchaseVolume;
    Long    institutionStraightPurchaseVolume;
    Long    institutionCumulativeStraightPurchaseVolume;
    Integer tradePrice;
    Integer changePrice;
    String  change;
    Long    accTradeVolume;
    Long    accTradePrice;
}
