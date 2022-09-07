package kr.co.glog.app.web.auth;

import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.model.MemberResult;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails extends MemberResult implements UserDetails {

    private MemberResult memberResult;

    public CustomUserDetails( MemberResult memberResult ) {
        this.memberResult = memberResult;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        memberResult.getRoleList().forEach( r-> {
            authorities.add(()->r);
        });

        return authorities;
    }


    @Override
    public String getPassword() {
        return memberResult.getPwd();
    }

    @Override
    public String getUsername() {
        return memberResult.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
