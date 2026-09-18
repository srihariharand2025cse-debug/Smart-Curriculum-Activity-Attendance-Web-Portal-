package com.smartcurriculum.portal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnWelcomeMessageOnRootEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Day 9 Student CRUD operations (Repository, Service, Controller) are Complete")));
    }

    @Test
    void shouldReturnStructuredApiResponseOnStatusEndpoint() throws Exception {
        mockMvc.perform(get("/api/status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Portal API is running smoothly"))
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.currentMilestone").value("Day 9: Implement Student CRUD operations (Repository, Service, Controller)"))
                .andExpect(jsonPath("$.data.databaseConfigured").value(true))
                .andExpect(jsonPath("$.data.studentEntityMapped").value(true))
                .andExpect(jsonPath("$.data.studentCrudActive").value(true))
                .andExpect(jsonPath("$.data.facultyEntityMapped").value(true))
                .andExpect(jsonPath("$.data.activityEntityMapped").value(true))
                .andExpect(jsonPath("$.data.attendanceEntityMapped").value(true))
                .andExpect(jsonPath("$.data.nextMilestone").value("Day 10: Implement Faculty CRUD operations"));
    }

    @Test
    void shouldReturnDatabaseStatusOnDbStatusEndpoint() throws Exception {
        mockMvc.perform(get("/api/db-status").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.connected").value(true));
    }

    @Test
    void shouldReturnStudentSummaryOnSummaryEndpoint() throws Exception {
        mockMvc.perform(get("/api/students/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.entityMapped").value(true))
                .andExpect(jsonPath("$.data.tableName").value("students"));
    }

    @Test
    void shouldReturnFacultySummaryOnSummaryEndpoint() throws Exception {
        mockMvc.perform(get("/api/faculty/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.entityMapped").value(true))
                .andExpect(jsonPath("$.data.tableName").value("faculty"));
    }

    @Test
    void shouldReturnActivitySummaryOnSummaryEndpoint() throws Exception {
        mockMvc.perform(get("/api/activities/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.entityMapped").value(true))
                .andExpect(jsonPath("$.data.entityClass").value("com.smartcurriculum.portal.entity.Activity"))
                .andExpect(jsonPath("$.data.tableName").value("activities"));
    }

    @Test
    void shouldReturnAttendanceSummaryOnSummaryEndpoint() throws Exception {
        mockMvc.perform(get("/api/attendance/summary").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.entityMapped").value(true))
                .andExpect(jsonPath("$.data.entityClass").value("com.smartcurriculum.portal.entity.Attendance"))
                .andExpect(jsonPath("$.data.tableName").value("attendance"));
    }
}
