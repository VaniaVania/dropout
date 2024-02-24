package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ivan.restapplication.exception.NotListeningUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService{

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(cacheNames = "topArtistsCache")
    public JsonNode findTopArtistsAllTime() throws JsonProcessingException {
        ArrayNode node = mapper.createArrayNode();

        List<String> terms = new ArrayList<>();
        terms.add("short_term");
        terms.add("medium_term");
        terms.add("long_term");

        for (String term : terms) {
            node.add(userService.findTopArtists(term));
        }
        return node;
    }

    public List<String> findTopArtistsGenres(String term) throws JsonProcessingException {
        List<String> tracksIds = new ArrayList<>(); // List of tracks ids

        userService.findTopArtists(term).findValue("genres").forEach(el -> tracksIds
                .add(el.asText()));       // Adding ids to a list
        return tracksIds;
    }


    public String findTopTrackIds(String term) throws JsonProcessingException {
        List<String> trackIds = new ArrayList<>(); // List of tracks ids
        JsonNode topTracksNode = userService.findTopTracks(term);

        topTracksNode.forEach(el -> trackIds
                .add(el.get("id").asText()));       // Adding ids to a list
        return trackIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "", ""));
    }


    public ArrayNode findMinMaxTrackFeatures(String feature, String term) throws JsonProcessingException, NotListeningUserException {
        Map<Float, JsonNode> trackListMap = new TreeMap<>();  //Map with float value of feature, and track href
        String topIds = findTopTrackIds(term);
        ArrayNode minMaxValueNode = mapper.createArrayNode();

        JsonNode featureNode = mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/audio-features?ids=" + topIds, String.class));

        if (!featureNode.get("audio_features").asText().equals("[null]")) {
            featureNode.get("audio_features")
                    .forEach(f -> trackListMap.put(f.get(feature).floatValue(), f.get("track_href")));     //Forming map  --> [ 0.5 , href ]
        }

        minMaxValueNode.add(mapper
                .readTree(restTemplate
                        .getForObject(trackListMap.get(Collections.max(trackListMap.keySet())).asText(), String.class)
                ));   //max value

        minMaxValueNode.add(mapper
                .readTree(restTemplate
                        .getForObject(trackListMap.get(Collections.min(trackListMap.keySet())).asText(), String.class)
                ));  //min value
        return minMaxValueNode;
    }

    @Cacheable(cacheNames = "suggestCache",key = "#followedArtistsNode")
    public JsonNode findSuggestedArtists(JsonNode followedArtistsNode) throws JsonProcessingException {
        ArrayNode suggestNode = mapper.createArrayNode();
        JsonNode topArtistsAllTimeNode = findTopArtistsAllTime();
        List<String> followedArtistsNodeIds = followedArtistsNode.findValuesAsText("id");

        for (JsonNode termNode: topArtistsAllTimeNode) {
            for (JsonNode el : termNode) {
                if (!followedArtistsNodeIds.contains(el.get("id").asText()) && !suggestNode.findValuesAsText("id").contains(el.get("id").asText())) {
                    suggestNode.add(el);
                }
            }
        }
        return suggestNode;
    }
}
