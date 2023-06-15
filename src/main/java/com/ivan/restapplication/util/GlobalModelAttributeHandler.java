package com.ivan.restapplication.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.SpotifyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeHandler {

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public GlobalModelAttributeHandler(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

   @ModelAttribute
    public void addAttributes(Model model) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getPrincipal().toString().equals("anonymousUser") ) {
            model.addAttribute("isAuthenticated", true);

           if (!spotifyApiService.showUserProfile().findValues("url").isEmpty()) {
                model.addAttribute("profileImage", spotifyApiService.showUserProfile()
                        .findValuesAsText("url")
                        .get(0));

            } else {
                model.addAttribute("profileImage", "/images/user.png");
            }

        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }
}
