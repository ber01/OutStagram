package com.outstagram.boot.member;

import com.outstagram.boot.common.AppProperties;
import jdk.jfr.Description;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class MemberControllerTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AppProperties appProperties;

    private final static String BEARER = "BEARER ";
    private final static String EMAIL = "minkh@gmail.com";
    private final static String USERNAME = "khmin";
    private final static String PASSWORD = "!Ab123456";

    @Test
    @Description("회원 전체를 불러오는 테스트")
    public void findAllMembers() {
        webTestClient.get()
                .uri("/api/members")
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    @Description("회원 전체를 불러오는 테스트 with 인증")
    public void findAllMembersWithAuthentication() {
        webTestClient
                .get().uri("/api/members")
                    .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .exchange()
                .expectStatus()
                    .isOk()
        ;
    }

    @Test
    @Description("정상적으로 회원가입이 성공하는 테스트")
    public void createMember() {
        Member member = Member.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        webTestClient
                .post().uri("/api/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(member), Member.class)
                .exchange()
                .expectStatus()
                    .isCreated()
                .expectHeader()
                    .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                    .jsonPath("id").exists()
                    .jsonPath("email").isEqualTo(EMAIL)
                    .jsonPath("username").isEqualTo(USERNAME)
        ;
    }

    @Test
    @Parameters
    @Description("회원가입이 실패하는 테스트")
    public void createMemberFail(String email, String username, String password) {
        Member member = Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        webTestClient
                .post().uri("/api/members")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(Mono.just(member), Member.class)
                .exchange()
                .expectStatus()
                    .isBadRequest()
        ;
    }

    private Object[] parametersForCreateMemberFail() {
        return new Object[] {
                new Object[]{"", "", ""}, // email, username, password 입력 x
                new Object[]{"", USERNAME, PASSWORD}, // email 입력 x
                new Object[]{EMAIL, "", PASSWORD}, // username 입력 x
                new Object[]{EMAIL, USERNAME, ""}, // password 입력 x
                new Object[]{"fail", USERNAME, PASSWORD}, // email 형식 오류
                new Object[]{EMAIL, "f", PASSWORD}, // username 형식 오류
                new Object[]{EMAIL, USERNAME, "12345"} // password 형식 오류
        };
    }

    private String getAccessToken() {
        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        fromData.add("username", appProperties.getTestUsername());
        fromData.add("password", appProperties.getTestPassword());
        fromData.add("grant_type", "password");

        FluxExchangeResult<String> result = webTestClient
                .mutate().filter(basicAuthentication(appProperties.getClientId(), appProperties.getClientSecret())).build()
                .post().uri("/oauth/token")
                    .body(BodyInserters.fromFormData(fromData))
                .exchange()
                .returnResult(String.class);

        System.out.println(result);

        String resultS = result.toString();
        int start = resultS.indexOf("{");
        int end = resultS.indexOf("}");
        String content = resultS.substring(start, end + 1);

        Jackson2JsonParser parser = new Jackson2JsonParser();
        return BEARER + parser.parseMap(content).get("access_token").toString();

        /*
        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", email)
                .param("password", password)
                .param("grant_type", "password"));

        String contentAsString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(contentAsString).get("access_token").toString();
        */
    }

}