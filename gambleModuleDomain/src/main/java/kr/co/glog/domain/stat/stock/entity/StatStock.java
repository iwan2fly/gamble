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
    String  marketCode;                         // 시장코드
    String	stockCode;                          // 주식코드
    String  stockName;                          // 주식이름
    String  periodCode;                         // 주기코드
    Integer year;		                        // 년도
    Integer month;                              // 월
    String  yearOrder;                          // 년도의 월순서, 주순서
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
    Long    priceTrade;                         // 거래대금
    Long    priceTotal;                         // 기간 마지막 시총
    Long    priceTotalPrevious;                 // 기간 이전 마지막 시총
    Long    priceChangeMax;                     // 가격 최고 최저 차이
    Float   rateChange;                         // 변동률
    Float   rateChangeMax;                      // 변동최저최고 사이 비율
    Long    volumeTotal;                        // 전체주식수
    Long    volumeTotalPrevious;                // 이전 전체주식수
    Long    volumeTrade;                        // 거래량
    Long    volumeLow;                          // 최저거래량
    Long    volumeHigh;                         // 최고거래량
    Long    volumeAverage;                      // 평균거래량
    Long    volumeStandardDeviation;            // 거래량 표준편차
    Long    foreignerPrevious;                  // 외국인 보유량 이전
    Long    foreignerStart;                     // 외국인 보유량 최초
    Long    foreignerLow;                       // 외국인 보유량 최소
    Long    foreignerHigh;                      // 외국인 보유량 최대
    Long    foreignerFinal;                     // 외국인 보유량 최종
    Long    foreignerAverage;                   // 외국인 보유량 평균
    Long    foreignerStandardDeviation;         // 외국인 보유량 표준편차
    String  priceLowDate;                       // 최저가날짜
    String  priceHighDate;                      // 최고가날짜
    String  volumeLowDate;                      // 최저거래량 날짜
    String  volumeHighDate;                     // 최고거래량 날짜
    Integer riseCount;                          // 상승회수
    Integer evenCount;                          // 보합회수
    Integer fallCount;                          // 하락회수
    Integer riseStockCount;                      // 상승회수
    Integer evenStockCount;                      // 보합회수
    Integer fallStockCount;                      // 하락회수
}
