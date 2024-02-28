package kr.co.glog.domain.system.model;

import kr.co.glog.common.model.PagingParam;
import kr.co.glog.domain.system.entity.SystemConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("SystemConfigParam")
public class SystemConfigParam extends SystemConfig {

    PagingParam pagingParam = new PagingParam();


}
