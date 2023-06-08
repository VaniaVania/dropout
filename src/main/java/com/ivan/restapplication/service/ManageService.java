package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManageService {

    private final AuthService authService;
    private final TopArtistService topArtistService;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private JsonNode followedArtistNode;

    @Autowired
    public ManageService(AuthService authService, TopArtistService topArtistService, RestTemplate restTemplate, ObjectMapper mapper) {
        this.authService = authService;
        this.topArtistService = topArtistService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public JsonNode getFollowedArtists() throws JsonProcessingException {
        JsonNode followedArtistsJson = mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/me/following?type=artist&limit=50",
                                HttpMethod.GET,
                                authService.useToken(),
                                String.class)
                        .getBody());

        ArrayNode items = (ArrayNode) followedArtistsJson.findValue("items");
        String next = followedArtistsJson.get("artists").get("next").asText();

        while (!next.equals("null")) {
            JsonNode loopFollowedArtistsJson = mapper
                    .readTree(restTemplate
                            .exchange(next, HttpMethod.GET, authService.useToken(), String.class)
                            .getBody());
            for (JsonNode e : loopFollowedArtistsJson.findValue("items")) {
                items.add(e);
            }
            next = loopFollowedArtistsJson.findValue("next").asText();
        }

        List<JsonNode> sortedDataNodes = items.findParents("name")
                .stream()
                .sorted(Comparator.comparing(o -> o.get("name").asText()))
                .collect(Collectors.toList());
        //create ArrayNode
        followedArtistNode = mapper.createObjectNode().arrayNode().addAll(sortedDataNodes);
        return followedArtistNode;
    }

    public JsonNode getSuggestedArtists() throws JsonProcessingException {
        ArrayNode suggest = mapper.createArrayNode();
        JsonNode artists = topArtistService.findTopArtistsAllTime();
        JsonNode followed = getFollowedArtistNode(); //half-time method

        for (JsonNode artist : artists) {
            for (JsonNode el : artist) {
                if (!followed.findValuesAsText("id").contains(el.get("id").asText()) && !suggest.findValuesAsText("id").contains(el.get("id").asText())) {
                    suggest.add(el);
                }
            }
        }
        return suggest;
    }

    public void unfollowArtists(String ids) {
        restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.DELETE, authService.useToken(), String.class);
    }

    public void followArtists(String ids) {
        restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.PUT, authService.useToken(), String.class);
    }

    public JsonNode getSpotifySuggestedTracks(String seed_artists, String seed_tracks, String seed_genres) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/recommendations?seed_artists=" + seed_artists + "&seed_genres=" + seed_genres + "&seed_tracks=" + seed_tracks, HttpMethod.GET, authService.useToken(), String.class).getBody());

    }

    public JsonNode getAvailableGenresSeeds() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .exchange("https://api.spotify.com/v1/recommendations/available-genre-seeds", HttpMethod.GET, authService.useToken(), String.class).getBody());
    }

    public void setFollowedArtistNode(JsonNode node) {
        this.followedArtistNode = node;
    }

    public JsonNode getFollowedArtistNode() {
        return followedArtistNode;
    }
}