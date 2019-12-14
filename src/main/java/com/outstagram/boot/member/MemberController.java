package com.outstagram.boot.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping
    public Flux<Member> getMembers(@CurrentMember Member member) {
        if (member != null) {
            System.out.println(member.getId());
        }
        return memberRepository.findAll();
    }

    @PostMapping
    public Mono<Member> createMember(@RequestBody Member member) {
        return memberService.createMember(member);
    }

}
