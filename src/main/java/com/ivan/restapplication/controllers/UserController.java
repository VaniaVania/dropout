package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.models.ExplicitContent;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.repository.ExplicitContentRepository;
import com.ivan.restapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static com.ivan.restapplication.controllers.AuthController.TOKEN;

@RestController
public class UserController {

    private UsersService usersService;
    private ExplicitContentRepository explicitContentRepository;

    @Autowired
    public UserController(UsersService usersService, ExplicitContentRepository explicitContentRepository) {
        this.usersService = usersService;
        this.explicitContentRepository = explicitContentRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(){
        String url = "https://api.spotify.com/v1/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
    }

    @PostMapping("/test")
    public ResponseEntity<HttpStatus> createObject(@RequestBody User user){
        usersService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
