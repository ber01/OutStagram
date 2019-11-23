package com.outstagram.boot.member;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
class MemberTest {

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

}