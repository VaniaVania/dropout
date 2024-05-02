package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.model.entity.User;
import com.ivan.restapplication.service.impl.SavedUserService;
import com.ivan.restapplication.service.impl.SpotifyUserServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@WithMockUser
public class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SavedUserService savedUserService;

    @MockBean
    private SpotifyUserServiceImpl spotifyUserServiceImpl;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModelMapper mapper;

    @Test
    @Disabled
    public void testMyProfile() throws Exception {
        Mockito.when(savedUserService.save(Mockito.any(User.class))).thenReturn(new User());
        Mockito.when(spotifyUserServiceImpl.showUserProfile()).thenReturn(objectMapper.readTree("{ \"id\" : \"2\"}"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isOk());
    }

}
