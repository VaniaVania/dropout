package com.ivan.restapplication.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.AuthService;
import com.ivan.restapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeHandler {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public GlobalModelAttributeHandler(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model) throws JsonProcessingException {
        if (authService.getToken() != null) {
            model.addAttribute("isAuthorized", true);
            model.addAttribute("profileImage", userService.showUserProfile().get("images")
                    .findValues("url")
                    .get(0)
                    .asText());
        }
    }
}
