package kr.co.glog.domain.member.entity;

import kr.co.glog.domain.EntityModel;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@ToString(callSuper=true)
// @AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="member")
public class Member extends EntityModel {

    @Id
    @Column(nullable = false, unique = true)
    private Long memberId;

    @Column(nullable = false)
    String memberName;


    private String pwd;


    private String email;


    private Timestamp lastLoginTime;


    private Timestamp lastLogoutTime;


    private Timestamp lastPwdUpdateTime;


    private Short authFailCount;


    private String memberStatusCode ;
}
