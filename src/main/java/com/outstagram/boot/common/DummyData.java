package com.outstagram.boot.common;

import com.outstagram.boot.article.Article;
import com.outstagram.boot.article.ArticleRepository;
import com.outstagram.boot.member.Member;
import com.outstagram.boot.member.MemberRepository;
import com.outstagram.boot.member.MemberRole;
import com.outstagram.boot.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AppProperties appProperties;
    private final ArticleRepository articleRepository;

    @Override
    public void run(String... args) throws Exception {
        memberRepository.deleteAll()
                .thenMany(
                        Flux.just(
                                Member.builder()
                                        .email("test@email.com")
                                        .username("testuser")
                                        .password("pass")
                                        .createdAt(LocalDateTime.now())
                                        .roles(Set.of(MemberRole.ADMIN, MemberRole.USER))
                                        .build(),
                                Member.builder()
                                        .email("test2@email.com")
                                        .username("testuser2")
                                        .password("pass")
                                        .createdAt(LocalDateTime.now())
                                        .roles(Set.of(MemberRole.USER))
                                        .build(),
                                Member.builder()
                                        .email("test3@email.com")
                                        .username("testuser3")
                                        .password("pass")
                                        .createdAt(LocalDateTime.now())
                                        .roles(Set.of(MemberRole.ADMIN))
                                        .build(),
                                // 테스트 용 더미 회원
                                Member.builder()
                                        .email(appProperties.getTestUsername())
                                        .username("authTest")
                                        .password(appProperties.getTestPassword())
                                        .createdAt(LocalDateTime.now())
                                        .roles(Set.of(MemberRole.ADMIN, MemberRole.USER))
                                        .build()
                        ).flatMap(memberService::saveMember)
                ).subscribe(System.out::println);
        articleRepository.deleteAll()
                .thenMany(
                        Flux.just(
                                 Article.builder()
                                        .title("test")
                                        .description("It is test")
                                        .createdAt(LocalDateTime.now())
                                        .image("https://placeimg.com/512/512/1")
                                        .favoritesCount(0)
                                        .build(),
                                Article.builder()
                                        .title("test2")
                                        .description("It is test")
                                        .createdAt(LocalDateTime.now())
                                        .image("https://placeimg.com/512/512/2")
                                        .favoritesCount(0)
                                        .build(),
                                Article.builder()
                                        .title("test3")
                                        .description("It is test")
                                        .createdAt(LocalDateTime.now())
                                        .image("https://placeimg.com/512/512/3")
                                        .favoritesCount(0)
                                        .build()
                        ).flatMap(articleRepository::save)
                ).subscribe(System.out::println);
    }
}