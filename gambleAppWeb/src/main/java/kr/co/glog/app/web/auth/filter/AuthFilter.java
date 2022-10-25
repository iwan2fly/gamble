package kr.co.glog.app.web.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.app.web.auth.CustomUserDetails;
import kr.co.glog.app.web.auth.config.JwtTokenProvider;
import kr.co.glog.common.exception.ErrorCode;
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
        response.getOutputStream().write(objectMapper.writeValueAsBytes(RestResponse.success(memberResult)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // TODO 회원이 없는 경우에도 이곳을 타므로 그런거 나중에 처리해줘야 함

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(RestResponse.fail(ErrorCode.COMMON_UNAUTHORIZED)));
    }
}
