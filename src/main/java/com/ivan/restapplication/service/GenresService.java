package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.api.ApiBinding;
import org.springframework.beans.factory.annotation.Autowired;

public class GenresService extends ApiBinding implements SpotifyGenresService {

    @Autowired
    private ObjectMapper mapper;

    public GenresService(String accessToken) {
        super(accessToken);
    }

    @Override
    public JsonNode getAvailableGenresSeeds() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations/available-genre-seeds", String.class));
    }
}
