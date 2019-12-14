package com.outstagram.boot.article;

import com.outstagram.boot.member.Member;
import com.outstagram.boot.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    public Mono<Article> create(Article article, String memberId) {
        article.setMemberId(memberId);
        return articleRepository.save(article);
    }

    public Flux<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Mono<Article> getArticleById(String id) {
        return articleRepository.findById(id);
    }

    public Mono<ResponseEntity<Article>> updateArticle(String id, Article article) {
        return articleRepository.findById(id)
                .flatMap(existingArticle -> {
                    existingArticle.setTitle(article.getTitle());
                    return articleRepository.save(existingArticle);
                })
                .map(updatedArticle -> new ResponseEntity<>(updatedArticle, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public Mono<ResponseEntity<Void>> deleteArticle(String id) {
        return articleRepository.findById(id)
                .flatMap(existingArticle ->
                    articleRepository.delete(existingArticle)
                            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
