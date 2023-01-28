package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        JsonNode currentUserTopArtistJson = mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/me/top/artists?limit=50&time_range=" + term, HttpMethod.GET, authService.useToken(), String.class)
                        .getBody());
        return currentUserTopArtistJson.get("items");
    }


    public List<String> getTopArtistsGenres(String term) throws JsonProcessingException {
        List<String> tracksIds = new ArrayList<>(); // List of tracks ids

        findTopArtists(term).findValue("genres").forEach(el -> tracksIds
                .add(el.asText()));       // Adding ids to a list

        return tracksIds;
    }
}
