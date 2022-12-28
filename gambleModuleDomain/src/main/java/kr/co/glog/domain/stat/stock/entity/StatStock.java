package kr.co.glog.domain.stat.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatStock")
public class StatStock extends EntityModel {

    Long    statStockId;
    String	stockCode;                            // 주식코드
    String  periodCode;                         // 주기코드
    Integer year;		                        // 년도
    Integer month;                              // 월
    String  yearWeek;                           // 년주차
    Integer week;                               // 주차
    String  startDate;                          // 주기의 시작일자
    String  endDate;                            // 주기의 종료일자
    Integer tradeDays;                          // 거래일 수
    Integer pricePrevious;                      // 이전종가
    Integer priceStart;                         // 시작가
    Integer priceFinal;                         // 종가
    Integer priceLow;                           // 최저가
    Integer priceHigh;                          // 최고가
    Integer priceAverage;                       // 평균가
    Integer priceStandardDeviation;             // 가격 표준편차
    Integer priceChange;                        // 변동금액
    Float   rateChange;                         // 변동률
    Long    volumeLow;                          // 최저거래량
    Long    volumeHigh;                         // 최고거래량
    Long    volumeAverage;                      // 평균거래량
    Long    volumeStandardDeviation;            // 거래량 표준편차
    Long    foreignerLow;                       // 외국인 최소
    Long    foreignerHigh;                      // 외국인 최대
    Long    foreignerAverage;                   // 외국인 평균
    Long    foreignerStandardDeviation;         // 외국인 표준편차
    String  priceLowDate;                       // 최저가날짜
    String  priceHighDate;                      // 최고가날짜
    String  volumeLowDate;                      // 최저거래량 날짜
    String  volumeHighDate;                     // 최고거래량 날짜
}
