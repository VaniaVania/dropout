package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        JsonNode currentUserTopTracksJson = mapper.readTree(restTemplate.exchange( "https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + term, HttpMethod.GET, authService.useToken(), String.class).getBody());
        return currentUserTopTracksJson.get("items");
    }

    public Map<Float,JsonNode> getTrackList(String term, String feature) throws JsonProcessingException {
        Map<Float,JsonNode> tracksList = new HashMap<>();

        List<String> trackIds = new ArrayList<>();
        findTopTracks(term).forEach(el -> trackIds.add(el.get("id").asText()));
        String topTracksIdsString = trackIds.stream().map(String::valueOf).collect(Collectors.joining(",","",""));

        JsonNode featureJson = mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/audio-features?ids=" + topTracksIdsString, HttpMethod.GET, authService.useToken(), String.class).getBody());
        for (JsonNode features: featureJson.get("audio_features")) {
            tracksList.put(features.get(feature).floatValue(), features.get("track_href"));
        }
        return tracksList;
    }

    public JsonNode findTrackFeature(String type, @RequestParam(defaultValue = "valence") String feature, String term) throws JsonProcessingException {
        if(type.equals("positive")){
            JsonNode maxValueTrack = mapper.readTree(restTemplate.exchange(getTrackList(term,feature).get(Collections.max(getTrackList(term,feature).keySet())).asText(),HttpMethod.GET,authService.useToken(), String.class).getBody());
            return maxValueTrack;  //Max value from the map
        }else if(type.equals("negative")){
            JsonNode minValueTrack = mapper.readTree(restTemplate.exchange(getTrackList(term,feature).get(Collections.min(getTrackList(term,feature).keySet())).asText(),HttpMethod.GET,authService.useToken(), String.class).getBody());
            return minValueTrack;  //Min value from the map
        }
        return null;
    }


}




