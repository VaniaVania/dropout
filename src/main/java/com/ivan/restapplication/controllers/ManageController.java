package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.SubscriptionService;
import com.ivan.restapplication.service.TopTrackService;
import com.ivan.restapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/manage")
public class ManageController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final TopTrackService topTrackService;

    @Autowired
    public ManageController(UserService userService, SubscriptionService subscriptionService, TopTrackService topTrackService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.topTrackService = topTrackService;
    }

    @GetMapping

    public String followedArtists() {
        return "manage";
    }

    @GetMapping("/recommendation")
    public String recommendedArtists(@RequestParam String seed_artists, @RequestParam String seed_tracks, @RequestParam String seed_genres, Model model, RedirectAttributes redirectAttributes) throws JsonProcessingException {

        try {
            model.addAttribute("spotifySuggestedArtists", subscriptionService.getSpotifySuggestedArtists(seed_artists, seed_tracks, seed_genres));
        } catch (HttpClientErrorException ex) {
            redirectAttributes.addFlashAttribute("generateError", "Seed must include 5 values in total!");
            return "redirect:/manage#generateNav";
        }
        return "recommendation";
    }

    @DeleteMapping("/unfollow")
    public String unfollowArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.unfollowArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }
        return "redirect:/manage";
    }

    @PutMapping("/follow")
    public String followArtists(@RequestParam String ids, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.followArtists(ids);
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error", "Choose the right amount of artists");
        }

        return "redirect:/manage";
    }

    @ModelAttribute
    public void attributes(Model model) throws JsonProcessingException {
        model.addAttribute("topTracks", topTrackService.findTopTracks("short_term"));
        model.addAttribute("followedArtists", userService.getFollowedArtists());
        model.addAttribute("availableGenres", subscriptionService.getAvailableGenresSeeds());


        model.addAttribute("suggestArtists", subscriptionService.getSuggestedArtists());
    }

}
