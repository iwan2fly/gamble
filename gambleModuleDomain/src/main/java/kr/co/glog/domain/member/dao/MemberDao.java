package kr.co.glog.domain.member.dao;

import kr.co.glog.common.exception.ParameterMissingException;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.mapper.MemberMapper;
import kr.co.glog.domain.member.model.MemberParam;
import kr.co.glog.domain.member.model.MemberResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberDao {

    private final MemberMapper memberMapper;

    public MemberResult getMember( Long memberId ) {
        if ( memberId == null ) throw new ParameterMissingException( "memberId" );

        MemberResult memberResult = null;
        MemberParam memberParam = new MemberParam();
        memberParam.setMemberId( memberId );
        ArrayList<MemberResult> memberList = memberMapper.selectMemberList( memberParam );
        if ( memberList != null && memberList.size() > 0 ) memberResult = memberList.get(0);
        return memberResult;
    }

    public MemberResult getMember( String email ) {
        if ( email == null ) throw new ParameterMissingException( "email" );

        MemberResult memberResult = null;
        MemberParam memberParam = new MemberParam();
        memberParam.setEmail( email );
        ArrayList<MemberResult> memberList = memberMapper.selectMemberList( memberParam );
        if ( memberList != null && memberList.size() > 0 ) memberResult = memberList.get(0);
        return memberResult;
    }

    public ArrayList<MemberResult> getMemberList( MemberParam memberParam ) {
        if ( memberParam == null ) throw new ParameterMissingException( "memberParam" );

        return memberMapper.selectMemberList( memberParam );
    }

    public Member saveMember( Member member ) {
        if ( member == null ) throw new ParameterMissingException( "Member" );

        if ( member.getMemberId() == null ) {
            memberMapper.insertMember( member );
        } else {
            memberMapper.updateMember( member );
        }

        return member;
    }

    public void deleteMember( Long memberId ) {
        if ( memberId == null ) throw new ParameterMissingException( "memberId" );

        MemberParam memberParam = new MemberParam();
        memberParam.setMemberId( memberId );
        memberMapper.deleteMember( memberParam );
    }




}
