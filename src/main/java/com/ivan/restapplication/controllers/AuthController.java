package com.ivan.restapplication.controllers;

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
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
public class AuthController {

    private final static String CLIENT_ID = "2c8ed13da29f45b990a1ad43ba870f7d";
    private final static String CLIENT_SECRET = "c18c855bfbe442d6aed8b9df99ae131f";
    private final static String REDIRECT_URI = "http://localhost:8082/callback";
    private final static String RESPONSE_TYPE = "code";
    private final static String GRANT_TYPE = "authorization_code";
    static String TOKEN = "";
    static String CODE = "";

    @GetMapping("/authorize")
    public RedirectView authorize(){
        String getUri = "https://accounts.spotify.com/authorize";
        getUri += "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE
                + "&show_dialog=" + true
                + "&scope=" + "playlist-modify-private playlist-modify-public ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        return new RedirectView(getUri);
    }

    @GetMapping("/callback")
    public RedirectView getSpotifyCode(@RequestParam(value = "code") String code) throws IOException {
        CODE = code;
        getSpotifyToken();
        return new RedirectView("http://localhost:8082/main");
    }

    @PostMapping("/token")
    private void getSpotifyToken() throws IOException {
        String url = "https://accounts.spotify.com/api/token";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("code" , CODE);
        body.add("redirect_uri" , REDIRECT_URI);
        body.add("client_id" , CLIENT_ID);
        body.add("client_secret" , CLIENT_SECRET);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body, headers);
        String spotifyResponse = restTemplate.postForObject(url,request,String.class);

        JsonNode obj = mapper.readTree(spotifyResponse);
        TOKEN = obj.get("access_token").toString()
                   .replace("\"","");
    }
}
