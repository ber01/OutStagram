package com.outstagram.boot.article;

import com.outstagram.boot.common.AppProperties;
import com.outstagram.boot.member.Member;
import com.outstagram.boot.member.MemberRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ArticleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    private AppProperties appProperties;

    private final static String BEARER = "BEARER ";

    final String url = "/api/articles";

    @Before
    public void setUp() {
        this.articleRepository.deleteAll();

        Member member = createMember();
        webTestClient.post()
                .uri("/api/members")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(member), Member.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Description("정상적으로 게시글 생성하는 테스트")
    public void createArticle() {
        Article article = Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test")
                .description("It is test")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();

        Member member = createMember();
        webTestClient.post()
                .uri("/api/members")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(member), Member.class)
                .exchange()
                .expectStatus().isOk();
        article.setMemberId(member.getId());

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty();

        Mono<Article> articleMono = this.articleRepository.findById(article.getId());

        StepVerifier.create(articleMono)
                .assertNext(i -> assertThat(article).isNotNull())
                .verifyComplete();
    }

    @Test
    @Description("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createArticle_Bad_Request_Empty_Input() {
        Article article = new Article();

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }

    @Test
    @Description("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createArticle_Bad_Request_Wrong_Input() {
        Article article = new Article();
        article.setTitle("");

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();
    }

    @Test
    @Description("정상적으로 모든 게시물들을 보여주는 테스트")
    public void getAllArticles() {
        IntStream.range(0, 30).forEach(this::generateArticle);

        webTestClient.get().uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Article.class);
    }

    private Mono<Article> generateArticle(int index) {
        Article article = create(index);
        return articleRepository.save(article);
    }

    @Test
    @Description("정상적으로 게시물을 보여주는 테스트")
    public void getArticle() {
        Article article = Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test")
                .description("It is test")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();
        String memberId = createMember().getId();
        articleService.create(article, memberId);

        webTestClient.get().uri(url + "/" + article.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Description("정상적으로 게시물을 수정하는 테스트")
    public void updateArticle() {
        Article article = Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test")
                .description("It is test")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();
        Member member = this.createMember();

        webTestClient.post()
                .uri("/api/members")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(member), Member.class)
                .exchange()
                .expectStatus().isOk();

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();

        String updatedTitle = "Update Test";
        Article updatedArticle = new Article();
        updatedArticle.setTitle(updatedTitle);
        updatedArticle.setUpdatedAt(LocalDateTime.now());

        webTestClient.put().uri(url + "/" + article.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(updatedArticle), Article.class)
                .exchange()
                .expectStatus().isOk();

        Mono<Article> updatedArticleMono = articleRepository.findById(article.getId());

        StepVerifier.create(updatedArticleMono)
                .assertNext(i -> assertThat(i.getTitle()).isEqualTo(updatedTitle))
                .verifyComplete();
    }

    private Member createMember() {
        return Member.builder()
                .email("test@email.com")
                .username("testuser")
                .password("pass")
                .createdAt(LocalDateTime.now())
                .roles(Set.of(MemberRole.ADMIN, MemberRole.USER))
                .build();
    }

    @Test
    @Description("정상적으로 게시물을 삭제하는 테스트")
    public void deleteArticle() {
        Article article = Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test")
                .description("It is test")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody();

        webTestClient.delete().uri(url + "/" + article.getId())
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .exchange()
                .expectStatus().isOk();
    }

    private Article create(int index) {
        return Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test" + index)
                .description("It is test")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();
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