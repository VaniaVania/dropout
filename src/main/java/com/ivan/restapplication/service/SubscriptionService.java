package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionService {

    private final TopArtistService topArtistService;
    private final UserService userService;
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public SubscriptionService(TopArtistService topArtistService, UserService userService, AuthService authService, RestTemplate restTemplate, ObjectMapper mapper) {
        this.topArtistService = topArtistService;
        this.userService = userService;
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public JsonNode getSuggestedArtists() throws JsonProcessingException {
        ArrayNode suggestNode = mapper.createArrayNode();
        JsonNode followedNode = userService.getFollowedArtists();

        List<String> terms = new ArrayList<>();
        terms.add("short_term");
        terms.add("medium_term");
        terms.add("long_term");

        for (String term : terms) {
            for (JsonNode el : topArtistService.findTopArtists(term)) {
                if (!followedNode.findValuesAsText("id").contains(el.get("id").asText()) && !suggestNode.findValuesAsText("id").contains(el.get("id").asText())) {
                    suggestNode.add(el);
                }
            }
        }
        return suggestNode;
    }

    public JsonNode getSpotifySuggestedArtists(String seed_artists,String seed_tracks,String seed_genres) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/recommendations?seed_artists=" + seed_artists + "&seed_genres=" + seed_genres + "&seed_tracks=" + seed_tracks, HttpMethod.GET, authService.useToken(), String.class).getBody());

    }

    public JsonNode getAvailableGenresSeeds() throws JsonProcessingException {

        return mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/recommendations/available-genre-seeds", HttpMethod.GET, authService.useToken(), String.class).getBody());
    }



    public void unfollowArtists(String ids) {
        restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.DELETE, authService.useToken(), String.class);
    }

    public void followArtists(String ids) {
        restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.PUT, authService.useToken(), String.class);
    }
}
