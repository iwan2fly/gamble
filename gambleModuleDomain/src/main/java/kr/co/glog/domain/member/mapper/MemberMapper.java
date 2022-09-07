package kr.co.glog.domain.member.mapper;

import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.domain.member.model.MemberParam;
import kr.co.glog.domain.member.model.MemberResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface MemberMapper {
    public int insertMember( Member member );									    // 등록
    public int updateMember( Member member );									    // 수정
    public int deleteMember( Member member );									    // 삭제
    public ArrayList<MemberResult> selectMemberList(MemberParam memberParam);		// 목록
    public int selectMemberListCount(MemberParam memberParam);					    // 목록건수
}
