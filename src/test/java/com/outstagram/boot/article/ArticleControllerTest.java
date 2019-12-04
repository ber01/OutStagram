package com.outstagram.boot.article;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ArticleControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ArticleRepository articleRepository;

    final String url = "/api/articles";

    @Test
    @Description("정상적으로 게시글 생성하는 테스트")
    public void createArticle() {
        Article article = Article.builder()
                .id(UUID.randomUUID().toString())
                .slug("test")
                .title("test")
                .description("It is test")
                .body("test body")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();

        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(article), Article.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isNotEmpty();

        Mono<Article> articleMono = articleRepository.findById(article.getId());

        StepVerifier.create(articleMono)
                .assertNext(i -> assertThat(article).isNotNull())
                .verifyComplete();
    }

    private Article create(int index) {
        return Article.builder()
                .id(UUID.randomUUID().toString())
                .slug("test" + index)
                .title("test" + index)
                .description("It is test")
                .body("test body")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();
    }

    @Test
    @Description("정상적으로 모든 게시물들을 보여주는 테스트")
    public void getAllArticles() {
        IntStream.range(0, 30).forEach(this::generateArticle);

        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk();
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
                .slug("test")
                .title("test")
                .description("It is test")
                .body("test body")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();

        webTestClient.get().uri(url + "/" + article.getId())
                .exchange()
                .expectStatus().isOk();
    }
}