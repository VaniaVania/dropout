package com.ivan.restapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.ivan.restapplication.exception.UnauthorizedUserException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean()
    @RequestScope
    public RestTemplate restTemplate(OAuth2AuthorizedClientService clientService) {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = getAuthenticationToken(clientService);
        if (accessToken != null) {
            restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
        } else {
            restTemplate.getInterceptors().add(getNoTokenInterceptor());
        }
        return restTemplate;
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
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

    @Bean("myCacheManager")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(){
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(10, TimeUnit.SECONDS)
                                .build().asMap(),
                        false);
            }
        };
    }

    public String getAuthenticationToken(OAuth2AuthorizedClientService clientService){
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

    public ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        return (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, body);
        };
    }

    public ClientHttpRequestInterceptor getNoTokenInterceptor() {
        return (request, body, execution) -> {
            throw new UnauthorizedUserException();
        };
    }

}
