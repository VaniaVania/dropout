package com.ivan.restapplication.controllers;

import com.ivan.restapplication.models.User;
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

    private final UsersService usersService;


    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/real")
    public ResponseEntity<HttpStatus> getUser(@RequestBody(required = false) User user){
        String url = "https://api.spotify.com/v1/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        user = restTemplate.exchange(url,HttpMethod.GET,entity,User.class).getBody();
        usersService.ifEmptySave(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //solve with @RequestBody




}
