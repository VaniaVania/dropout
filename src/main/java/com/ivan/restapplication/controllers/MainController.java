package com.ivan.restapplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    static String CODE = "";

    @GetMapping("/main")
    public String mainPage(@RequestParam(value = "code",required = false) String code, Model model){
        CODE = code;
        model.addAttribute("token", AuthController.TOKEN);
        return "indexprototype";

    }
}
