package com.ivan.restapplication.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.properties.SpotifyProperties;
import com.ivan.restapplication.service.AuthService;
import com.ivan.restapplication.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalModelAttributeHandler {

    private final AuthService authService;
    private final ProfileService profileService;
    private final SpotifyProperties properties;

    @Autowired
    public GlobalModelAttributeHandler(AuthService authService, ProfileService profileService, SpotifyProperties properties) {
        this.authService = authService;
        this.profileService = profileService;
        this.properties = properties;
    }

    @ModelAttribute
    public void addAttributes(Model model) throws JsonProcessingException {
        try {
            authService.useToken();
        } catch (HttpClientErrorException e){
            authService.refreshToken();
            authService.useToken();
        }

        try {
            if (properties.getToken() != null) {
                model.addAttribute("isAuthorized", true);
            }

            if (!profileService.showUserProfile().findValues("url").isEmpty()) {
                model.addAttribute("profileImage", profileService.showUserProfile()
                        .findValues("url")
                        .get(0)
                        .asText());

            } else {
                model.addAttribute("profileImage", "/images/user.png");
            }

        } catch (HttpClientErrorException e) {
            authService.logout();
        }



    }
}
