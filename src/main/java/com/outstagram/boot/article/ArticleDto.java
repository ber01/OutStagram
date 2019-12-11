package com.outstagram.boot.article;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class ArticleDto {

    @NotNull
    private String image;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Min(0)
    private Integer favoritesCount;
}
