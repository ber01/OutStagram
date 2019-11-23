package com.outstagram.boot.member;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
        System.out.println(flux);
    }
}