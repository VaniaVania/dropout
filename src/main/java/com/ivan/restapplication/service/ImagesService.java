package com.ivan.restapplication.service;

import com.ivan.restapplication.models.Image;
import com.ivan.restapplication.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImagesService {

    private final ImagesRepository imagesRepository;

    @Autowired
    public ImagesService(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    public void saveAll(List<Image> images){
        imagesRepository.saveAll(images);
    }
}
