package com.ivan.restapplication.service;

import com.ivan.restapplication.model.entity.User;
import com.ivan.restapplication.service.impl.SavedUserServiceImpl;
import com.ivan.restapplication.service.impl.SpotifyUserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@AutoConfigureWebTestClient
public class SavedSpotifyUserServiceImplTest {

    @Autowired
    private SavedUserServiceImpl savedUserServiceImpl;

    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @Autowired
    private SpotifyUserServiceImpl spotifyUserServiceImpl;


    private User user;

    @BeforeEach
    public void setUp() {
        this.restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldSave() {
        savedUserServiceImpl.save(user);
        Assertions.assertTrue(savedUserServiceImpl.existsById(user.getId()));
    }

    @Test
    public void findUserFailTest() {
        boolean isCreated = savedUserServiceImpl.existsById(user.getId() + "fakeId");
        Assertions.assertFalse(isCreated);
    }

    @Test
    @WithMockUser
    public void getSpotifyDtoTest() {
        String expectedResponseBody = "{ \"id\": \"31mwjjv3r2up4fsautc4cpzqhegm\", \"displayName\": \"Vania\" }";

        server.expect(requestTo("https://api.spotify.com/v1/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));
    }

    @Autowired
    OAuth2AuthorizedClientService clientService;

    @Test
    @WithMockUser
    public void testOAuth2Token() {
    }



}
