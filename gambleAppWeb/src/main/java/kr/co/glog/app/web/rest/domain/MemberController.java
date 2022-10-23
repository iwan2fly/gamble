package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.common.model.RestResponse;
import kr.co.glog.domain.member.dao.MemberDao;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.model.MemberParam;
import kr.co.glog.domain.member.model.MemberResult;
import kr.co.glog.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberDao memberDao;

    // 특정 데이터 리턴
    @GetMapping("/get")
    public RestResponse get(Long memberId, @AuthenticationPrincipal Member currentUser) throws JsonProcessingException {
        log.debug("### currentUser: {}", currentUser);

        MemberResult memberResult = memberDao.getMember(memberId);
        return new RestResponse().putData("member", memberResult);
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(MemberParam memberParam) throws JsonProcessingException {
        ArrayList<MemberResult> memberList = memberDao.getMemberList(memberParam);
        return new RestResponse().putData("memberList", memberList);
    }

    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(@RequestBody Member member) throws JsonProcessingException {
        Member savedMember = memberDao.saveMember(member);
        return new RestResponse().putData("member", savedMember);
    }

    // 데이터 삭제
    @PostMapping("/delete")
    public RestResponse delete(Long memberId) throws JsonProcessingException {
        memberDao.deleteMember(memberId);
        return new RestResponse();
    }
}
