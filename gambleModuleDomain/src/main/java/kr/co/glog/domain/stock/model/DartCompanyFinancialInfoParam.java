package kr.co.glog.domain.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stock.entity.DartCompanyFinancialInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("DartCompanyFinancialInfoParam")
public class DartCompanyFinancialInfoParam extends DartCompanyFinancialInfo {

    PagingParam pagingParam = new PagingParam();


}
