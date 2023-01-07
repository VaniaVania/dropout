package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.AuthService;
import com.ivan.restapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manage")
public class ManageController {

    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public ManageController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping
    public String followedArtists(Model model) throws JsonProcessingException {
       model.addAttribute("artists", userService.getFollowedArtists());
       model.addAttribute("suggestArtists", userService.suggestArtists());
       return "manage";
    }

    @DeleteMapping("/delete")
    public String unfollowArtist(@RequestParam String ids){
        userService.unfollowArtist(ids);
        return "redirect:/manage";
    }

    @PutMapping("/follow")
    public String followArtist(@RequestParam String ids){
        userService.followArtist(ids);
        return "redirect:/manage";
    }

    @ModelAttribute
    public void addAttributes(Model model) throws JsonProcessingException {
        if(authService.getToken()!=null){
            model.addAttribute("isAuthorized", true);
            model.addAttribute("profileImage", userService.showUserProfile().get("images")
                    .findValues("url")
                    .get(0)
                    .asText());
        }
    }
}
