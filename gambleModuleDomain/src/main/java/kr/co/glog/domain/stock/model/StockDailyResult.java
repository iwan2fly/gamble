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
}
