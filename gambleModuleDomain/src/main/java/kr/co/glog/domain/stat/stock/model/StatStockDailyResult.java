package kr.co.glog.domain.stat.stock.model;

import kr.co.glog.domain.stat.stock.entity.StatStockDaily;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatStockDailyResult")
public class StatStockDailyResult extends StatStockDaily {
}
