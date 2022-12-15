package com.ivan.restapplication.service;


import com.ivan.restapplication.models.*;
import com.ivan.restapplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SavedUserService {

    private final UsersRepository usersRepository;


    @Autowired
    public SavedUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void save(@RequestBody User user){
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

        if(usersRepository.findById(user.getId()).isEmpty()){
            usersRepository.save(user);
        }

    }
}
