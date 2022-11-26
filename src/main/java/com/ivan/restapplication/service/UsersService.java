package com.ivan.restapplication.service;


import com.ivan.restapplication.models.*;
import com.ivan.restapplication.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import java.util.List;

@Service
@Transactional
public class UsersService{

    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public void save(User user){
        Follower follower = user.getFollowers();
        follower.setUser(user);

        ExplicitContent explicitContent = user.getExplicit_content();
        explicitContent.setUser(user);


        ExternalUrl externalUrl = user.getExternal_urls();
        externalUrl.setUser(user);

        List<Image> images = user.getImages();
        images.forEach(image -> image.setUser(user));

        usersRepository.save(user);
    }

    @Transactional
    public void ifEmptySave(User user){
        if(usersRepository.findById(user.getId()).isEmpty()){
             save(user);
        }
    }
}
