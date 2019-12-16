package com.outstagram.boot.member;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*")
    private String password;

}
