package kr.co.glog.domain.stat.common.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stat.common.entity.StatBasisDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatBasisDateParam")
public class StatBasisDateParam extends StatBasisDate {

    PagingParam pagingParam = new PagingParam();


}
