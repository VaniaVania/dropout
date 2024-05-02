package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.SpotifyTracksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SpotifyTracksServiceImpl implements SpotifyTracksService {

    private final ObjectMapper mapper;
    private final AnalysisServiceImpl analysisService;
    private final RestTemplate restTemplate;

    @Override
    public JsonNode getRecommendations(String seedArtists, String seedTracks, String seedGenres) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations?seed_artists=" + seedArtists + "&seed_genres=" + seedGenres + "&seed_tracks=" + seedTracks, String.class));
    }

    @Override
    public JsonNode getTracksAudioFeatures(String feature, String term) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/audio-features?ids=" + analysisService.findTopTrackIds(term), String.class));
    }
}
