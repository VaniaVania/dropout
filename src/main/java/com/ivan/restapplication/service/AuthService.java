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
import java.net.InetAddress;

@Service
@Transactional
public class AuthService {

    private final String port = "8085";
    private final String hostname = "http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + port;
    private final String CLIENT_ID = "2c8ed13da29f45b990a1ad43ba870f7d";
    private final String CLIENT_SECRET = "c18c855bfbe442d6aed8b9df99ae131f";
    private final String REDIRECT_URI = hostname +  "/callback";
    private final String RESPONSE_TYPE = "code";
    private final String GRANT_TYPE = "authorization_code";
    private String token = null;
    private String code = null;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public AuthService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    public RedirectView authorize() {
        String getUri = "https://accounts.spotify.com/authorize";
        getUri += "?client_id=" + CLIENT_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&response_type=" + RESPONSE_TYPE
                + "&show_dialog=" + true
                + "&scope=" + "playlist-modify-private playlist-modify-public ugc-image-upload user-read-playback-state user-modify-playback-state user-read-currently-playing app-remote-control streaming playlist-read-private playlist-read-collaborative playlist-modify-private playlist-modify-public user-follow-modify user-follow-read user-read-playback-position user-top-read user-read-recently-played user-library-modify user-library-read user-read-email user-read-private";
        return new RedirectView(getUri);
    }

    public void accessToken(String code) throws JsonProcessingException, UnauthorizedUserException {
        setCode(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", GRANT_TYPE);
        body.add("code", this.code);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        String spotifyResponse = restTemplate.postForObject("https://accounts.spotify.com/api/token", request, String.class);

        JsonNode obj = mapper.readTree(spotifyResponse);
        setToken(obj.get("access_token").toString()
                .replace("\"", ""));

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPort() {
        return port;
    }
}








