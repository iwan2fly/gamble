package kr.co.glog.domain.stock.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.stock.entity.Company;
import kr.co.glog.domain.stock.entity.CompanyFinancialInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("CompanyFinancialInfoParam")
public class CompanyFinancialInfoParam extends CompanyFinancialInfo {

    PagingParam pagingParam = new PagingParam();


}
