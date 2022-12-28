package kr.co.glog.domain.stock.model;

import kr.co.glog.domain.stock.entity.StockDaily;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StockDailyResult")
public class StockDailyResult extends StockDaily {

    Integer dataCount;                  // 자료건수 = 거래일수

    Integer   priceLow;                   // 최소값
    Integer   priceHigh;                  // 최대값
    Integer   priceAverage;               // 평균값
    Integer   priceStandardDeviation;     // 가격 표준편차

    Long    volumeLow;                  // 최소거래량
    Long    volumeHigh;                 // 최대거래량
    Long    volumeAverage;              // 평균거래량
    Long   volumeStandardDeviation;     // 거래량 표준편차
}
