package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthService {

    private final String client_id = "2c8ed13da29f45b990a1ad43ba870f7d";
    private final String client_secret = "c18c855bfbe442d6aed8b9df99ae131f";
    private final String redirect_uri = "http://localhost:8082/callback";
    private final String response_type = "code";
    private final String grant_type = "authorization_code";
    private String token = null;
    private String code = null;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public AuthService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public RedirectView authorize(){
        String getUri = "https://accounts.spotify.com/authorize";
        getUri += "?client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&response_type=" + response_type
                + "&show_dialog=" + true
                + "&scope=" + "playlist-modify-private playlist-modify-public ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        return new RedirectView(getUri);
    }

    public void accessToken(String code) throws JsonProcessingException, UnauthorizedUserException {
        setCode(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", grant_type);
        body.add("code" , this.code);
        body.add("redirect_uri" , redirect_uri);
        body.add("client_id" , client_id);
        body.add("client_secret" , client_secret);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body, headers);
        String spotifyResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token",request,String.class);

        JsonNode obj = mapper.readTree(spotifyResponse);
        setToken(obj.get("access_token").toString()
                .replace("\"", ""));

    }


    public HttpEntity<Object> useToken() throws UnauthorizedUserException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getToken());
        return new HttpEntity<>(headers);
    }

    public RedirectView logout(){
        setToken(null);
        restTemplate.getForObject("https://accounts.spotify.com/logout",String.class);
        return new RedirectView("http://localhost:8082");
    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getResponse_type() {
        return response_type;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}








