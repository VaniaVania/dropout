package com.ivan.restapplication.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ivan.restapplication.model.dto.UserDto;
import com.ivan.restapplication.service.SpotifyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpotifyUserServiceImpl implements SpotifyUserService {

    private final ObjectMapper mapper;
    private final RestTemplate restTemplate;
    private static final String ITEMS = "items";

    @Override
    public JsonNode showUserProfile() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me", String.class));
    }

    @Override
    public UserDto getSpotifyUserDTO() {
        return restTemplate.getForObject("https://api.spotify.com/v1/me", UserDto.class);
    }

    @Override
    public JsonNode findTopArtists(String term) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me/top/artists?limit=50&time_range=" + term, String.class)).get(ITEMS);
    }

    @Override
    public JsonNode findTopTracks(String term) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + term, String.class)).get(ITEMS);
    }

    @Override
    public JsonNode findFollowedArtists() throws JsonProcessingException {
        JsonNode followedArtistsJson = mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me/following?type=artist&limit=50", String.class)
                );

        ArrayNode items = (ArrayNode) followedArtistsJson.findValue(ITEMS);

        String next = followedArtistsJson.get("artists").get("next").asText();

        while (!next.equals("null")) {
            JsonNode loopFollowedArtistsJson = mapper
                    .readTree(restTemplate
                            .getForObject(next, String.class)
                    );
            for (JsonNode e : loopFollowedArtistsJson.findValue(ITEMS)) {
                items.add(e);
            }
            next = loopFollowedArtistsJson.findValue("next").asText();
        }

        List<JsonNode> sortedDataNodes = items.findParents("name")
                .stream()
                .sorted(Comparator.comparing(o -> o.get("name").asText())).toList();

        return mapper.createObjectNode().arrayNode().addAll(sortedDataNodes);  //TODO
    }

    @Override
    public void unfollowArtists(String ids) {
        restTemplate.delete("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.DELETE);
    }

    @Override
    public void followArtists(String ids) {
        restTemplate.exchange("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.PUT, HttpEntity.EMPTY, Void.class);
    }

    @Override
    public JsonNode createPlaylist(String name) throws JsonProcessingException {
        Map<String, String> playlist = new HashMap<>();
        playlist.put("name", name);
        return mapper.readTree(restTemplate.postForObject("https://api.spotify.com/v1/me/playlists", playlist, String.class));
    }
}
