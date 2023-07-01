package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ivan.restapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;


@Controller
@RequestMapping("/manage")
public class ManageController {

    private final SpotifyTracksService trackService;
    private final SpotifyUserService userService;
    private final SpotifyGenresService genresService;
    private final AnalysisService analysisService;

    @Autowired
    public ManageController(TrackService trackService, UserService userService, SpotifyGenresService genresService, AnalysisService analysisService) {
        this.trackService = trackService;
        this.userService = userService;
        this.genresService = genresService;
        this.analysisService = analysisService;
    }

    @GetMapping
    public String followedArtists() {
        return "manage";
    }

    @GetMapping("/recommendation")
    public String recommendedArtists(@RequestParam String seed_artists, @RequestParam String seed_tracks, @RequestParam String seed_genres, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        try {
            model.addAttribute("spotifySuggestedArtists", trackService.getRecommendations(seed_artists, seed_tracks, seed_genres));
        } catch (HttpClientErrorException ex) {
            redirectAttributes.addFlashAttribute("generateError", "Choose the right amount of seed");
            return "redirect:/manage#generateNav";
        }
        return "recommendation";
    }

    @DeleteMapping("/unfollow")
    public String unfollowArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            userService.unfollowArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }
        return "redirect:/manage";
    }

    @PutMapping("/follow")
    public String followArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            userService.followArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }
        return "redirect:/manage";
    }

    @ModelAttribute
    public void attributes(Model model) throws JsonProcessingException {
        JsonNode followedArtistsNode = userService.getFollowedArtists();

        model.addAllAttributes(Map.of(
                "followedArtists", followedArtistsNode,
                "suggestArtists", analysisService.getSuggestedArtists(followedArtistsNode),
                "topTracks", userService.findTopTracks("short_term"),
                "availableGenres", genresService.getAvailableGenresSeeds()
        ));
    }

}
