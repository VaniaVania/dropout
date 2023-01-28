package com.ivan.restapplication.service;


import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.*;
import com.ivan.restapplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SavedUserService {

    private final UsersRepository usersRepository;
    private final AuthService authService;
    private final RestTemplate restTemplate;

    @Autowired
    public SavedUserService(UsersRepository usersRepository, AuthService authService, RestTemplate restTemplate) {
        this.usersRepository = usersRepository;
        this.authService = authService;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void save(User user) {

        Follower follower = user.getFollowers();
        follower.setUser(user);

        ExplicitContent explicitContent = user.getExplicit_content();
        explicitContent.setUser(user);
        
        ExternalUrl externalUrl = user.getExternal_urls();
        externalUrl.setUser(user);

        List<Image> images = user.getImages();
        images.forEach(image -> image.setUser(user));

        user.setCreatedAt(LocalDateTime.now());

        user.setCountryImage("https://countryflagsapi.com/png/" + user.getCountry().toLowerCase());

        if (usersRepository.findById(user.getId()).isEmpty()) {
            usersRepository.save(user);
        }
    }

    public UserDTO getSpotifyUserDTO() {
        return restTemplate.exchange("https://api.spotify.com/v1/me", HttpMethod.GET, authService.useToken(), UserDTO.class).getBody();
    }

}
