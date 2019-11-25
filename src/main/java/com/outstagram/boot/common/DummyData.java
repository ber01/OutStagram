package com.outstagram.boot.common;

import com.outstagram.boot.member.Member;
import com.outstagram.boot.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        memberRepository.deleteAll()
                .thenMany(
                        Flux.just(
                                Member.builder()
                                    .email("test@email.com")
                                    .username("testuser")
                                    .password("pass")
                                    .createdAt(LocalDateTime.now())
                                    .build(),
                                Member.builder()
                                        .email("test2@email.com")
                                        .username("testuser2")
                                        .password("pass")
                                        .createdAt(LocalDateTime.now())
                                        .build(),
                                Member.builder()
                                        .email("test3@email.com")
                                        .username("testuser3")
                                        .password("pass")
                                        .createdAt(LocalDateTime.now())
                                        .build()
                        ).flatMap(memberRepository::save)
                ).subscribe(System.out::println);
    }
}
