package kr.co.glog.domain.system.model;

import kr.co.glog.domain.system.entity.SystemConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("SystemConfigResult")
public class SystemConfigResult extends SystemConfig {
}
