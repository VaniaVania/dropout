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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.bind.DatatypeConverter;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
//@SessionScope
public class AuthService{

    private final String PORT = "8085";
    private final String HOSTNAME = "http://localhost:8085";
    private final String CLIENT_ID = "2c8ed13da29f45b990a1ad43ba870f7d";
    private final String CLIENT_SECRET = "c18c855bfbe442d6aed8b9df99ae131f";
    public final String REDIRECT_URI = HOSTNAME +  "/callback";
    private final String RESPONSE_TYPE = "code";
    private final String GRANT_TYPE = "authorization_code";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private String token = null;
    private String refreshToken = null;

    @Autowired
    public AuthService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public RedirectView authorize(){
        String getUri = "https://accounts.spotify.com/authorize";
        getUri += "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE
                + "&show_dialog=" + true
                + "&scope=" + "playlist-modify-private playlist-modify-public ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        return new RedirectView(getUri);
    }

    public void accessToken(String code) throws JsonProcessingException, UnauthorizedUserException {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", GRANT_TYPE);
            body.add("code", code);
            body.add("redirect_uri", REDIRECT_URI);
            body.add("client_id", CLIENT_ID);
            body.add("client_secret", CLIENT_SECRET);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            JsonNode obj =  mapper.readTree(restTemplate.postForObject("https://accounts.spotify.com/api/token", request, String.class));

            setToken(obj.get("access_token").asText());
            setRefreshToken(obj.get("refresh_token").asText());
    }

    public void refreshToken() throws JsonProcessingException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + DatatypeConverter.printBase64Binary((CLIENT_ID + ":" + CLIENT_SECRET).getBytes(StandardCharsets.UTF_8)));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        JsonNode obj =  mapper.readTree(restTemplate.postForObject("https://accounts.spotify.com/api/token", request, String.class));
        setToken(obj.get("access_token").asText());
    }

    public HttpEntity<Object> useToken() throws UnauthorizedUserException {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + getToken());
            return new HttpEntity<>(headers);
    }

    public RedirectView logout() {
        setToken(null);
        restTemplate.getForObject("https://accounts.spotify.com/logout", String.class);
        return new RedirectView("/");
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }

    public String getREDIRECT_URI() {
        return REDIRECT_URI;
    }

    public String getRESPONSE_TYPE() {
        return RESPONSE_TYPE;
    }

    public String getGRANT_TYPE() {
        return GRANT_TYPE;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPORT() {
        return PORT;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}








