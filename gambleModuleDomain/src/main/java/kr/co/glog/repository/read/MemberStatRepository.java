package kr.co.glog.repository.read;

import kr.co.glog.domain.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberStatRepository extends CrudRepository<Member, Long> {

}
