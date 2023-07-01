package com.ivan.restapplication.service;

import com.ivan.restapplication.dto.UserDTO;
import com.ivan.restapplication.models.ExplicitContent;
import com.ivan.restapplication.models.ExternalUrl;
import com.ivan.restapplication.models.Follower;
import com.ivan.restapplication.models.User;
import com.ivan.restapplication.repository.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class SavedUserServiceTest {

    @Autowired
    private SavedUserService savedUserService;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private UsersRepository usersRepository;

    private MockRestServiceServer server;

    private User user;

    @BeforeEach
    public void setUp(){
        server = MockRestServiceServer.createServer(restTemplate);

        user = new User("country",
                "name",
                "mail@gmail.com",
                "href", "id",
                "free",
                "type",
                "uri",
                "image",
                new ExplicitContent(),
                new ExternalUrl(),
                new Follower(),
                new ArrayList<>(),
                LocalDateTime.now());
    }

    @Test
    public void shouldSave(){
        boolean isCreated = savedUserService.save(user);
        Assertions.assertTrue(isCreated);
        Mockito.verify(usersRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void findUserFailTest() {
        Mockito.when(usersRepository.findById("id"))
                .thenReturn(Optional.of(user));

        boolean isCreated = savedUserService.save(user);
        Assertions.assertFalse(isCreated);
    }

    @Test
    public void getSpotifyDtoTest(){
        String expectedResponseBody = "{ \"id\": \"user123\", \"displayName\": \"BilL Wood\" }";
        server.expect(requestTo("https://api.spotify.com/v1/me"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(expectedResponseBody, MediaType.APPLICATION_JSON));

        UserDTO user = savedUserService.getSpotifyUserDTO();
        Assertions.assertNotNull(user);
        server.verify();
    }
}
