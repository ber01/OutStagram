package com.outstagram.boot.member;

import com.outstagram.boot.common.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    private final static String BEARER = "BEARER ";

    @PostMapping("/api/login")
    public Mono<ResponseEntity<Member>> login(@RequestBody @Valid LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        Member checkMember = memberRepository.findByEmail(email).block();

        if (checkMember == null) {
            return Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
        } else {
            String checkPwd = checkMember.getPassword();
            boolean compare = passwordEncoder.matches(password, checkPwd);

            if (compare) {
                checkMember.setAccessToken(getAccessToken(checkMember));
                return Mono.just(new ResponseEntity<>(checkMember, HttpStatus.OK));
            } else {
                return Mono.just(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
            }
        }
    }

    private String getAccessToken(Member checkMember) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();

        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        fromData.add("username", checkMember.getEmail());
        fromData.add("password", checkMember.getPassword());
        fromData.add("grant_type", "password");

        webClient
                .mutate().filter(basicAuthentication(appProperties.getClientId(), appProperties.getClientSecret())).build()
                .post().uri("/oauth/token")
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(System.out::println)
        ;

        /*
        System.out.println(result);

        String resultS = result.toString();
        int start = resultS.indexOf("{");
        int end = resultS.indexOf("}");
        String content = resultS.substring(start, end + 1);

        Jackson2JsonParser parser = new Jackson2JsonParser();
        return BEARER + parser.parseMap(content).get("access_token").toString();
        */

        return null;
    }

}
