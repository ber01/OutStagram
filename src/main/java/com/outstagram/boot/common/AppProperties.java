package com.outstagram.boot.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("my-app")
@Getter @Setter
public class AppProperties {

    private String testUsername;

    private String testPassword;

    private String clientId;

    private String clientSecret;

}
