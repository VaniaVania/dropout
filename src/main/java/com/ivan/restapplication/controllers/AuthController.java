package com.ivan.restapplication.controllers;

import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.AuthorizationService;
import com.ivan.restapplication.service.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
public class AuthController {

    private final AuthorizationService authorizationService;
    private final UsersService usersService;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthController(AuthorizationService authorizationService, UsersService usersService, RestTemplate restTemplate) {
        this.authorizationService = authorizationService;
        this.usersService = usersService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/authorize")
    public RedirectView authorize(){
        return authorizationService.authorize();
    }

    @GetMapping("/callback")
    public RedirectView exchangeCode(@RequestParam(value = "code") String code) throws IOException {
        authorizationService.accessToken(code);
        saveSpotifyUser();
        return new RedirectView("http://localhost:8082/main");
    }

    @GetMapping("/save")
    public UserDTO getSpotifyUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + authorizationService.getToken());
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        return restTemplate.exchange("https://api.spotify.com/v1/me", HttpMethod.GET, entity, UserDTO.class).getBody();
    }

    @PostMapping("/save")
    public void saveSpotifyUser(){
        usersService.save(convertToUser(getSpotifyUser()));
    }

    public User convertToUser(UserDTO userDTO){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userDTO, User.class);
    }

    @GetMapping("/logout")
    public RedirectView logout(){
        return authorizationService.logout();
    }
}
