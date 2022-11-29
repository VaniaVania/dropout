package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import static com.ivan.restapplication.controllers.AuthController.TOKEN;

@Controller
public class MainController {
    @GetMapping("/main")
    public String mainPage(Model model){

        if(TOKEN!=null){
            model.addAttribute("isAuthorized", true);
        }
        return "main";
    }

    @GetMapping("/profile")
    public String myProfile(Model model) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();


        JsonNode currentUserProfileJson = mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/me", HttpMethod.GET, entity, String.class).getBody());
        JsonNode currentUserTopArtistsJson = mapper.readTree(restTemplate.exchange("https://api.spotify.com/v1/me/top/artists?limit=5", HttpMethod.GET, entity, String.class).getBody());

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

        //Artist`s card
        model.addAttribute("artistImages", currentUserTopArtistsJson.get("items")
                                                                              .findValues("images")
                                                                              .stream().map(p -> p.findValues("url")
                                                                              .remove(0)
                                                                              .asText())
                                                                              .toList());

        model.addAttribute("artistsNames", currentUserTopArtistsJson.get("items")
                .findValues("name"));



        return "profile";
    }

    @GetMapping("/artist")
    public String artist(){
        return "artist";
    }
}
