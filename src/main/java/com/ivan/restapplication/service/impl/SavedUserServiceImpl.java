package com.ivan.restapplication.service.impl;

import com.ivan.restapplication.model.entity.ExplicitContent;
import com.ivan.restapplication.model.entity.ExternalUrl;
import com.ivan.restapplication.model.entity.Follower;
import com.ivan.restapplication.model.entity.Image;
import com.ivan.restapplication.model.entity.User;
import com.ivan.restapplication.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedUserServiceImpl {

    @Value(value = "${app.spotify.country-api-url}")
    private String countryApiUrl;

    private final UsersRepository usersRepository;

    @Transactional
    public User save(User user) {
        Follower follower = user.getFollowers();
        follower.setUser(user);

        ExplicitContent explicitContent = user.getExplicitContent();
        explicitContent.setUser(user);
        
        ExternalUrl externalUrl = user.getExternalUrls();
        externalUrl.setUser(user);

        List<Image> images = user.getImages();
        images.forEach(image -> image.setUser(user));

        user.setCreatedAt(LocalDateTime.now());

        user.setCountryImage(countryApiUrl.concat(user.getCountry().toLowerCase()));

        return usersRepository.save(user);
    }

    public boolean existsById(String id){
        return usersRepository.existsById(id);
    }
}
