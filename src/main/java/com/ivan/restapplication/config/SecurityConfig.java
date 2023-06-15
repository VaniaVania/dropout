package com.ivan.restapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> {
                        authorize.antMatchers("/").permitAll();
                        authorize.antMatchers("/images/**", "/styles/**").permitAll();
                        authorize.anyRequest().authenticated();
                })
                .cors().disable()
                .csrf().disable()
                .oauth2Login()
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .clearAuthentication(true)
                .invalidateHttpSession(true).deleteCookies("JSESSIONID").deleteCookies("SESSION")
                .and()
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.spotifyClientRegistration());
    }

    private ClientRegistration spotifyClientRegistration() {
        return ClientRegistration
                .withRegistrationId("spotify")
                .clientId("2c8ed13da29f45b990a1ad43ba870f7d")
                .clientSecret("c18c855bfbe442d6aed8b9df99ae131f")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8085/login/oauth2/code/spotify")
                .scope("playlist-modify-private", "playlist-modify-public", "user-modify-playback-state", "playlist-read-private", "playlist-modify-public", "user-follow-modify", "user-follow-read", "user-top-read", "user-read-recently-played", "user-library-modify", "user-library-read", "user-read-email", "user-read-private")
                .authorizationUri("https://accounts.spotify.com/authorize?show_dialog=true")
                .tokenUri("https://accounts.spotify.com/api/token")
                .userInfoUri("https://api.spotify.com/v1/me")
                .userNameAttributeName("display_name")
                .clientName("spotify")
                .build();

    }


}
