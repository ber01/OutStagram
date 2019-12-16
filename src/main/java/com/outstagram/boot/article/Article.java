package com.outstagram.boot.article;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString @EqualsAndHashCode(of = "id")
@Document
public class Article {

    @Id
    private String id;

    @NotNull
    private String image;

    @NotNull
    @Size(min = 1, max = 255)
    private String title;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    private List<String> tagList;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Min(0)
    private Integer favoritesCount;

    private Set<String> favoritedMemberId;

    private String memberId;
}
