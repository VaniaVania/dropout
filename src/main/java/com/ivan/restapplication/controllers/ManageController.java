package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.SpotifyApiService;
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

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public ManageController(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    @GetMapping
    public String followedArtists() {
        return "manage";
    }

    @GetMapping("/recommendation")
    public String recommendedArtists(@RequestParam String seed_artists, @RequestParam String seed_tracks, @RequestParam String seed_genres, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        try {
            model.addAttribute("spotifySuggestedArtists", spotifyApiService.getSpotifySuggestedTracks(seed_artists, seed_tracks, seed_genres));
        } catch (HttpClientErrorException ex) {
            redirectAttributes.addFlashAttribute("generateError", "Choose the right amount of seed");
            return "redirect:/manage#generateNav";
        }
        return "recommendation";
    }

    @DeleteMapping("/unfollow")
    public String unfollowArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            spotifyApiService.unfollowArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }
        return "redirect:/manage";
    }

    @PutMapping("/follow")
    public String followArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            spotifyApiService.followArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }
        return "redirect:/manage";
    }

    @ModelAttribute
    public void attributes(Model model) throws JsonProcessingException {
        model.addAllAttributes(Map.of(
                "followedArtists", spotifyApiService.getFollowedArtists(),
                "suggestArtists", spotifyApiService.getSuggestedArtists(),
                "topTracks", spotifyApiService.findTopTracks("short_term"),
                "availableGenres", spotifyApiService.getAvailableGenresSeeds()
        ));
    }

}
