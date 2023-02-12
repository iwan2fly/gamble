package kr.co.glog.domain.member.model;

import kr.co.glog.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper=true)
@Alias("MemberResult")
public class MemberResult extends Member {
    private List<String> roleList = new ArrayList<>();
}
