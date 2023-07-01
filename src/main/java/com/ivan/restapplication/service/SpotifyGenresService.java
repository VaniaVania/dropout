package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface SpotifyGenresService {
    JsonNode getAvailableGenresSeeds() throws JsonProcessingException;
}
