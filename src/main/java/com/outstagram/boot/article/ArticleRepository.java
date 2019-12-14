package com.outstagram.boot.article;

import com.outstagram.boot.member.Member;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ArticleRepository extends ReactiveMongoRepository<Article, String> {
    Mono<Article> findByTitle (String title);
    Flux<Article> findByMemberId (String memberId);
}
