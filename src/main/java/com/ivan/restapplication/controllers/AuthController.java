package com.ivan.restapplication.controllers;

import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.AuthService;
import com.ivan.restapplication.service.SavedUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
public class AuthController {

    private final AuthService authService;
    private final SavedUserService savedUserService;
    private final RestTemplate restTemplate;

    @Autowired
    public AuthController(AuthService authService, SavedUserService savedUserService, RestTemplate restTemplate) {
        this.authService = authService;
        this.savedUserService = savedUserService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/authorize")
    public RedirectView authorize(){
        return authService.authorize();
    }

    @GetMapping("/callback")
    public RedirectView exchangeCode(@RequestParam(value = "code") String code) throws IOException {
        authService.accessToken(code);
        savedUserService.save(convertToUser(getSpotifyUser()));
        return new RedirectView("http://localhost:8082");
    }

    @GetMapping("/logout")
    public RedirectView logout(){
        return authService.logout();
    }

    public UserDTO getSpotifyUser(){
        return restTemplate.exchange("https://api.spotify.com/v1/me", HttpMethod.GET, authService.useToken(), UserDTO.class).getBody();
    }

    public User convertToUser(UserDTO userDTO){
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userDTO, User.class);
    }
}
