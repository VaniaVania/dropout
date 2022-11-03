package com.ivan.restapplication.service;

import com.ivan.restapplication.models.ExternalUrl;
import com.ivan.restapplication.repository.ExternalUrlsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExternalUrlsService {

    private final ExternalUrlsRepository externalUrlsRepository;

    @Autowired
    public ExternalUrlsService(ExternalUrlsRepository externalUrlsRepository) {
        this.externalUrlsRepository = externalUrlsRepository;
    }

    public void save(ExternalUrl externalUrl){
        externalUrlsRepository.save(externalUrl);
    }
}
