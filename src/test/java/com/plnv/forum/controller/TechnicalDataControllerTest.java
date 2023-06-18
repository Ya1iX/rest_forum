package com.plnv.forum.controller;

import com.plnv.forum.config.JwtService;
import com.plnv.forum.entity.TechnicalData;
import com.plnv.forum.service.TechnicalDataService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TechnicalDataController.class)
@Import({JwtService.class})
class TechnicalDataControllerTest {
    private static final String END_POINT_PATH = "/api/v1/technical";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TechnicalDataService service;

    @Test
    @WithMockUser
    void getDataByIdShouldReturn200Ok() throws Exception {
        Mockito.when(service.readById("rules", null)).thenReturn(TechnicalData.builder().id("rules").text("Rules text").build());

        mockMvc.perform(get(END_POINT_PATH + "/rules").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entity.id", is("rules")));
    }
}