package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class MainController {

    private final AuthorizationService authorizationService;
    public final RestTemplate restTemplate;

    @Autowired
    public MainController(AuthorizationService authorizationService, RestTemplate restTemplate) {
        this.authorizationService = authorizationService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/main")
    public String mainPage(Model model){
        if(authorizationService.getToken()!=null){
            model.addAttribute("isAuthorized", true);
        }
        return "main";
    }

    @GetMapping("/profile")
    public String myProfile(@RequestParam(value = "time_range",defaultValue = "short_term") String time_range, Model model) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authorizationService.getToken());
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode currentUserProfileJson = mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/me", HttpMethod.GET, entity, String.class).getBody());
        JsonNode currentUserTopArtistsJson = mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/me/top/artists?limit=5&time_range=" + time_range, HttpMethod.GET, entity, String.class).getBody());

        //User Card
        model.addAttribute("images", currentUserProfileJson.get("images")
                .findValues("url")
                .get(0)
                .asText());
        model.addAttribute("display_name", currentUserProfileJson.get("display_name"));
        model.addAttribute("followers", currentUserProfileJson.get("followers").get("total"));
        model.addAttribute("country", currentUserProfileJson.get("country"));

        model.addAttribute("external_urls", currentUserProfileJson.get("external_urls")
                .get("spotify")
                .asText());


        //Artist cards
        model.addAttribute("items", currentUserTopArtistsJson.get("items"));

        return "profile";
    }

    @GetMapping("/artist")
    public String artist(){
        return "artist";
    }
}
