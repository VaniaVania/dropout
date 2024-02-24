package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.SpotifyTracksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrackService implements SpotifyTracksService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JsonNode getRecommendations(String seed_artists, String seed_tracks, String seed_genres) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations?seed_artists=" + seed_artists + "&seed_genres=" + seed_genres + "&seed_tracks=" + seed_tracks, String.class));
    }

    @Override
    public JsonNode getTracksAudioFeatures(String feature, String term) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/audio-features?ids=" + analysisService.findTopTrackIds(term), String.class));
    }
}
