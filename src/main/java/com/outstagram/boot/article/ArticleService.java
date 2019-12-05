package com.outstagram.boot.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Mono<Article> create(Article article) {
        return articleRepository.save(article);
    }

    public Flux<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Mono<ResponseEntity<Article>> getArticleById(String id) {
        return articleRepository.findById(id)
                .map(savedArticle -> ResponseEntity.ok(savedArticle))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
