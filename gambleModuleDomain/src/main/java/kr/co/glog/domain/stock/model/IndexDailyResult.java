package kr.co.glog.domain.stock.model;

import kr.co.glog.domain.stock.entity.IndexDaily;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("IndexDailyResult")
public class IndexDailyResult extends IndexDaily {

    Integer dataCount;                  // 자료건수 = 거래일수

    Float   priceLow;                   // 최소값
    Float   priceHigh;                  // 최대값
    Float   priceAverage;               // 평균값
    Float   priceStandardDeviation;     // 가격 표준편차

    Long    volumeLow;                  // 최소거래량
    Long    volumeHigh;                 // 최대거래량
    Long    volumeAverage;              // 평균거래량
    Long   volumeStandardDeviation;     // 거래량 표준편차
}
