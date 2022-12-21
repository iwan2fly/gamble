package kr.co.glog.domain.stat.common.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("StatBasisDate")
public class StatBasisDate extends EntityModel {

    String  statDate;
    String  statDateName;
    String  statDateDesc;

}
