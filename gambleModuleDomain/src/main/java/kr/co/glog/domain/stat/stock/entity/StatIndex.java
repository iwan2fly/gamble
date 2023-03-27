package kr.co.glog.domain.stat.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatIndex")
public class StatIndex extends EntityModel {

    Long    statIndexId;
    String	marketCode;                         // 시장코드
    String  periodCode;                         // 주기코드
    Integer year;		                        // 년도
    Integer month;                              // 월
    String  yearOrder;                          // 년도의 월순서, 주순서
    Integer week;                               // 주차
    String  startDate;                          // 주기의 시작일자
    String  endDate;                            // 주기의 종료일자
    Integer tradeDays;                          // 거래일 수
    Float   pricePrevious;                      // 이전종가
    Float   priceStart;                         // 시작가
    Float   priceFinal;                         // 종가
    Float   priceLow;                           // 최저가
    Float   priceHigh;                          // 최고가
    Float   priceAverage;                       // 평균가
    Float   priceStandardDeviation;             // 가격 표준편차
    Float   priceChange;                        // 변동금액
    Float   pirceChangeMax;                     // 지수 최고 최저 차이
    Float   rateChange;                         // 변동률
    Float   rateChangeMax;                      // 최저최고 사이 비율
    Long    volumeTrade;                        // 거래량
    Long    volumeLow;                          // 최저거래량
    Long    volumeHigh;                         // 최고거래량
    Long    volumeAverage;                      // 평균거래량
    Long    volumeStandardDeviation;            // 거래량 표준편차
    String  priceLowDate;                       // 최저가날짜
    String  priceHighDate;                      // 최고가날짜
    String  volumeLowDate;                      // 최저거래량 날짜
    String  volumeHighDate;                     // 최고거래량 날짜
    Integer riseCount;                          // 상승회수
    Integer fallCount;                          // 하락회수
    Integer evenCount;                          // 보합회수
    Integer riseStockCount;                          // 상승주식수
    Integer fallStockCount;                          // 하락주식수
    Integer evenStockCount;                          // 보합주식수
}
