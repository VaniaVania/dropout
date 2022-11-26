package com.ivan.restapplication.repository;

import com.ivan.restapplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<User,Long> {
      List<User> findById(String id);
}
