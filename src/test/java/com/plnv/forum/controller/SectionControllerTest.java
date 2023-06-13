package com.plnv.forum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plnv.forum.config.JwtService;
import com.plnv.forum.entity.Section;
import com.plnv.forum.service.SectionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SectionController.class)
class SectionControllerTest {
    private static final String END_POINT_PATH = "/api/v1/sections";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SectionService service;

    @MockBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void getByIdShouldReturn200Ok() throws Exception {
        Mockito.when(service.readById(1L)).thenReturn(Section.builder().id(1L).name("Section name 1").build());

        mockMvc.perform(get(END_POINT_PATH + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.section.id", is(1)));
    }
}