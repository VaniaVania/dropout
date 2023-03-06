package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.TopArtistService;
import com.ivan.restapplication.service.TopTrackService;
import com.ivan.restapplication.service.ProfileService;
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
    private final ProfileService profileService;

    @Autowired
    public ProfileController(TopTrackService topTrackService, TopArtistService topArtistService, ProfileService profileService) {
        this.topTrackService = topTrackService;
        this.topArtistService = topArtistService;
        this.profileService = profileService;
    }

    @GetMapping()
    public String myProfile(@RequestParam(defaultValue = "short_term") String time_range, Model model) throws JsonProcessingException, UnauthorizedUserException {

        try {
            //Top track Card
            model.addAttribute("tracks", topTrackService.findTopTracks(time_range));

            //Top Artist Cart
            model.addAttribute("items", topArtistService.findTopArtists(time_range));

            //User Card
            model.addAttribute("display_name", profileService.showUserProfile().get("display_name").asText());
            model.addAttribute("followers", profileService.showUserProfile().get("followers").get("total"));
            model.addAttribute("country", profileService.showUserProfile().get("country").toString()
                    .replace("\"", "")
                    .toLowerCase());

            model.addAttribute("external_urls", profileService.showUserProfile().get("external_urls")
                    .get("spotify")
                    .asText());
            model.addAttribute("term", time_range);
        } catch (HttpClientErrorException ex){
            throw new UnauthorizedUserException();
        }

        return "profile";
    }
}
