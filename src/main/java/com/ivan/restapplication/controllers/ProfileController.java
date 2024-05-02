package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ivan.restapplication.mapper.UserMapper;
import com.ivan.restapplication.exception.UnauthorizedUserException;
import com.ivan.restapplication.service.SpotifyUserService;
import com.ivan.restapplication.service.impl.SavedUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final SpotifyUserService userService;
    private final SavedUserServiceImpl savedUserServiceImpl;
    private final UserMapper mapper;

    @GetMapping()
    public String myProfile(@RequestParam(defaultValue = "short_term", name = "time_range") String timeRange, Model model) throws JsonProcessingException, UnauthorizedUserException {
        savedUserServiceImpl.save(mapper
                .dtoToEntity(userService
                        .getSpotifyUserDTO()));
        JsonNode userProfile = userService.showUserProfile();
        model.addAllAttributes(Map.of(
                "tracks", userService.findTopTracks(timeRange),
                "items", userService.findTopArtists(timeRange),
                "display_name", userProfile.get("display_name").asText(),
                "followers", userProfile.get("followers").get("total"),
                "country", userProfile.get("country").asText(),
                "external_urls", userProfile.get("external_urls").get("spotify").asText(),
                "term", timeRange
        ));
        return "profile";
    }
}
