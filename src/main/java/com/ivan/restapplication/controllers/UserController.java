package com.ivan.restapplication.controllers;

import com.ivan.restapplication.models.User;
import com.ivan.restapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UsersService usersService;
    private final ImagesService imagesService;
    private final FollowersService followersService;
    private final ExternalUrlsService externalUrlsService;
    private final ExplicitContentsService explicitContentsService;

    @Autowired
    public UserController(UsersService usersService, ImagesService imagesService, FollowersService followersService, ExternalUrlsService externalUrlsService, ExplicitContentsService explicitContentsService) {
        this.usersService = usersService;
        this.imagesService = imagesService;
        this.followersService = followersService;
        this.externalUrlsService = externalUrlsService;
        this.explicitContentsService = explicitContentsService;
    }


    @PostMapping("/test")
    public ResponseEntity<HttpStatus> createObject(@RequestBody User user){
        explicitContentsService.save(user.getExplicit_content());
        externalUrlsService.save(user.getExternal_urls());
        followersService.save(user.getFollowers());
        imagesService.saveAll(user.getImages());
        usersService.save(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
