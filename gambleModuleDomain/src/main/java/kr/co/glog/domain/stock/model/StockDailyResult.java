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

    Integer  dataCount;                  // 자료건수 = 거래일수

    Integer   priceLow;                   // 최소값
    Integer   priceHigh;                  // 최대값
    Integer   priceAverage;               // 평균값
    Integer   priceStandardDeviation;     // 가격 표준편차
    Integer   pricePrevious;              // 이전가격
    Long      priceTotalPrevious;         // 이전시총
    Long      priceTrade;                 // 거래대금

    Long    volumeLow;                  // 최소거래량
    Long    volumeHigh;                 // 최대거래량
    Long    volumeAverage;              // 평균거래량
    Long    volumeStandardDeviation;     // 거래량 표준편차
    String  volumeLowDate;              // 거래량 최소일자
    String  volumeHighDate;             // 거래량 최대일자

    Long    foreignerLow;                  // 외국인보유최소
    Long    foreignerHigh;                 // 외국인보유최대
    Long    foreignerAverage;              // 외국인보유평균
    Long    foreignerStandardDeviation;     // 외국인보유 표준편차
    Long    foreignerStart;              // 외국인 시작시 보유
    Long    foreignerFinal;                // 외국인 최종 보유

    String  priceLowDate;           // 최저가일자
    String  priceHighDate;          // 최고가일자

    Integer riseCount;                          // 상승회수
    Integer evenCount;                          // 보합회수
    Integer fallCount;                          // 하락회수

    String  startDate;              // 시작일자
    String  endDate;                // 종료일자


}
