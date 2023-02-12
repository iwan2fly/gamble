package kr.co.glog.app.web.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.app.web.auth.config.JwtTokenProvider;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.model.MemberResult;
import kr.co.glog.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/rest/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${server.servlet.session.timeout}")
    private int sessionTimeout;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @PostMapping("/redis/login")
    public Member login(@RequestBody Member member, HttpSession session) {
        Member authMember = memberService.loginMember(member.getEmail(), member.getPwd());
        log.debug("### authMember: {}", authMember);

        session.setAttribute(authMember.getEmail(), objectMapper.writeValueAsString(authMember));
        session.setMaxInactiveInterval(sessionTimeout);
        return authMember;
    }

    @SneakyThrows
    @GetMapping("/member")
    public MemberResult getUser(@RequestParam String email, HttpSession session) {
        Object sessionAttribute = session.getAttribute(email);
        if (sessionAttribute == null) throw new AccountExpiredException("세션 만료");

        String authMemberJsonString = (String) sessionAttribute;
        log.debug("### authMemberJsonString: {}", authMemberJsonString);

        MemberResult authMember = objectMapper.readValue(authMemberJsonString, MemberResult.class);
        return authMember;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Member member) {
        Member savedMember = memberService.registerMember(member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success(savedMember));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity onlyAdmin() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.success(Collections.singletonMap("who", "ADMIN 만 호출이 가능합니다.")));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager")
    public ResponseEntity onlyManager() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.success(Collections.singletonMap("who", "MANAGER 만 호출이 가능합니다.")));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity onlyUser() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.success(Collections.singletonMap("who", "USER 만 호출이 가능합니다.")));
    }

    // 로그인
    /*@PostMapping("/login")
    public RestResponse login(@RequestBody Member member) {
        log.debug( member.toString() );
        RestResponse restResponse = new RestResponse();

        MemberResult memberResult = memberService.loginMember( member.getEmail(), member.getPwd() );
        String token = jwtTokenProvider.createToken(member.getMemberId(), memberResult.getRoles() );

        restResponse.putData( "token", token );
        return restResponse;
    }*/

    /*
    // * AccessToken이 만료되었을 때 토큰(AccessToken , RefreshToken)재발급해주는 메서드
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(
            @RequestBody @Valid TokenDto requestTokenDto) {
        TokenDto tokenDto = authService
                .reissue(requestTokenDto.getAccessToken(), requestTokenDto.getRefreshToken());
        return ResponseEntity.ok(tokenDto);
    }


    //   로그아웃을 했을 때 토큰을 받아 BlackList에 저장하는 메서드
    @DeleteMapping("/authenticate")
    public ResponseEntity<Void> logout(
            @RequestBody @Valid TokenDto requestTokenDto) {
        authService.logout(requestTokenDto.getAccessToken(), requestTokenDto.getRefreshToken());
        return null;
    }

     */
}
