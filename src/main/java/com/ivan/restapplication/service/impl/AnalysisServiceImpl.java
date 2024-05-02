package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ivan.restapplication.exception.NotListeningUserException;
import com.ivan.restapplication.model.enums.Term;
import com.ivan.restapplication.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final ObjectMapper mapper;
    private final SpotifyUserServiceImpl spotifyUserServiceImpl;
    private final RestTemplate restTemplate;

    @Override
    @Cacheable(cacheNames = "topArtistsCache")
    public JsonNode findTopArtistsAllTime() {
        ArrayNode node = mapper.createArrayNode();
        Arrays.stream(Term.values()).forEach(term -> {
            try {
                node.add(spotifyUserServiceImpl.findTopArtists(term.getValue()));
            } catch (JsonProcessingException e) {
                throw new NotListeningUserException();
            }
        });
        return node;
    }

    @Override
    @SneakyThrows
    public List<String> findTopArtistsGenres(String term) {
        List<String> tracksIds = new ArrayList<>(); // List of tracks ids

        spotifyUserServiceImpl.findTopArtists(term).findValue("genres").forEach(el -> tracksIds
                .add(el.asText()));       // Adding ids to a list
        return tracksIds;
    }

    @Override
    @SneakyThrows
    public String findTopTrackIds(String term) {
        List<String> trackIds = new ArrayList<>(); // List of tracks ids
        JsonNode topTracksNode = spotifyUserServiceImpl.findTopTracks(term);

        topTracksNode.forEach(el -> trackIds
                .add(el.get("id").asText()));       // Adding ids to a list
        return trackIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "", ""));
    }

    @Override
    public ArrayNode findMinMaxTrackFeatures(String feature, String term) throws JsonProcessingException {
        Map<Float, JsonNode> trackListMap = new TreeMap<>();  //Map with float value of feature, and track href
        String topIds = findTopTrackIds(term);
        ArrayNode minMaxValueNode = mapper.createArrayNode();

        JsonNode featureNode = mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/audio-features?ids=" + topIds, String.class));

        if (!featureNode.get("audio_features").toString().equals("[null]")) {
            featureNode.get("audio_features")
                    .forEach(f -> trackListMap.put(f.get(feature).floatValue(), f.get("track_href")));     //Forming map  --> [ 0.5 , href ]

            minMaxValueNode.add(mapper
                    .readTree(restTemplate
                            .getForObject(trackListMap.get(Collections.max(trackListMap.keySet())).asText(), String.class)
                    ));   //max value

            minMaxValueNode.add(mapper
                    .readTree(restTemplate
                            .getForObject(trackListMap.get(Collections.min(trackListMap.keySet())).asText(), String.class)
                    ));  //min value
        } else {
            throw new NotListeningUserException();
        }
        return minMaxValueNode;
    }

    @Override
    @Cacheable(cacheNames = "suggestCache", key = "#followedArtistsNode")
    public JsonNode findSuggestedArtists(JsonNode followedArtistsNode) {
        ArrayNode suggestNode = mapper.createArrayNode();
        JsonNode topArtistsAllTimeNode = findTopArtistsAllTime();
        List<String> followedArtistsNodeIds = followedArtistsNode.findValuesAsText("id");

        for (JsonNode termNode : topArtistsAllTimeNode) {
            for (JsonNode el : termNode) {
                if (!followedArtistsNodeIds.contains(el.get("id").asText()) && !suggestNode.findValuesAsText("id").contains(el.get("id").asText())) {
                    suggestNode.add(el);
                }
            }
        }
        return suggestNode;
    }
}
