package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.SubscriptionService;
import com.ivan.restapplication.service.TopTrackService;
import com.ivan.restapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String followedArtists(){

        return "manage";
    }

    @GetMapping("/recommendation")
    public String recommendedArtists(@RequestParam String seed_artists, @RequestParam String seed_tracks, @RequestParam String seed_genres, Model model) throws JsonProcessingException {
        model.addAttribute("spotifySuggestedArtists", subscriptionService.getSpotifySuggestedArtists(seed_artists,seed_tracks,seed_genres));
        return "recommendation";
    }

    @DeleteMapping("/delete")
    public String unfollowArtists(@RequestParam String ids) {
        subscriptionService.unfollowArtists(ids);
        return "redirect:/manage";
    }

    @PutMapping("/follow")
    public String followArtists(@RequestParam String ids) {
        subscriptionService.followArtists(ids);
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
