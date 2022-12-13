package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("IndexDaily")
public class IndexDaily extends EntityModel {

    Long    indexDailyId;
    String	marketCode;
    String 	tradeDate;		            // 날짜
    Float	priceFinal;		            // 종가
    Float   priceChange;                // 변동금액
    Float 	rateChange;			        // 변동비
    Long	volume;		                // 거래량
    Long    priceTotal;                 // 거래대금
    Long    priceTotalPerson;           // 거래대금 - 개인
    Long    priceTotalForeigner;        // 거래대금 - 외인
    Long    priceTotalOrg;              // 거래대금 - 기관

    public Float getPriceYesterday() {
        return getPriceFinal() - getPriceChange();
    }
}
