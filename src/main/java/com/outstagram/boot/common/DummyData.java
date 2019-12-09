package com.outstagram.boot.common;

import com.outstagram.boot.article.Article;
import com.outstagram.boot.article.ArticleRepository;
import com.outstagram.boot.member.Member;
import com.outstagram.boot.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final MemberRepository memberRepository;

    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) throws Exception {
        memberRepository.deleteAll()
                .thenMany(
                        Flux.just(
                                Member.builder()
                                    .email("test1@emailcom")
                                    .username("test1")
                                    .password("pass")
                                    .createdAt(LocalDateTime.now())
                                .build(),
                                Member.builder()
                                    .email("test2@emailcom")
                                    .username("test2")
                                    .password("pass")
                                    .createdAt(LocalDateTime.now())
                                .build(),
                                Member.builder()
                                    .email("test3@emailcom")
                                    .username("test3")
                                    .password("pass")
                                    .createdAt(LocalDateTime.now())
                                .build()
                        ).flatMap(memberRepository::save)
                ).subscribe();

        articleRepository.deleteAll()
                .thenMany(
                        Flux.fromStream(
                                Stream.generate( () ->
                                        create()
                                ).limit(30L)
                        ).flatMap(articleRepository::save)
                ).subscribe();
    }

    private Article create() {
        return Article.builder()
                .id(UUID.randomUUID().toString())
                .title("test")
                .description("It is test")
                .createdAt(LocalDateTime.now())
                .image("/url")
                .favoritesCount(0)
                .build();
    }
}
