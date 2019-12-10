package com.outstagram.boot.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping
    public Flux<Member> getMembers() {
        return memberRepository.findAll();
    }

    @PostMapping
    public void testMember() {
        System.out.println("테스트용");
    }

}
