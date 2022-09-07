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
    public RestResponse get(HttpServletRequest request, HttpServletResponse response, Long memberId) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        MemberResult memberResult = memberDao.getMember( memberId );

        restResponse.putData( "member", memberResult );
        return restResponse;
    }


    // 목록 데이터 리턴
    @GetMapping("/getList")
    public RestResponse getList(HttpServletRequest request, HttpServletResponse response, MemberParam memberParam ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        ArrayList<MemberResult> memberList = memberDao.getMemberList( memberParam );

        restResponse.putData( "memberList", memberList );
        return restResponse;
    }

    // 데이터 변경
    @PostMapping("/save")
    public RestResponse save(HttpServletRequest request, HttpServletResponse response, @RequestBody Member member ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        Member savedMember = memberDao.saveMember( member );

        restResponse.putData( "member", savedMember );
        return restResponse;
    }

    // 데이터 삭제
    @PostMapping("/delete")
    public RestResponse delete(HttpServletRequest request, HttpServletResponse response, Long memberId ) throws JsonProcessingException {
        RestResponse restResponse = new RestResponse();

        memberDao.deleteMember( memberId );

        return restResponse;
    }


}
