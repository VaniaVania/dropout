package com.ivan.restapplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.ivan.restapplication.exception.UnauthorizedUserException;
import com.ivan.restapplication.util.TokenHandler;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @SessionScope
    public RestTemplate restTemplate(OAuth2AuthorizedClientService clientService) {
        RestTemplate restTemplate = new RestTemplate();
        String accessToken = TokenHandler.getAuthenticationToken(clientService);
        if (accessToken != null) {
            restTemplate.getInterceptors().add(getBearerTokenInterceptor(accessToken));
        } else {
            restTemplate.getInterceptors().add(getNoTokenInterceptor());
        }
        return restTemplate;
    }

    @Bean("myCacheManager")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(){
            @Override
            @NonNull
            protected Cache createConcurrentMapCache(@NonNull String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(100, TimeUnit.SECONDS)
                                .build().asMap(),
                        false);
            }
        };
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
