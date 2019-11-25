package com.outstagram.boot.member;

import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Description("Member 객체 생성 테스트")
    public void init_test() {
        Member member = Member.builder()
                .id(UUID.randomUUID().toString())
                .email("test@test.com")
                .username("test")
                .password("password")
                .image("/url/url.com")
                .bio("test bio")
                .createdAt(LocalDateTime.now())
                .build();

        assertThat(member).isNotNull();
        assertThat(member.getUsername()).isEqualTo("test");
        assertThat(member.getUpdatedAt()).isNull();
    }

    @Test
    @Description("데이터베이스 Read 테스트")
    public void read_member_data() {
        Flux<Member> flux = memberRepository.findAll();
        Mono<List<Member>> listMono = flux.collectList();
        List<Member> block = listMono.block();

        System.out.println("1번 : " + listMono);
        System.out.println("2번 : " + block);
    }

    @Test
    @Description("하나의 Member 객체를 저장 후 불러오는 테스트")
    public void save_read_test() {
        String email = "email@email.com";

        Mono.just(Member.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .password("testPasswordPassword")
                .createdAt(LocalDateTime.now())
                .build()).flatMap(memberRepository::save).subscribe();

        Mono<Member> byEmail = memberRepository.findByEmail(email);
        Member member = byEmail.block();

        log.info(String.valueOf(member));

        assertThat(member != null ? member.getEmail() : null).isEqualTo(email);
    }
}