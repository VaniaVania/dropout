package com.ivan.restapplication.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
@ConfigurationProperties(prefix = "spotify.client")
@PropertySource("classpath:application.properties")
@SessionScope
@Getter
@Setter
public class SpotifyConfig {

    @Value(value = "${spring.security.oauth2.client.registration.spotify.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String clientSecret;

    private String redirectUri;
    private String responseType;
    private String grantType;
    private String scope;
    private boolean showDialog;
    private String token;
    private String refreshToken;
    private String code;

}
