package com.ivan.restapplication.controllers;

import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.AuthService;
import com.ivan.restapplication.service.SavedUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
public class AuthController {

    private final AuthService authService;
    private final SavedUserService savedUserService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService, SavedUserService savedUserService, ModelMapper modelMapper) {
        this.authService = authService;
        this.savedUserService = savedUserService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/authorize")
    public RedirectView authorize(){
        return authService.authorize();
    }

    @GetMapping("/callback")
    public RedirectView exchangeCode(@RequestParam(value = "code") String code) throws IOException {
        authService.accessToken(code);
        savedUserService.save(convertToUser(savedUserService.getSpotifyUserDTO()));
        return new RedirectView("/");
    }

    @GetMapping("/mylogout")
    public RedirectView logout() {
        return authService.logout();
    }

    @GetMapping("/oauth2/authorize-client")
    public RedirectView authorizationSpotify(){
        return new RedirectView("https://accounts.spotify.com/authorize");
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
