package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.ManageService;
import com.ivan.restapplication.service.TopTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/manage")
public class ManageController {

    private final ManageService manageService;
    private final TopTrackService topTrackService;

    @Autowired
    public ManageController(ManageService manageService, TopTrackService topTrackService) {
        this.manageService = manageService;
        this.topTrackService = topTrackService;
    }

    @GetMapping
    public String followedArtists() {
        return "manage";
    }

    @GetMapping("/recommendation")
    public String recommendedArtists(@RequestParam String seed_artists, @RequestParam String seed_tracks, @RequestParam String seed_genres, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {

        try {
            model.addAttribute("spotifySuggestedArtists", manageService.getSpotifySuggestedTracks(seed_artists, seed_tracks, seed_genres));
        } catch (HttpClientErrorException ex) {
            redirectAttributes.addFlashAttribute("generateError", "Choose the right amount of seed");
            return "redirect:/manage#generateNav";
        }
        return "recommendation";
    }

    @DeleteMapping("/unfollow")
    public String unfollowArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            manageService.unfollowArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }
        return "redirect:/manage";
    }

    @PutMapping("/follow")
    public String followArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            manageService.followArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }

        return "redirect:/manage";
    }

    @ModelAttribute
    public void attributes(Model model) throws JsonProcessingException {

        model.addAttribute("followedArtists", manageService.getFollowedArtists());  //half-time
        model.addAttribute("suggestArtists", manageService.getSuggestedArtists()); //half-time
        model.addAttribute("topTracks", topTrackService.findTopTracks("short_term"));
        model.addAttribute("availableGenres", manageService.getAvailableGenresSeeds());
    }

}
