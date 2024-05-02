package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ivan.restapplication.model.dto.UserDto;

public interface SpotifyUserService {
    JsonNode showUserProfile() throws JsonProcessingException;
    JsonNode findTopArtists(String term) throws JsonProcessingException;
    JsonNode findTopTracks(String term) throws JsonProcessingException;
    JsonNode findFollowedArtists() throws JsonProcessingException;
    void unfollowArtists(String ids);
    void followArtists(String ids);
    UserDto getSpotifyUserDTO();
    JsonNode createPlaylist(String name) throws JsonProcessingException;
}
