package kr.co.glog.app.web.rest;

import kr.co.glog.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/rest/dummy")
@RequiredArgsConstructor
public class DummyController {

    @GetMapping("/m1")
    public String m1(HttpServletRequest request, HttpServletResponse response ) {
        log.debug( " ::: m1" );
        return "m1";
    }


}
