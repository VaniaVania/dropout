package com.ivan.restapplication.config.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Value(value = "${spring.security.oauth2.client.registration.spotify.client-id}")
    private String clientId;

    @Value(value = "${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String clientSecret;

    @Value(value = "${app.spotify.redirect_url}")
    private String redirectUri;

    @Value(value = "${app.spotify.authorization_uri}")
    private String authorizationUri;

    @Value(value = "${app.spotify.token_uri}")
    private String tokenUri;

    @Value(value = "${app.spotify.user_info_uri}")
    private String userInfoUri;

    @Value(value = "${app.spotify.username_attribute_name}")
    private String usernameAttributeName;

    @Value(value = "${app.spotify.registration_id}")
    private String registrationId;

    @Value(value = "${app.spotify.scope}")
    private String scope;

    @Bean
    @SneakyThrows
    SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/", "/images/**", "/styles/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .oauth2Login(withDefaults())
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .clearAuthentication(true))
                .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration spotifyClientRegistration = ClientRegistration
                .withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .scope(scope.split(","))
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .userInfoUri(userInfoUri)
                .userNameAttributeName(usernameAttributeName)
                .clientName(registrationId)
                .build();
        return new InMemoryClientRegistrationRepository(spotifyClientRegistration);
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .clientCredentials()
                        .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }
}
