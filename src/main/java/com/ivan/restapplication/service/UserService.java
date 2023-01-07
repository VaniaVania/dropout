package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AuthService authService;
    private final TopArtistService artistService;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public UserService(AuthService authService, RestTemplate restTemplate, TopArtistService artistService, ObjectMapper mapper) {
        this.authService = authService;
        this.artistService = artistService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public JsonNode showUserProfile() throws JsonProcessingException {
        return mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/me", HttpMethod.GET, authService.useToken(), String.class).getBody());
    }

    public JsonNode getFollowedArtists() throws JsonProcessingException {

        JsonNode followedArtistsJson = mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&limit=50", HttpMethod.GET, authService.useToken(), String.class).getBody());
        JsonNode items = followedArtistsJson.get("artists").findValue("items");

        String next = followedArtistsJson.get("artists").get("next").asText();

        while (!next.equals("null")){
            JsonNode loopFollowedArtistsJson = mapper.readTree(restTemplate.exchange(next, HttpMethod.GET, authService.useToken(), String.class).getBody());
            for (JsonNode e: loopFollowedArtistsJson.get("artists").get("items")){
                ((ArrayNode)items).add(e);
            }
            next = loopFollowedArtistsJson.get("artists").get("next").asText();
        }

        List<JsonNode> sortedDataNodes = items.findParents("name")
                .stream()
                .sorted(Comparator.comparing(o -> o.get("name").asText()))
                .collect(Collectors.toList());
        //return the same Json structure as in method parameter
        return mapper.createObjectNode().arrayNode().addAll(sortedDataNodes);
    }

    public JsonNode suggestArtists() throws JsonProcessingException {
        JsonNode followedArtists = getFollowedArtists();

        ArrayNode suggestNode = mapper.createArrayNode();

        for (JsonNode el: artistService.findTopArtists("long_term")){
            if (!followedArtists.findValuesAsText("id").contains(el.get("id").asText())){
                suggestNode.add(el);
            }
        }
         return suggestNode;
    }

    public void unfollowArtist(@RequestParam String ids){
         restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.DELETE, authService.useToken(), String.class);
    }

    public void followArtist(@RequestParam String ids){
        restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.PUT, authService.useToken(), String.class);
    }


}
