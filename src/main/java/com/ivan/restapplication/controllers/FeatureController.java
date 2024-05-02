package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ivan.restapplication.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/features")
@RequiredArgsConstructor
public class FeatureController {

    private final AnalysisService analysisService;

    @GetMapping()
    public String features(@RequestParam(defaultValue = "short_term", name = "time_range") String timeRange, @RequestParam(defaultValue = "acousticness") String feature, Model model) throws JsonProcessingException {
            model.addAllAttributes(Map.of(
                    "track", analysisService.findMinMaxTrackFeatures(feature, timeRange),
                    "feature", feature,
                    "term", timeRange)
            );
            return "features";
    }
}
