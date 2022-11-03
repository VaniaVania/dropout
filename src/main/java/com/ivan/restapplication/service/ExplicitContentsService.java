package com.ivan.restapplication.service;

import com.ivan.restapplication.models.ExplicitContent;
import com.ivan.restapplication.repository.ExplicitContentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExplicitContentsService {

    private final ExplicitContentsRepository explicitContentsRepository;

    @Autowired
    public ExplicitContentsService(ExplicitContentsRepository explicitContentsRepository) {
        this.explicitContentsRepository = explicitContentsRepository;
    }

    public void save(ExplicitContent explicitContent){
        explicitContentsRepository.save(explicitContent);
    }
}
