package com.outstagram.boot.article;

import com.outstagram.boot.member.CurrentMember;
import com.outstagram.boot.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Mono<ResponseEntity<Article>> createArticle(@RequestBody @Valid Article article, Errors errors, @CurrentMember Member member) {
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return articleService.create(article, member)
                .map(saveArticle -> new ResponseEntity<>(saveArticle, HttpStatus.CREATED));
    }

    @PostMapping("/{id}/favorite")
    public Mono<Article> favoriteArticle(@PathVariable(value = "id") String id, @CurrentMember String memberId) {
        Article article = articleService.findArticle(id);
        articleService.favorite(article, memberId);
        return articleService.save(article);
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
