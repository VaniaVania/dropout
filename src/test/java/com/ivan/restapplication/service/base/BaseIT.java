package com.ivan.restapplication.service.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class BaseIT {
    public static JsonNode jsonNode;
    public static String SUCCESSFUL = "successful";
    public static String TRACK_IDS = "trackId1,trackId2,trackId3";

    @BeforeAll
    @SneakyThrows
    public static void init(){
        jsonNode = new ObjectMapper()
                .readTree("{ \"value\" : \"" + SUCCESSFUL + "\"}");
    }
}
