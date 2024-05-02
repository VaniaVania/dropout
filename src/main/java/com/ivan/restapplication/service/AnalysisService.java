package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

public interface AnalysisService {
    JsonNode findTopArtistsAllTime();
    List<String> findTopArtistsGenres(String term);
    String findTopTrackIds(String term);
    ArrayNode findMinMaxTrackFeatures(String feature, String term) throws JsonProcessingException;
    JsonNode findSuggestedArtists(JsonNode followedArtistsNode);
}
