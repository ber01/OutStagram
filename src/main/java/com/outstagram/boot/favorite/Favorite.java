package com.outstagram.boot.favorite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Favorite {

    @Id
    private String id;

    private String articleId;

    private String memberId;
}
