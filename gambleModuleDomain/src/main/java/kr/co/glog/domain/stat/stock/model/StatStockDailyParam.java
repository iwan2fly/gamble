package kr.co.glog.domain.stat.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stat.stock.entity.StatStockDaily;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatStockDailyParam")
public class StatStockDailyParam extends StatStockDaily {

    PagingParam pagingParam = new PagingParam();


}
