package kr.co.glog.domain.member.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@ToString(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Alias("Member")
public class Member {
    private Long memberId;
    private String memberName;
    private String pwd;
    private String email;
    private String roles;
    private Timestamp lastLoginTime;
    private Timestamp lastLogoutTime;
    private Timestamp lastPwdUpdateTime;
    private Short authFailCount;
    private String memberStatusCode ;

    public ArrayList<String> getRoleList() {
        ArrayList<String> roleList = new ArrayList<String>();
        if ( this.roles != null ) {
            roleList = (ArrayList)Arrays.asList(this.roles.split(",") );
        }

        return roleList;
    }
}
