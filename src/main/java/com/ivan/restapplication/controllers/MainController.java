package com.ivan.restapplication.controllers;

import com.ivan.restapplication.models.ExplicitContent;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.repository.ExplicitContentRepository;
import com.ivan.restapplication.repository.UsersRepository;
import com.ivan.restapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    static String CODE = "";

    @GetMapping("/main")
    public String mainPage(@RequestParam(value = "code",required = false) String code, Model model){
        CODE = code;
        model.addAttribute("token", AuthController.TOKEN);
        return "main";
    }

}
