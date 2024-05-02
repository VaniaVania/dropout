package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ivan.restapplication.service.AnalysisService;
import com.ivan.restapplication.service.SpotifyGenresService;
import com.ivan.restapplication.service.SpotifyTracksService;
import com.ivan.restapplication.service.SpotifyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/manage")
@RequiredArgsConstructor
public class ManageController {

    private final SpotifyTracksService trackService;
    private final SpotifyUserService userService;
    private final SpotifyGenresService genresService;
    private final AnalysisService analysisService;

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
        JsonNode followedArtistsNode = userService.findFollowedArtists();
        model.addAllAttributes(Map.of(
                "followedArtists", followedArtistsNode,
                "suggestArtists", analysisService.findSuggestedArtists(followedArtistsNode),
                "topTracks", userService.findTopTracks("short_term"),
                "availableGenres", genresService.getAvailableGenresSeeds()
        ));
    }

}
