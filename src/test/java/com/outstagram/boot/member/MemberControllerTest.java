package com.outstagram.boot.member;

import jdk.jfr.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class MemberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Description("회원 전체를 불러오는 테스트")
    public void findMemberAll() {
        webTestClient.get()
                .uri("/api/members")
                .exchange()
        ;
    }

}