package kr.co.glog.domain.stat.stock.model;

import kr.co.glog.domain.stat.stock.entity.DailyRankStock;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("DailyRankStockResult")
public class DailyRankStockResult extends DailyRankStock {
}
