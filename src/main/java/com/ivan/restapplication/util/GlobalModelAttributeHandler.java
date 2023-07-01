package com.ivan.restapplication.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.SpotifyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeHandler {

    @Autowired
    private SpotifyUserService service;

   @ModelAttribute
    public void addAttributes(Model model) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal().toString().equals("anonymousUser") ) {
            model.addAttribute("isAuthenticated", false);

        } else {
            model.addAttribute("isAuthenticated", true);

            if (!service.showUserProfile().findValues("url").isEmpty()) {
                model.addAttribute("profileImage", service.showUserProfile()
                        .findValues("url")
                        .get(0)
                        .asText());

            } else {
                model.addAttribute("profileImage", "/images/user.png");
            }
        }
    }
}
