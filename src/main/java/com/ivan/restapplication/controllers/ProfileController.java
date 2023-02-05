package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.TopArtistService;
import com.ivan.restapplication.service.TopTrackService;
import com.ivan.restapplication.service.UserService;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final TopTrackService topTrackService;
    private final TopArtistService topArtistService;
    private final UserService userService;

    @Autowired
    public ProfileController(TopTrackService topTrackService, TopArtistService topArtistService, UserService userService) {
        this.topTrackService = topTrackService;
        this.topArtistService = topArtistService;
        this.userService = userService;
    }

    @GetMapping()
    public String myProfile(@RequestParam(defaultValue = "short_term") String time_range, Model model) throws JsonProcessingException, UnauthorizedUserException {

        try {
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
            model.addAttribute("term", time_range);
        } catch (HttpClientErrorException ex){
            throw new UnauthorizedUserException();
        }

        return "profile";
    }
}
