package com.outstagram.boot.member;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void fidByEmail() {
        String email = "kyunghwan@test.com";
        String password = "password";

        Mono.just(Member.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .password(password)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(MemberRole.ADMIN, MemberRole.USER))
                .build()).flatMap(memberRepository::save).subscribe();

        UserDetailsService userDetailsService = memberService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

}