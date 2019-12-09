package com.outstagram.boot.article;

import com.outstagram.boot.member.Member;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString @EqualsAndHashCode(of = "id")
@Document
public class Article {

    @Id
    private String id;

    private String image;

    private String title;

    private String description;

    private List<String> tagList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean favorited;

    private Integer favoritesCount;

    private Member member;
}
