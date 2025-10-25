package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

@Deprecated(since = "1.0.0", forRemoval = false)
public interface SpotifyGenresService {

    JsonNode getAvailableGenresSeeds() throws JsonProcessingException;
}
