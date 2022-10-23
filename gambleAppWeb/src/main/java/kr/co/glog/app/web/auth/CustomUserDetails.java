package kr.co.glog.app.web.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.glog.domain.member.model.MemberResult;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CustomUserDetails extends MemberResult implements UserDetails {

    private MemberResult memberResult;
    private String username;
    private String password;

    public CustomUserDetails(MemberResult memberResult) {
        this.memberResult = memberResult;
        this.username = memberResult.getEmail();
        this.password = memberResult.getPwd();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        memberResult.getRoleList().forEach(r -> {
            authorities.add(() -> r);
        });
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
