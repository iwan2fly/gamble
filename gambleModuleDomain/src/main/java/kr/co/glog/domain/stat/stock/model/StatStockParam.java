package kr.co.glog.domain.stat.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stat.stock.entity.StatStock;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatStockParam")
public class StatStockParam extends StatStock {

    PagingParam pagingParam = new PagingParam();

    Integer endYear;
}
