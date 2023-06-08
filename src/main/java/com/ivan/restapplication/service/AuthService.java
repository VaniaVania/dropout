package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.properties.SpotifyProperties;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class AuthService{

    private final SpotifyProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public AuthService(RestTemplate restTemplate, ObjectMapper mapper, SpotifyProperties properties) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.properties = properties;
    }


    public String authorize(){
        String getUri = "https://accounts.spotify.com/authorize";
        getUri += "?client_id=" + properties.getClientId()
                + "&redirect_uri=" + properties.getRedirectUri()
                + "&response_type=" + properties.getResponseType()
                + "&show_dialog=" + properties.isShowDialog()
                + "&scope=" + properties.getScope();
        return getUri;
    }

    public void accessToken(String code) throws JsonProcessingException, UnauthorizedUserException {
            properties.setCode(code);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", properties.getGrantType());
            body.add("code", properties.getCode());
            body.add("redirect_uri", properties.getRedirectUri());
            body.add("client_id", properties.getClientId());
            body.add("client_secret", properties.getClientSecret());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
            JsonNode obj =  mapper.readTree(restTemplate.postForObject("https://accounts.spotify.com/api/token",
                    request,
                    String.class));

            properties.setToken(obj.get("access_token").asText());
            properties.setRefreshToken(obj.get("refresh_token").asText());
    }

    public void refreshToken() throws JsonProcessingException{
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + DatatypeConverter.printBase64Binary((properties.getClientId() + ":" + properties.getClientSecret()).getBytes(StandardCharsets.UTF_8)));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", properties.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        JsonNode obj = mapper.readTree(restTemplate.postForObject("https://accounts.spotify.com/api/token",
                request,
                String.class));
        properties.setToken(obj.get("access_token").asText());
    }

    public HttpEntity<Object> useToken() throws UnauthorizedUserException {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + properties.getToken());
            return new HttpEntity<>(headers);
    }

    public void logout() {
        properties.setToken(null);
        restTemplate.getForObject("https://accounts.spotify.com/logout", String.class);
    }

}








