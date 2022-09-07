package kr.co.glog.app.web.auth.service;

import kr.co.glog.app.web.auth.CustomUserDetails;
import kr.co.glog.domain.member.dao.MemberDao;
import kr.co.glog.domain.member.model.MemberResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberDao memberDao;

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {

        log.debug( "[call] loadUserByUsername.loadByUserName" );
        log.debug( "[call] email = " + email );

        MemberResult memberResult = memberDao.getMember( email );
        if ( memberResult == null ) throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");

        return new CustomUserDetails( memberResult );
    }
}
