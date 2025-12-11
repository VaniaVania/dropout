package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.client.GenoutClient;
import com.ivan.restapplication.dto.response.SongCollaborationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/collaborations")
@RequiredArgsConstructor
public class CollaborationController {

    private final GenoutClient genoutClient;
    private final ObjectMapper mapper;

    @GetMapping
    public String showTrackCollaborations(@RequestParam(defaultValue = "51899") String firstSongId, @RequestParam(defaultValue = "3876994") String secondSongId, Model model) {
        SongCollaborationResponse response = genoutClient.getSongCollaborations(firstSongId, secondSongId);
        JsonNode node = mapper.convertValue(response, JsonNode.class);

        model.addAllAttributes(Map.of(
                "sharedArtists", node.get("sharedArtists"),
                "sharedCollaborations", node.get("sharedCollaborations")
        ));
        return "collaboration";
    }
}
