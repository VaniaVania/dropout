package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.SpotifyGenresService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SpotifyGenresServiceImpl implements SpotifyGenresService {

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;

    @Override
    public JsonNode getAvailableGenresSeeds() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations/available-genre-seeds", String.class));
    }
}
