package com.ivan.restapplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private JsonNode jsonNode;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test()
    @WithMockUser(username = "user1", authorities = {"ROLE_USER"})
    void showUserProfileSuccess() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        String responseBody = "{ \"name\": \"John Doe\", \"email\": \"john@example.com\" }";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        //when(authService.useToken()).thenReturn(entity);
        when(restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                entity,
                String.class
        )).thenReturn(responseEntity);
        when(mapper.readTree(responseBody)).thenReturn(jsonNode);

        JsonNode result = userService.showUserProfile();

        //verify(authService).useToken();
        verify(restTemplate).exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                entity,
                String.class
        );
        verify(mapper).readTree(responseBody);
        assertEquals(jsonNode, result);
    }
}