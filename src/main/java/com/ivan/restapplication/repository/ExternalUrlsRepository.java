package com.ivan.restapplication.repository;

import com.ivan.restapplication.models.ExternalUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUrlsRepository extends JpaRepository<ExternalUrl,Long> {
}
