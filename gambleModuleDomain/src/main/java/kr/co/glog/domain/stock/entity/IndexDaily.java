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
    Float   priceStart;                 // 시가
    Float   priceHigh;                  // 고가
    Float   priceLow;                   // 저가
    Long    priceTrade;                 // 거래대금
    Long	volumeTrade;		        // 거래량
    Float 	rateChange;			        // 변동비
    Integer riseStockCount;             // 오른주식수
    Integer evenStockCount;             // 보합주식수
    Integer fallStockCount;             // 내린주식수

    public Float getPriceYesterday() {
        return getPriceFinal() - getPriceChange();
    }
}
