package com.ivan.restapplication.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class MainController {

    @GetMapping()
    public String mainPage() {
        return "main";
    }

    @GetMapping("/albums")
    public String artist() {
        return "album_search";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
