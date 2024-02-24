package com.ivan.restapplication.service.impl;

import com.ivan.restapplication.entity.*;
import com.ivan.restapplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SavedUserService {

    private final UsersRepository usersRepository;

    @Autowired
    public SavedUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public User save(User user) {
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

        return usersRepository.save(user);
    }

    public boolean existsById(String id){
        return usersRepository.existsById(id);
    }
}
