package kr.co.glog.app.web.auth;

import kr.co.glog.domain.member.model.MemberResult;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
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
        List<GrantedAuthority> authorities = new ArrayList<>();
        memberResult.getRoleList().forEach(role -> addAuthority(authorities, role));
        return authorities;
    }

    private static void addAuthority(List<GrantedAuthority> authorities, String role) {
        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        authorities.add(new SimpleGrantedAuthority(role));
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
