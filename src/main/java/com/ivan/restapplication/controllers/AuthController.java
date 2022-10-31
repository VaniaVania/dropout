package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    final static String CLIENT_ID = "2c8ed13da29f45b990a1ad43ba870f7d";
    final static String CLIENT_SECRET = "c18c855bfbe442d6aed8b9df99ae131f";
    private final static String REDIRECT_URI = "http://localhost:8082/main";
    private final static String RESPONSE_TYPE = "code";
    private final static String GRANT_TYPE = "authorization_code";
    static String TOKEN = "";
    static String PLAYLIST_ID = "";


    @GetMapping("/callback")
    public void getSpotifyCode(HttpServletResponse servletResponse) throws IOException{
        String getUri = "https://accounts.spotify.com/authorize";
        getUri += "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE
                + "&show_dialog=" + true
                + "&scope=" + "playlist-modify-private playlist-modify-public";
        servletResponse.sendRedirect(getUri);
    }

    @PostMapping("/token")
    private void exchangeCode( HttpServletResponse response) throws IOException {
        String url = "https://accounts.spotify.com/api/token";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("code" , MainController.CODE);
        body.add("redirect_uri" , REDIRECT_URI);
        body.add("client_id" , CLIENT_ID);
        body.add("client_secret" , CLIENT_SECRET);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body, headers);
        String spotifyResponse = restTemplate.postForObject(url,request,String.class);

        JsonNode obj = mapper.readTree(spotifyResponse);
        TOKEN = obj.get("access_token").toString()
                   .replace("\"","");
        response.sendRedirect(REDIRECT_URI);
        System.out.println(TOKEN);
    }

    @PostMapping("/playlist")
    public String getArtistInformation() throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/users/31mwjjv3r2up4fsautc4cpzqhegm/playlists";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String,Object> body = new HashMap<>();
        body.put("name", "Ohhh LALALALLALALA");
        body.put("description" , "El Chapo Nigga i`m The GOD , GOD , GOD. This is my halloween. I am crazy");
        body.put("public", true);

        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body,headers);
        String response = restTemplate.postForObject(url,entity,String.class);
        JsonNode obj = mapper.readTree(response);

        PLAYLIST_ID = obj.get("id").toString()
                .replace("\"","");
        return response;
    }

    @PostMapping("/song")
    public String addSong(){
        String url = "https://api.spotify.com/v1/playlists/" + PLAYLIST_ID +  "/tracks";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        Map<String, List<String>> body = new HashMap<>();
        List<String> tracks = new ArrayList<>();
        tracks.add("spotify:track:4emwJBzykVyl76M9HGDhz4");
        tracks.add("spotify:track:2OIVxvKa7XYSiZO7IYyIK5");
        tracks.add("spotify:track:28MRSuy6PBcR42LbNldzj0");
        body.put("uris", tracks );

        HttpEntity<Map<String,List<String>>> entity = new HttpEntity<>(body,headers);
        return restTemplate.postForObject(url,entity,String.class);
    }
}
