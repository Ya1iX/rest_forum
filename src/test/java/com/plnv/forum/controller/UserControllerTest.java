package com.plnv.forum.controller;

import com.plnv.forum.config.JwtService;
import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.entity.User;
import com.plnv.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String END_POINT_PATH = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void getDataByIdShouldReturn200Ok() throws Exception {
        Mockito.when(service.readByUsername("user")).thenReturn(User.builder().id(UUID.randomUUID()).username("user").build());

        mockMvc.perform(get(END_POINT_PATH + "/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.username", is("user")));
    }
}