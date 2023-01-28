package com.ivan.restapplication.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperBean {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
