package kr.co.glog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.glog.domain.member.dao.MemberDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String LOGIN_API = "/rest/auth/login";
    private final MemberDao memberDao;

    @Bean
    public SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
        return webSecurityExpressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

        /**
         * ADMIN 은 MANAGER, USER 의 권한도 포함됨
         * MANAGER 는 USER 의 권한도 포함됨
         */
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
        return roleHierarchy;
    }

    // 정적 자원에 대해서는 Security 설정을 적용하지 않음.
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/resources/static/**")
                .and()
                .expressionHandler(webExpressionHandler())
        ;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        log.debug( "::: SecurityConfig.configure HttpSerucity" );

        httpSecurity
                .headers(AbstractHttpConfigurer::disable)
                .csrf().disable()       // csrf 보안 토큰 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                .mvcMatchers(HttpMethod.GET, "/auth/login").permitAll() // 화면은 모두 open 되어야 할 것
                .mvcMatchers(HttpMethod.POST, LOGIN_API).permitAll() // 회원 인증
                .mvcMatchers(HttpMethod.POST, "/rest/auth/register").permitAll() // 회원 가입
                .antMatchers("/rest/auth/**").permitAll() // 회원 가입
                .antMatchers("/m/**").hasRole("ADMIN")
                .antMatchers("**/tx/**").hasRole("USER")
//                .anyRequest().authenticated()
                .anyRequest().permitAll()
                .expressionHandler(webExpressionHandler())
                //.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
        ;
    }
}
