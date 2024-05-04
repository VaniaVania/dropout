package com.ivan.restapplication.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@UtilityClass
public class TokenHandler {

    @Nullable
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
}
