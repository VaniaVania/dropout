package com.ivan.restapplication.repository;

import com.ivan.restapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByDisplayName(String name);

    boolean existsByDisplayName(String name);
}
