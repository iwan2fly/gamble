package kr.co.glog.app.web.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.co.glog.app.web.auth.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String SECRET_KEY = "32charormoreneeds32charormoreneeds";

    // 액세스 토큰 유효시간 30분
    private long accessTokenValidTime = 30 * 60 * 1000;

    // 리프레시 토큰 유효시간 7일
    private long refreshTokenValidTime = 60 * 60 * 1000 * 24 * 7;
    // private long tokenValidTime = 3000; // 3초 - 토큰 만료 테스트

    private final UserDetailsServiceImpl userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        log.debug( "---- JwtTokenProvider.init()");
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    // JWT 토큰 생성
    public String createAccessToken(Long memberId, String email, String roles) {
        log.debug( "---- createAccessToken()");
        Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보단위
        claims.put("username", email); // 정보는 key / value 쌍으로 저장된다.
        claims.put("memberId", memberId); // 정보는 key / value 쌍으로 저장된다.
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    public String createRefreshToken(Long memberId, String email, String roles) {
        log.debug( "---- createRefreshToken()");
        Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보단위
        claims.put("username", email); // 정보는 key / value 쌍으로 저장된다.
        claims.put("memberId", memberId); // 정보는 key / value 쌍으로 저장된다.
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        log.debug( "---- JwtTokenProvider.getAuthentication()");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getMemeberId(String token) {
        Claims body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return body.get("memberId").toString();
    }

    public String getUsername(String token) {
        Claims body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return body.get("username").toString();
    }

    public String getRoles(String token) {
        Claims body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return body.get("roles").toString();
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        log.debug( "---- JwtTokenProvider.resolveToken()");
        return request.getHeader("X-AUTH-TOKEN");
    }
    // 토큰의 유효성 + 만료일자 확인

    public boolean validateToken(String jwtToken) {
        log.debug( "---- JwtTokenProvider.validateToken()");

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
