package kr.co.glog.app.web.rest.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.glog.domain.member.entity.Member;
import kr.co.glog.repository.master.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/domain/member")
public class MemberController {

    private final MemberRepository memberRepository;

    // http://localhost:8088/jpa/members
    // 전체 데이터 Return (READ)
    @GetMapping("/members")
    public List<Member> members(HttpServletResponse response, String memberName ) throws JsonProcessingException {
       // return memberRepository.findByName( memberName );
        return null;
    }

    /*
    //해당 ID 데이터 Return (READ)
    @GetMapping("members/{id}")
    public EntityModel<Member> member(@PathVariable int id) {
        Optional<Member> user = memberRepository.findById(id);
        if(!user.isPresent()) {
            throw new MemberNotFoundException(String.format("ID[%s] not found",id));
        }

        //HATEOAS
        EntityModel<Member> model = new EntityModel<>(user.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllMembers());
        model.add(linkTo.withRel("all-members"));
        return model;

    }

    // Data 삭제 (DELETE)
    @DeleteMapping("/members/{id}")
    public void deleteMember(@PathVariable int id) {
        memberRepository.deleteById(id);
    }
    */

    // 데이터 추가 (CREATE)
    @PostMapping("/add")
    // public ResponseEntity<Member> add(@Valid @RequestBody Member member) {
    public ResponseEntity<Member> add( @RequestBody Member member ) {
        log.debug( member.toString() );

        Member savedMember = memberRepository.save( member );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMember.getMemberId())
                .toUri();

        return ResponseEntity.created(location).build();


    }

}
