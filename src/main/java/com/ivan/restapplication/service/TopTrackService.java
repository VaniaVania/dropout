package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopTrackService {

    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public TopTrackService(AuthService authService, RestTemplate restTemplate, ObjectMapper mapper) {
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public JsonNode findTopTracks(String term) throws JsonProcessingException, UnauthorizedUserException {
        return mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + term, HttpMethod.GET, authService.useToken(), String.class)
                        .getBody()).get("items");
    }

    public Map<Float, JsonNode> getTrackList(String term, String feature) throws JsonProcessingException {
        Map<Float, JsonNode> tracksListMap = new HashMap<>();  //Map with float value of feature, and track node
        //Re-arrange list to a string

        JsonNode featureJson = mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/audio-features?ids=" + getTopTracksIds(term), HttpMethod.GET, authService.useToken(), String.class)
                        .getBody());
        //Request Json for an audio features

            featureJson.get("audio_features")
                    .forEach(f -> tracksListMap
                            .put(f.get(feature).floatValue(), f.get("track_href")));     //Forming map

        return tracksListMap;
    }

    public String getTopTracksIds(String term) throws JsonProcessingException {
        List<String> tracksIds = new ArrayList<>(); // List of tracks ids

        findTopTracks(term).forEach(el -> tracksIds
                .add(el.get("id")
                        .asText()));       // Adding ids to a list

        return tracksIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "", ""));
    }

    public ArrayNode findTrackFeature(@RequestParam String feature, String term) throws JsonProcessingException {
        ArrayNode node = mapper.createArrayNode();
        Map<Float, JsonNode> trackList = getTrackList(term, feature);

        node.add(mapper
                .readTree(restTemplate
                        .exchange(trackList.get(Collections.max(trackList.keySet())).asText(), HttpMethod.GET, authService.useToken(), String.class)
                        .getBody()));

        node.add(mapper
                .readTree(restTemplate
                        .exchange(trackList.get(Collections.min(trackList.keySet())).asText(), HttpMethod.GET, authService.useToken(), String.class)
                        .getBody()));

        return node;
    }
}




