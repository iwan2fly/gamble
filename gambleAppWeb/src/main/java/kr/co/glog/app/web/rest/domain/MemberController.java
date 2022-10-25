package kr.co.glog.app.web.rest.domain;

import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.member.dao.MemberDao;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.model.MemberParam;
import kr.co.glog.domain.member.model.MemberResult;
import kr.co.glog.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberDao memberDao;

    // 특정 데이터 리턴
    @GetMapping("/{memberId}")
    public ResponseEntity getMember(@PathVariable Long memberId, @AuthenticationPrincipal Member currentUser) {
        log.debug("### currentUser: {}", currentUser);
        MemberResult member = memberDao.getMember(memberId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.success(member));
    }


    // 목록 데이터 리턴
    @GetMapping
    public ResponseEntity getMemberList(MemberParam memberParam, @AuthenticationPrincipal Member currentUser) {
        log.debug("### currentUser: {}", currentUser);
        List<MemberResult> memberList = memberDao.getMemberList(memberParam);
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.success(Collections.singletonMap("list", memberList)));
    }

    // 데이터 변경
    @PostMapping
    public ResponseEntity saveMember(@Valid @RequestBody Member member, @AuthenticationPrincipal Member currentUser) {
        log.debug("### currentUser: {}", currentUser);
        log.debug("### member: {}", member);
        Member savedMember = memberService.registerMember(member);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success(savedMember));
    }

    // 데이터 삭제
    @DeleteMapping("/{memberId}")
    public ResponseEntity deleteMember(@PathVariable Long memberId, @AuthenticationPrincipal Member currentUser) {
        log.debug("### currentUser: {}", currentUser);
        memberDao.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(RestResponse.success());
    }
}
