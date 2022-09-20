package kr.co.glog.domain.stock.entity;

import kr.co.glog.common.model.EntityModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=false)
@Alias("DartCorp")
public class DartCorp extends EntityModel {
    String  corpCode;
    String  corpName;
    String  stockCode;
    String  modifyDate;
}
