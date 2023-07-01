package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.SpotifyUserService;
import com.ivan.restapplication.service.UserService;
import com.ivan.restapplication.service.SavedUserService;
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

    private final ModelMapper mapper;
    private final SpotifyUserService userService;
    private final SavedUserService savedUserService;

    @Autowired
    public ProfileController(SavedUserService savedUserService, ModelMapper mapper, UserService userService) {
        this.savedUserService = savedUserService;
        this.mapper = mapper;
        this.userService = userService;
    }

    @GetMapping()
    public String myProfile(@RequestParam(defaultValue = "short_term") String time_range, Model model) throws JsonProcessingException, UnauthorizedUserException {
        try {
            savedUserService.save(convertToUser(userService.getSpotifyUserDTO()));
            JsonNode userProfile = userService.showUserProfile();
            model.addAllAttributes(Map.of(
                    "tracks", userService.findTopTracks(time_range),
                    "items", userService.findTopArtists(time_range),
                    "display_name", userProfile.get("display_name").asText(),
                    "followers", userProfile.get("followers").get("total"),
                    "country", userProfile.get("country").asText(),
                    "external_urls", userProfile.get("external_urls").get("spotify").asText(),
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
