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
    Integer turn;		                        // 순번
    String  startDate;                          // 주기의 시작일자
    String  endDate;                            // 주기의 종료일자
    Float   priceLow;                           // 최저가
    Float   priceHigh;                          // 최고가
    Float   priceAverage;                       // 평균가
    Float   priceStandardDeviation;             // 가격 표준편차
    Long    volumeLow;                          // 최저거래량
    Long    volumeHigh;                         // 최고거래량
    Long    volumeAverage;                      // 평균거래량
    Long    volumeStandardDeviation;            // 거래량 표준편차
    String  priceLowDate;                       // 최저가날짜
    String  priceHighDate;                      // 최고가날짜
    String  volumeLowDate;                      // 최저거래량 날짜
    String  volumeHighDate;                     // 최고거래량 날짜
}
