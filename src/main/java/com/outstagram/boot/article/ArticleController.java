package com.outstagram.boot.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Mono<Article> createArticle(@RequestBody Article article) {
        return articleService.create(article);
    }

    @GetMapping
    public Flux<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public Mono<Article> getArticleById(@PathVariable(value = "id") String id) {
        return articleService.getArticleById(id);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Article>> updateArticle(@PathVariable(value = "id") String id, @RequestBody Article article) {
        return articleService.updateArticle(id, article);
    }

}
