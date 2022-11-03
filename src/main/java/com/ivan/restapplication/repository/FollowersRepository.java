package com.ivan.restapplication.repository;

import com.ivan.restapplication.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowersRepository extends JpaRepository<Follower,Long> {
}
