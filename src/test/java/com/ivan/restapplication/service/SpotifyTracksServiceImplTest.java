package com.ivan.restapplication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.base.BaseIT;
import com.ivan.restapplication.service.impl.AnalysisServiceImpl;
import com.ivan.restapplication.service.impl.SpotifyTracksServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpotifyTracksServiceImplTest extends BaseIT{

    @InjectMocks
    private SpotifyTracksServiceImpl spotifyUserService;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private AnalysisServiceImpl analysisService;

    @Mock
    private RestTemplate restTemplate;


    @Test
    @SneakyThrows
    void getRecommendations_Successful(){
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonNode.get("value").asText());
        when(mapper.readTree(anyString())).thenReturn(jsonNode);

        JsonNode result = spotifyUserService.getRecommendations("", "", "");

        assertNotNull(result);
        assertEquals(SUCCESSFUL, result.get("value").asText());
    }

    @Test
    @SneakyThrows
    void getTracksAudioFeatures_Successful(){
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonNode.get("value").asText());
        when(mapper.readTree(anyString())).thenReturn(jsonNode);
        when(analysisService.findTopTrackIds(anyString())).thenReturn(TRACK_IDS);

        JsonNode result = spotifyUserService.getTracksAudioFeatures("", "");

        assertNotNull(result);
        assertEquals(SUCCESSFUL, result.get("value").asText());
    }
}
