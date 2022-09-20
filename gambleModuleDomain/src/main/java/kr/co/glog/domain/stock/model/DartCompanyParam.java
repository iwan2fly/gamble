package kr.co.glog.domain.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stock.entity.DartCompany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("DartCompanyParam")
public class DartCompanyParam extends DartCompany {

    PagingParam pagingParam = new PagingParam();
}
