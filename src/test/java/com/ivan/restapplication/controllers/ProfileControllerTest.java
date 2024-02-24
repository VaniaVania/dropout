package com.ivan.restapplication.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivan.restapplication.entity.User;
import com.ivan.restapplication.service.impl.SavedUserService;
import com.ivan.restapplication.service.impl.UserService;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    private UserService userService;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModelMapper mapper;

    @Test
    @Ignore
    public void testMyProfile() throws Exception {
        Mockito.when(savedUserService.save(Mockito.any(User.class))).thenReturn(new User());
        Mockito.when(userService.showUserProfile()).thenReturn(objectMapper.readTree("{ \"id\" : \"2\"}"));

        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isOk());
    }

}
