package com.outstagram.boot.article;

import com.outstagram.boot.member.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Article> createArticle(@RequestBody @Valid Article article, @CurrentMember String memberId) {
        return articleService.create(article, memberId);
    }

    @PostMapping("/{id}/favorite")
    public Mono<Article> favoriteArticle(@PathVariable(value = "id") String id, @CurrentMember String memberId) {
        Article article = articleService.findArticle(id);
        article.setFavoritesCount(article.getFavoritesCount() + 1);
        article.setFavoritedMemberId(Collections.singleton(memberId));
        if (article.getFavoritedMemberId().contains(memberId)) {
            article.setFavoritesCount(article.getFavoritesCount() - 1);
        }
        return articleService.create(article, memberId);
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
