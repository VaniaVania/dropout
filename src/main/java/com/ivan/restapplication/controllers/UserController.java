package com.ivan.restapplication.controllers;

import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/test")
    public ResponseEntity<HttpStatus> createObject(@RequestBody User user){
        usersService.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
