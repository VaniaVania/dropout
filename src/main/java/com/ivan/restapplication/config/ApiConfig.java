package com.ivan.restapplication.config;

import com.ivan.restapplication.service.AnalysisService;
import com.ivan.restapplication.service.GenresService;
import com.ivan.restapplication.service.TrackService;
import com.ivan.restapplication.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.context.annotation.RequestScope;


@Configuration
public class ApiConfig {

    @Bean
    @RequestScope
    public UserService userService(OAuth2AuthorizedClientService clientService) {
        return new UserService(authentication(clientService));
    }

    @Bean
    @RequestScope
    public AnalysisService analysisService(OAuth2AuthorizedClientService clientService) {
        return new AnalysisService(authentication(clientService));
    }

    @Bean
    @RequestScope
    public TrackService trackService(OAuth2AuthorizedClientService clientService) {
        return new TrackService(authentication(clientService));
    }

    @Bean
    @RequestScope
    public GenresService genresService(OAuth2AuthorizedClientService clientService) {
        return new GenresService(authentication(clientService));
    }

    private String authentication(OAuth2AuthorizedClientService clientService){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            if (oauthToken.getAuthorizedClientRegistrationId().equals("spotify")) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
                if (client == null) {
                    clientService.removeAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
                } else {
                    return client.getAccessToken().getTokenValue();
                }
            }
        }
        return null;
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .clientCredentials()
                        .password()
                        .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

}
