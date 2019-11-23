package com.outstagram.boot.member;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Document
public class Member {

    @Id
    private String id;

    private String email;

    private String username;

    private String password;

    private String image;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
