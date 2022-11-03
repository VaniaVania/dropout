package com.ivan.restapplication.controllers;

import com.ivan.restapplication.models.User;
import com.ivan.restapplication.repository.ExplicitContentsRepository;
import com.ivan.restapplication.repository.ExternalUrlsRepository;
import com.ivan.restapplication.repository.FollowersRepository;
import com.ivan.restapplication.repository.ImagesRepository;
import com.ivan.restapplication.service.*;
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


    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(){
        String url = "https://api.spotify.com/v1/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET,entity,String.class);
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
