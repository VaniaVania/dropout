package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.properties.SpotifyProperties;
import com.ivan.restapplication.util.UnauthorizedUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private SpotifyProperties spotifyProperties;

    @Mock
    private JsonNode jsonNode;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthorize() {
        when(spotifyProperties.getClientId()).thenReturn("testClientId");
        when(spotifyProperties.getRedirectUri()).thenReturn("testRedirectUri");
        when(spotifyProperties.getResponseType()).thenReturn("testResponseType");
        when(spotifyProperties.isShowDialog()).thenReturn(true);
        when(spotifyProperties.getScope()).thenReturn("testScope");

        String authorizeUrl = authService.authorize();

        String expectedUrl = "https://accounts.spotify.com/authorize" +
                "?client_id=testClientId" +
                "&redirect_uri=testRedirectUri" +
                "&response_type=testResponseType" +
                "&show_dialog=true" +
                "&scope=testScope";
        assertEquals(expectedUrl, authorizeUrl);
    }

    @Test
    void testAccessToken() throws JsonProcessingException, UnauthorizedUserException {
        when(spotifyProperties.getGrantType()).thenReturn("testGrantType");
        when(spotifyProperties.getRedirectUri()).thenReturn("testRedirectUri");
        when(spotifyProperties.getClientId()).thenReturn("testClientId");
        when(spotifyProperties.getClientSecret()).thenReturn("testClientSecret");

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn("testResponse");

        when(objectMapper.readTree(anyString())).thenReturn(jsonNode);
        when(jsonNode.get("access_token")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("access_token").asText()).thenReturn("testAccessToken");
        when(jsonNode.get("refresh_token")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("refresh_token").asText()).thenReturn("testRefreshToken");

        ReflectionTestUtils.setField(authService, "properties", spotifyProperties);

        authService.accessToken("testCode");

        verify(spotifyProperties).setCode("testCode");
        verify(spotifyProperties).setToken("testAccessToken");
        verify(spotifyProperties).setRefreshToken("testRefreshToken");

    }

    @Test
    void testAccessToken_UnauthorizedUserException() {
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class))).thenThrow(UnauthorizedUserException.class);

        ReflectionTestUtils.setField(authService, "properties", spotifyProperties);

        assertThrows(UnauthorizedUserException.class, () -> authService.accessToken("testCode"));
    }

    @Test
    void testRefreshToken() throws JsonProcessingException {
        when(spotifyProperties.getClientId()).thenReturn("testClientId");
        when(spotifyProperties.getClientSecret()).thenReturn("testClientSecret");
        when(spotifyProperties.getRefreshToken()).thenReturn("testRefreshToken");

        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class))).thenReturn("testResponse");

        when(objectMapper.readTree(anyString())).thenReturn(jsonNode);
        when(jsonNode.get("access_token")).thenReturn(mock(JsonNode.class));
        when(jsonNode.get("access_token").asText()).thenReturn("testAccessToken");

        authService.refreshToken();

        verify(spotifyProperties).setToken("testAccessToken");
    }

    @Test
    void testUseToken() throws UnauthorizedUserException {
        when(spotifyProperties.getToken()).thenReturn("testToken");

        HttpHeaders expectedHeaders = new HttpHeaders();
        expectedHeaders.setContentType(MediaType.APPLICATION_JSON);
        expectedHeaders.set("Authorization", "Bearer testToken");

        HttpEntity<Object> expectedHttpEntity = new HttpEntity<>(expectedHeaders);

        HttpEntity<Object> httpEntity = authService.useToken();

        assertEquals(expectedHttpEntity, httpEntity);
    }

    @Test
    void testLogout() {
        authService.logout();
        verify(spotifyProperties).setToken(null);
        verify(restTemplate).getForObject("https://accounts.spotify.com/logout", String.class);
    }
}
