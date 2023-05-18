package com.ivan.restapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

@Configuration
public class OAuth2LoginConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.spotifyClientRegistration());
    }

    private ClientRegistration spotifyClientRegistration() {
        return ClientRegistration.withRegistrationId("spotify")
                .clientId("2c8ed13da29f45b990a1ad43ba870f7d")
                .clientSecret("c18c855bfbe442d6aed8b9df99ae131f")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8085/callback")
                .scope("playlist-modify-private", "playlist-modify-public", "ugc-image-upload", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing" , "app-remote-control" ,"playlist-read-private" , "playlist-read-collaborative", "playlist-modify-private", "playlist-modify-public", "user-follow-modify", "user-follow-read", "user-read-playback-position", "user-top-read", "user-read-recently-played", "user-library-modify", "user-library-read", "user-read-email", "user-read-private")
                .authorizationUri("https://accounts.spotify.com/authorize")
                .tokenUri("https://accounts.spotify.com/api/token")
                .userInfoUri("https://api.spotify.com/v1/me")
                .build();

    }
}