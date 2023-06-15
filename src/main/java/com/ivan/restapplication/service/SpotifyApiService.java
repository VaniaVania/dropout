package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ivan.restapplication.api.ApiBinding;
import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.util.NotListeningUserException;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

public class SpotifyApiService extends ApiBinding {

    @Autowired
    private ObjectMapper mapper;

    public SpotifyApiService(String accessToken) {
        super(accessToken);
    }

    public JsonNode showUserProfile() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me", String.class));
    }

    public UserDTO getSpotifyUserDTO() {
        return restTemplate.getForObject("https://api.spotify.com/v1/me", UserDTO.class);
    }

    //ARTISTS
    public JsonNode findTopArtists(String term) throws JsonProcessingException {
        JsonNode currentUserTopArtistJson = mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me/top/artists?limit=50&time_range=" + term, String.class));
        return currentUserTopArtistJson.get("items");
    }

    public JsonNode findTopArtistsAllTime() throws JsonProcessingException {
        ArrayNode node = mapper.createArrayNode();

        List<String> terms = new ArrayList<>();
        terms.add("short_term");
        terms.add("medium_term");
        terms.add("long_term");

        for (String term : terms) {
            node.add(findTopArtists(term));
        }
        return node;
    }

    public List<String> getTopArtistsGenres(String term) throws JsonProcessingException {
        List<String> tracksIds = new ArrayList<>(); // List of tracks ids

        findTopArtists(term).findValue("genres").forEach(el -> tracksIds
                .add(el.asText()));       // Adding ids to a list

        return tracksIds;
    }

    //TRACKS
    public JsonNode findTopTracks(String term) throws JsonProcessingException, UnauthorizedUserException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me/top/tracks?limit=50&time_range=" + term, String.class)
                ).get("items");
    }

    public Map<Float, JsonNode> getTrackList(String term, String feature) throws JsonProcessingException {
        Map<Float, JsonNode> tracksListMap = new HashMap<>();  //Map with float value of feature, and track node
        //Re-arrange list to a string
        JsonNode featureJson = mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/audio-features?ids=" + getTopTracksIds(term), String.class)
                        );
        //Request Json for an audio features
        if (!featureJson.get("audio_features").asText().equals("[null]")) {
            featureJson.get("audio_features")
                    .forEach(f -> tracksListMap
                            .put(f.get(feature).floatValue(), f.get("track_href")));     //Forming map
        }
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

    public ArrayNode findTrackFeature(@RequestParam String feature, String term) throws JsonProcessingException, NotListeningUserException {
        ArrayNode node = mapper.createArrayNode();
        Map<Float, JsonNode> trackList = getTrackList(term, feature);

        node.add(mapper
                .readTree(restTemplate
                        .getForObject(trackList.get(Collections.max(trackList.keySet())).asText(), String.class)
                        ));

        node.add(mapper
                .readTree(restTemplate
                        .getForObject(trackList.get(Collections.min(trackList.keySet())).asText(), String.class)
                        ));
        return node;
    }

    //MANAGE
    private JsonNode followedArtistNode;
    public JsonNode getFollowedArtists() throws JsonProcessingException {
        JsonNode followedArtistsJson = mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/me/following?type=artist&limit=50", String.class)
                        );

        ArrayNode items = (ArrayNode) followedArtistsJson.findValue("items");

        String next = followedArtistsJson.get("artists").get("next").asText();

        while (!next.equals("null")) {
            JsonNode loopFollowedArtistsJson = mapper
                    .readTree(restTemplate
                            .getForObject(next, String.class)
                            );
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
        ArrayNode suggestNode = mapper.createArrayNode();

        for (JsonNode termNode: findTopArtistsAllTime()) {
            for (JsonNode el : termNode) {
                if (!followedArtistNode.findValuesAsText("id").contains(el.get("id").asText()) && !suggestNode.findValuesAsText("id").contains(el.get("id").asText())) {
                    suggestNode.add(el);
                }
            }
        }

        return suggestNode;
    }

    public void unfollowArtists(String ids) {
        restTemplate.delete("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.DELETE);
    }

    public void followArtists(String ids) {
        restTemplate.put("https://api.spotify.com/v1/me/following?type=artist&ids=" + ids, HttpMethod.PUT);
    }

    public JsonNode getSpotifySuggestedTracks(String seed_artists, String seed_tracks, String seed_genres) throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations?seed_artists=" + seed_artists + "&seed_genres=" + seed_genres + "&seed_tracks=" + seed_tracks, String.class));

    }

    public JsonNode getAvailableGenresSeeds() throws JsonProcessingException {
        return mapper
                .readTree(restTemplate
                        .getForObject("https://api.spotify.com/v1/recommendations/available-genre-seeds", String.class));
    }

    public Object createPlaylist(String name) {
        Map<String, String> playlist = new HashMap<String, String>();
        playlist.put("name", name);

        Object response = this.restTemplate.postForObject("https://api.spotify.com/v1/me/playlists", playlist, Object.class);

        return response;
    }
}