package kr.co.glog.domain.stat.stock.model;

import kr.co.glog.domain.stat.stock.entity.StatStock;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatStockResult")
public class StatStockResult extends StatStock {

    Integer rangeSize;              // 밤위크기
    Float   rateChangeMin;          // 범위 최소값
    Float   rateChangeMax;          // 범위 최대값
    Integer stockCount;             // 주식 수
}
