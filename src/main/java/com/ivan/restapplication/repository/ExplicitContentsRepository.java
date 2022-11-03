package com.ivan.restapplication.repository;

import com.ivan.restapplication.models.ExplicitContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExplicitContentsRepository extends JpaRepository<ExplicitContent,Long> {
}
