package com.ivan.restapplication.controllers;

import com.ivan.restapplication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class MainController {

    private final AuthService authService;

    @Autowired
    public MainController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping()
    public String mainPage(Model model){
        if(authService.getToken()!=null){
            model.addAttribute("isAuthorized", true);
        }
        return "main";
    }

    @GetMapping("/artist")
    public String artist(){
        return "artist";
    }



}
