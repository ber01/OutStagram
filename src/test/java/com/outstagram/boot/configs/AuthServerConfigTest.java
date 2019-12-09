package com.outstagram.boot.configs;

import com.outstagram.boot.member.Member;
import com.outstagram.boot.member.MemberRole;
import com.outstagram.boot.member.MemberService;
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthServerConfigTest {

    @Autowired
    MemberService memberService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Description("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        String email = "authTest@test.com";
        String password = "authPassword";

        Mono.just(Member.builder()
                .email(email)
                .username("authTest")
                .password(password)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(MemberRole.ADMIN, MemberRole.USER))
                .build()).flatMap(memberService::saveMember).subscribe();

        String clientId = "myApp";
        String clientSecret = "pass";

        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        fromData.add("username", email);
        fromData.add("password", password);
        fromData.add("grant_type", "password");

        webTestClient
                .mutate().filter(basicAuthentication(clientId, clientSecret)).build()
                .post()
                    .uri("/oauth/token")
                    .body(BodyInserters.fromFormData(fromData))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("access_token").exists()
        ;

        /*
        mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId, clientSecret))
                    .param("username", email)
                    .param("password", password)
                    .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
        ;
        */
    }
}