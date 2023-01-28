package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.TopTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/features")
public class FeatureController {

    private final TopTrackService topTrackService;

    @Autowired
    public FeatureController(TopTrackService topTrackService) {
        this.topTrackService = topTrackService;
    }

    @GetMapping()
    public String features(@RequestParam(defaultValue = "short_term") String time_range, @RequestParam(defaultValue = "acousticness") String feature, Model model) throws JsonProcessingException {
        //Happiest Track Card
        model.addAttribute("happiestTrack", topTrackService.findTrackFeature("positive", feature, time_range));
        model.addAttribute("saddestTrack", topTrackService.findTrackFeature("negative", feature, time_range));

        return "features";
    }

}
