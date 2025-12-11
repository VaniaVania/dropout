package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.client.GenoutClient;
import com.ivan.restapplication.dto.response.SongAnalyticsResponse;
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

    private final GenoutClient genoutClient;
    private final ObjectMapper mapper;

    @GetMapping
    public String compareTracks(@RequestParam(defaultValue = "791921") String firstSongId, @RequestParam(defaultValue = "10631889") String secondSongId, Model model) {
        SongAnalyticsResponse songAnalytics = genoutClient.getSongAnalytics(firstSongId, secondSongId);
        JsonNode node = mapper.convertValue(songAnalytics, JsonNode.class);

        model.addAllAttributes(Map.of(
                "firstTrack", node.get("firstTrack"),
                "secondTrack", node.get("secondTrack"),
                "similarityMetrics", node.get("similarityMetrics"),
                "popularityFactors", node.get("popularityFactors"),
                "styleFeatures", node.get("styleFeatures"),
                "temporalFeatures", node.get("temporalFeatures"),
                "similarityScore", node.get("similarityScore")
        ));
        return "features";
    }
}
