package com.outstagram.boot.member;

import com.outstagram.boot.common.AppProperties;
import jdk.jfr.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class MemberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AppProperties appProperties;

    private final static String BEARER = "BEARER ";

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
        webTestClient.get()
                .uri("/api/members")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .exchange()
                .expectStatus().isOk()
        ;
    }

    @Test
    @Description("정상적으로 회원가입이 성공하는 테스트")
    public void createMember() {
        webTestClient.post()
                .uri("/api/members")
                .exchange()
                .expectStatus().isOk();
    }

    private String getAccessToken() {
        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        fromData.add("username", appProperties.getTestUsername());
        fromData.add("password", appProperties.getTestPassword());
        fromData.add("grant_type", "password");

        FluxExchangeResult<String> result = webTestClient
                .mutate().filter(basicAuthentication(appProperties.getClientId(), appProperties.getClientSecret())).build()
                .post()
                    .uri("/oauth/token")
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