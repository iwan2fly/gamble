package kr.co.glog.domain.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stock.entity.DartCorp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("DartCorpParam")
public class DartCorpParam extends DartCorp {

    PagingParam pagingParam = new PagingParam();

    boolean hasStockCode;
}
