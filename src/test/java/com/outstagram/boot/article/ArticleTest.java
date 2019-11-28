package com.outstagram.boot.article;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
 public class ArticleTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    @Description("Article 객체 생성 테스트")
    public void init_data() {
        Article article = Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test")
                .slug("test")
                .description("testDes")
                .body("testBody")
                .createdAt(LocalDateTime.now())
                .build();

        assertThat(article).isNotNull();
        assertThat(article.getTitle()).isEqualTo("test");
        assertThat(article.getImage()).isNull();
    }

    @Test
    @Description("데이터베이스 Read 테스트")
    public void read_article_data() {
        String title = "test";
        Flux<Article> flux = articleRepository.findByTitle(title);
        List<Article> articles = flux.collectList().block();

        System.out.println(articles);

    }

}