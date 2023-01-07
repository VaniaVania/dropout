package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.AuthService;
import com.ivan.restapplication.service.TopArtistService;
import com.ivan.restapplication.service.TopTrackService;
import com.ivan.restapplication.service.UserService;
import com.ivan.restapplication.util.UnauthorizedUserException;
import com.ivan.restapplication.util.UnauthorizedUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final AuthService authService;
    private final TopTrackService topTrackService;
    private final TopArtistService topArtistService;
    private final UserService userService;

    @Autowired
    public ProfileController(AuthService authService, TopTrackService topTrackService, TopArtistService topArtistService, UserService userService) {
        this.authService = authService;
        this.topTrackService = topTrackService;
        this.topArtistService = topArtistService;
        this.userService = userService;
    }

    @GetMapping()
    public String myProfile(@RequestParam(value = "time_range",defaultValue = "short_term") String time_range, Model model) throws JsonProcessingException, UnauthorizedUserException {

        //Top track Card
        model.addAttribute("tracks", topTrackService.findTopTracks(time_range));

        //Top Artist Cart
        model.addAttribute("items", topArtistService.findTopArtists(time_range));

        //User Card
        model.addAttribute("images", userService.showUserProfile().get("images")
                .findValues("url")
                .get(0)
                .asText());
        model.addAttribute("display_name", userService.showUserProfile().get("display_name").asText());
        model.addAttribute("followers", userService.showUserProfile().get("followers").get("total"));
        model.addAttribute("country", userService.showUserProfile().get("country").toString()
                .replace("\"", "")
                .toLowerCase());

        model.addAttribute("external_urls", userService.showUserProfile().get("external_urls")
                .get("spotify")
                .asText());

        return "profile";
    }

    @ExceptionHandler
    private ResponseEntity<UnauthorizedUserResponse> handleException(UnauthorizedUserException e){
        UnauthorizedUserResponse response = new UnauthorizedUserResponse("Person is not authorized!",
                System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // Unauthorized - Status 401
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
