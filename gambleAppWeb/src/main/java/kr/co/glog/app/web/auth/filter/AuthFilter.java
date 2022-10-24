package kr.co.glog.app.web.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.app.web.auth.CustomUserDetails;
import kr.co.glog.app.web.auth.config.JwtTokenProvider;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.member.model.MemberParam;
import kr.co.glog.domain.member.model.MemberResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static kr.co.glog.app.web.auth.config.SecurityConfig.LOGIN_API;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        super(authenticationManager);
        setFilterProcessesUrl(LOGIN_API);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        MemberParam member = objectMapper.readValue(request.getInputStream(), MemberParam.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPwd(), null);
        return getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();
        MemberResult memberResult = user.getMemberResult();
        String jwtToken = jwtTokenProvider.createToken(memberResult.getMemberId(), memberResult.getEmail(), memberResult.getRoles());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        memberResult.setPwd(null);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(new RestResponse().putData("member", memberResult)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("인증실패 - 에러를 리턴하자");
        // TODO 에러를 리턴하자
    }

    /*@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 유효한 토큰인지 확인합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            // SecurityContext 에 Authentication 객체를 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }*/

}
