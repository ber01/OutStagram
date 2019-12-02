package com.outstagram.boot.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Mono<Article> create(Article article) {
        return articleRepository.save(article);
    }
}
