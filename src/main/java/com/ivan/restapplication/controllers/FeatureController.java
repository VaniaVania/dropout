package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ivan.restapplication.service.AnalysisService;
import com.ivan.restapplication.util.NotListeningUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/features")
public class FeatureController {

    private final AnalysisService analysisService;

    @Autowired
    public FeatureController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping()
    public String features(@RequestParam(defaultValue = "short_term") String time_range, @RequestParam(defaultValue = "acousticness") String feature, Model model) throws JsonProcessingException {
        try {
            model.addAttribute("track", analysisService.findMinMaxTrackFeatures(feature, time_range));
            model.addAttribute("feature", feature);
            model.addAttribute("term", time_range);
            return "features";
        } catch (NoSuchElementException e){
            throw new NotListeningUserException();
        }
    }
}
