package com.smartcurriculum.portal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnWelcomeMessageOnRootEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Day 5 Student Entity and Database Mapping is Complete")));
    }

    @Test
    void shouldReturnStructuredApiResponseOnStatusEndpoint() throws Exception {
        mockMvc.perform(get("/api/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Portal API is running smoothly"))
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.currentMilestone").value("Day 5: Create Student Entity and Database Mapping"))
                .andExpect(jsonPath("$.data.databaseConfigured").value(true))
                .andExpect(jsonPath("$.data.studentEntityMapped").value(true))
                .andExpect(jsonPath("$.data.nextMilestone").value("Day 6: Create Faculty Entity and Database Mapping"));
    }

    @Test
    void shouldReturnDatabaseStatusOnDbStatusEndpoint() throws Exception {
        mockMvc.perform(get("/api/db-status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.connected").value(true));
    }
}
