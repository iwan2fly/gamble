package kr.co.glog.app.web.rest.auth;

import kr.co.glog.app.web.auth.config.JwtTokenProvider;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.model.MemberResult;
import kr.co.glog.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rest/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;


    // 회원가입
    @PostMapping("/register")
    public RestResponse register(@RequestBody Member member) {
        RestResponse restResponse = new RestResponse();

        member = memberService.registerMember(member);

        restResponse.putData("member", member);
        return restResponse;
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
