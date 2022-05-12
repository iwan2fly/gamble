package kr.co.glog.repository.master;

import kr.co.glog.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {

}
