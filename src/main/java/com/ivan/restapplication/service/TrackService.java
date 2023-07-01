package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.api.ApiBinding;
import org.springframework.beans.factory.annotation.Autowired;

public class TrackService extends ApiBinding implements SpotifyTracksService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AnalysisService analysisService;

    public TrackService(String accessToken) {
        super(accessToken);
    }

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
                        .getForObject("https://api.spotify.com/v1/audio-features?ids=" + analysisService.getTopTrackIds(term), String.class));
    }
}
