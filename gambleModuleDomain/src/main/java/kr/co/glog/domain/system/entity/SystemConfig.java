package kr.co.glog.domain.system.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=false)
@Alias("SystemConfig")
public class SystemConfig extends EntityModel {

    Long    systemConfigId;
    String  configCode;
    String  configJson;

}
