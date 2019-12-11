package com.outstagram.boot.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Mono<Article> createArticle(@RequestBody @Valid ArticleDto articleDto, Errors errors) {
        if (errors.hasErrors()) {
            return Mono.error((Throwable) errors);
        }
        return articleService.create(articleDto);
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

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteArticle(@PathVariable(value = "id") String id) {
        return articleService.deleteArticle(id);
    }
}
