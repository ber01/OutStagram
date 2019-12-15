package com.outstagram.boot.member;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode(of = "id")
@Document
public class Member {

    @Id
    private String id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 2)
    private String username;

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*")
    private String password;

    private String image;

    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Set<MemberRole> roles;

}
