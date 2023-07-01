package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface SpotifyTracksService {
    JsonNode getRecommendations(String seed_artists, String seed_tracks, String seed_genres) throws JsonProcessingException;

    JsonNode getTracksAudioFeatures(String feature, String term) throws JsonProcessingException;

}
