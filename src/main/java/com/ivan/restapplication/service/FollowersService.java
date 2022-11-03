package com.ivan.restapplication.service;

import com.ivan.restapplication.models.Follower;
import com.ivan.restapplication.repository.FollowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowersService {

    private final FollowersRepository followersRepository;

    @Autowired
    public FollowersService(FollowersRepository followersRepository) {
        this.followersRepository = followersRepository;
    }

    public void save(Follower follower){
        followersRepository.save(follower);
    }
}
