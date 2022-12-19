package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TopArtistService {

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public TopArtistService(AuthService authService, RestTemplate restTemplate, ObjectMapper mapper) {
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public JsonNode findTopArtists(String term) throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/me/top/artists?limit=10&time_range=";
        JsonNode currentUserTopArtistJson = mapper.readTree(restTemplate.exchange( url + term, HttpMethod.GET, authService.useToken(), String.class).getBody());
        return currentUserTopArtistJson.get("items");
    }
}
