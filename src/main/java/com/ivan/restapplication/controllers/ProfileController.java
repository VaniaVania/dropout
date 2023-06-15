package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.SavedUserService;
import com.ivan.restapplication.service.SpotifyApiService;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final SpotifyApiService spotifyApiService;
    private final SavedUserService savedUserService;
    private final ModelMapper mapper;

    @Autowired
    public ProfileController(SpotifyApiService spotifyApiService, SavedUserService savedUserService, ModelMapper mapper) {
        this.spotifyApiService = spotifyApiService;
        this.savedUserService = savedUserService;
        this.mapper = mapper;
    }

    @GetMapping()
    public String myProfile(@RequestParam(defaultValue = "short_term") String time_range, Model model) throws JsonProcessingException, UnauthorizedUserException {
        try {
            savedUserService.save(convertToUser(spotifyApiService.getSpotifyUserDTO()));

            model.addAllAttributes(Map.of(
                    "tracks", spotifyApiService.findTopTracks(time_range),
                    "items", spotifyApiService.findTopArtists(time_range),
                    "display_name", spotifyApiService.showUserProfile().get("display_name").asText(),
                    "followers", spotifyApiService.showUserProfile().get("followers").get("total"),
                    "country", spotifyApiService.showUserProfile().get("country").asText(),
                    "external_urls", spotifyApiService.showUserProfile().get("external_urls").get("spotify").asText(),
                    "term", time_range
            ));
        } catch (HttpClientErrorException ex){
            throw new UnauthorizedUserException();
        }
        return "profile";
    }

    public User convertToUser(UserDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }
}
