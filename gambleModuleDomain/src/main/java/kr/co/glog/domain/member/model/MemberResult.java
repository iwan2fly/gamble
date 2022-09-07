package kr.co.glog.domain.member.model;

import kr.co.glog.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("MemberResult")
public class MemberResult extends Member {
}
