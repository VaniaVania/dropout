package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import static com.ivan.restapplication.controllers.AuthController.TOKEN;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage(){
        return "main";
    }

    @GetMapping("/profile")
    public String myProfile(Model model) throws JsonProcessingException {
        String url = "https://api.spotify.com/v1/me";

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Object> entity = new HttpEntity<>(headers);
        String response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

        JsonNode object = mapper.readTree(String.valueOf(response));

        model.addAttribute("display_name", object.get("display_name"));
        model.addAttribute("followers", object.get("followers").get("total"));
        model.addAttribute("country", object.get("country"));
        model.addAttribute("images", object.get("images")
                .findValues("url")
                .get(0)
                .toString()
                .replace("\"",""));
        model.addAttribute("external_urls", object.get("external_urls")
                .get("spotify")
                .toString()
                .replace("\"",""));

        return "profile";
    }


}
