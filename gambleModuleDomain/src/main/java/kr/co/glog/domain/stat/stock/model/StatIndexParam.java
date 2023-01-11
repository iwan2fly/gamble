package kr.co.glog.domain.stat.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stat.stock.entity.StatIndex;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatIndexParam")
public class StatIndexParam extends StatIndex {

    PagingParam pagingParam = new PagingParam();

    Integer endYear;

}
