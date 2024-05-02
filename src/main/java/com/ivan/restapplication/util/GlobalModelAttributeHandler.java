package com.ivan.restapplication.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.SpotifyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeHandler {

    private final SpotifyUserService service;

    @ModelAttribute
    public void addAttributes(Model model) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication.isAuthenticated();
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            if (!service.showUserProfile().findValues("url").isEmpty()) {
                model.addAttribute("profileImage", service.showUserProfile().findValues("url").get(1).asText());
            } else {
                model.addAttribute("profileImage", "/images/user.png");
            }
        }
    }
}

