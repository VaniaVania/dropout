package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.SpotifyGenresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GenresService implements SpotifyGenresService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JsonNode getAvailableGenresSeeds() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations/available-genre-seeds", String.class));
    }
}
