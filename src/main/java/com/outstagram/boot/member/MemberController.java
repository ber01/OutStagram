package com.outstagram.boot.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
    public Mono<ResponseEntity<Member>> save(@RequestBody @Valid Member member) {
        Mono<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail.block() != null) {
            return Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        }

        return memberService.save(member)
                .map(saveMember -> new ResponseEntity<>(saveMember, HttpStatus.CREATED));
    }
}
