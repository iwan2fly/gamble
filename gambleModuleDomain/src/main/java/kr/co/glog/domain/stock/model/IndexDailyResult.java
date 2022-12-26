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

    Integer dataCount;              // 자료건수 = 거래일수

    Float   minPrice;               // 최소값
    Float   maxPrice;               // 최대값
    Float   averagePrice;           // 평균값

    Long    minVolume;              // 최소거래량
    Long    maxVolume;              // 최대거래량
    Long    averageVolume;          // 평균거래량
}
