package com.outstagram.boot.member;

import com.outstagram.boot.common.AppProperties;
import jdk.jfr.Description;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class LoginControllerTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AppProperties appProperties;

    private Member member;

    @Before
    @Description("저장 된 회원 불러오기")
    public void loadMember() {
        this.member = memberRepository.findByEmail(appProperties.getTestUsername()).block();
    }

    @Test
    @Parameters
    @Description("로그인이 실패하는 테스트")
    public void loginFail(String email, String password) {
        LoginDto loginDto = LoginDto.builder()
                .email(email)
                .password(password)
                .build();

        webTestClient
                .post().uri("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(loginDto), LoginDto.class)
                .exchange()
                .expectStatus()
                    .isBadRequest()
        ;
    }

    public Object[] parametersForLoginFail() {
        return new Object[] {
                new Object[] {"", ""}, // 이메일, 비밀번호 입력 X
                new Object[] {"", "!Ab12345"}, // 이메일 입력 X
                new Object[] {"ok@email.com", ""}, // 비밀번호 입력 X
                new Object[] {"fail", "!Ab12345"}, // 이메일 형식 X
                new Object[] {"ok@email.com", "12345"}, // 비밀번호 형식 X
                new Object[] {"ok@email.com", "!Ab12345"} // 존재하지만 비밀번호가 다른 경우
        };
    }

    @Test
    @Description("로그인이 성공하는 테스트")
    public void loginSuccess() {
        LoginDto loginDto = LoginDto.builder()
                .email(appProperties.getTestUsername())
                .password(appProperties.getTestPassword())
                .build();

        webTestClient
                .post().uri("/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(loginDto), LoginDto.class)
                .exchange()
                .expectStatus()
                    .isOk()
        ;
    }

}