package kr.co.glog.domain.service;

import kr.co.glog.domain.member.dao.MemberDao;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.exception.InvalidPasswordException;
import kr.co.glog.domain.member.exception.UnregisteredEmailException;
import kr.co.glog.domain.member.model.MemberResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 *  회원 관련 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    // 암호화에 필요한 PasswordEncoderImpl 를 Bean 등록합니다.
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원아이디로 회원이 존재하는지 여부 체크
     * * @param memberId
     * @return
     */
    public boolean isExistMember( Long memberId ) {

        boolean result  = false;

        MemberResult memberResult = memberDao.getMember( memberId );
        if ( memberResult != null )  result = true;

        return result;
    }

    /**
     * 회원 등록
     * @param member
     */
    public Member registerMember( Member member ) {

        member.setPwd( passwordEncoder.encode( member.getPwd() ) );
        member.setRoles("ROLE_USER");
        return memberDao.saveMember( member );

    }

    /**
     *  회원 로그인
     * @param email
     * @param password
     * @return
     */
    public MemberResult loginMember( String email, String password ) {
        MemberResult memberResult = memberDao.getMember( email );
        if ( memberResult == null ) throw new UnregisteredEmailException();

        if ( !passwordEncoder.matches( password, memberResult.getPwd() ) ) {
            throw new InvalidPasswordException();
        }

        return memberResult;
    }
}
