package com.smartcurriculum.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartcurriculum.portal.dto.ActivityRequestDto;
import com.smartcurriculum.portal.dto.ActivityResponseDto;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit/Integration tests for ActivityController endpoints — Day 18 end-to-end testing.
 */
@WebMvcTest(ActivityController.class)
class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    private ObjectMapper objectMapper;
    private ActivityRequestDto requestDto;
    private ActivityResponseDto responseDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        requestDto = new ActivityRequestDto();
        requestDto.setActivityCode("ACT-CS-101");
        requestDto.setTitle("Cloud Computing Workshop");
        requestDto.setDescription("Hands-on workshop on AWS & Docker");
        requestDto.setActivityType("WORKSHOP");
        requestDto.setDepartment("Computer Science and Engineering");
        requestDto.setSemester(5);
        requestDto.setCredits(2);
        requestDto.setVenue("Lab 3");
        requestDto.setStartDate(LocalDate.of(2026, 10, 1));
        requestDto.setEndDate(LocalDate.of(2026, 10, 2));
        requestDto.setMaxEnrollment(60);
        requestDto.setStatus("ACTIVE");

        responseDto = new ActivityResponseDto();
        responseDto.setId(1L);
        responseDto.setActivityCode("ACT-CS-101");
        responseDto.setTitle("Cloud Computing Workshop");
        responseDto.setDescription("Hands-on workshop on AWS & Docker");
        responseDto.setActivityType("WORKSHOP");
        responseDto.setDepartment("Computer Science and Engineering");
        responseDto.setSemester(5);
        responseDto.setCredits(2);
        responseDto.setVenue("Lab 3");
        responseDto.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("POST /api/activities - should create activity successfully (201)")
    void shouldCreateActivity() throws Exception {
        when(activityService.createActivity(any(ActivityRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.activityCode", is("ACT-CS-101")))
                .andExpect(jsonPath("$.title", is("Cloud Computing Workshop")));

        verify(activityService).createActivity(any(ActivityRequestDto.class));
    }

    @Test
    @DisplayName("POST /api/activities - should return 400 when required fields missing")
    void shouldReturn400OnValidationFailure() throws Exception {
        ActivityRequestDto invalidDto = new ActivityRequestDto();
        // Missing activityCode, title, activityType, department

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Validation failed")));
    }

    @Test
    @DisplayName("POST /api/activities - should return 409 on duplicate activity code")
    void shouldReturn409OnDuplicateCode() throws Exception {
        when(activityService.createActivity(any(ActivityRequestDto.class)))
                .thenThrow(new DuplicateResourceException("Activity", "activityCode", "ACT-CS-101"));

        mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already exists")));
    }

    @Test
    @DisplayName("GET /api/activities - should return paginated activities")
    void shouldGetAllActivitiesPaginated() throws Exception {
        PageImpl<ActivityResponseDto> page = new PageImpl<>(List.of(responseDto), PageRequest.of(0, 10), 1);
        when(activityService.getAllActivities(anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/activities?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].activityCode", is("ACT-CS-101")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    @DisplayName("GET /api/activities/{id} - should return activity by ID")
    void shouldGetActivityById() throws Exception {
        when(activityService.getActivityById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/activities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.activityCode", is("ACT-CS-101")));
    }

    @Test
    @DisplayName("GET /api/activities/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() throws Exception {
        when(activityService.getActivityById(99L))
                .thenThrow(new ResourceNotFoundException("Activity", "id", 99L));

        mockMvc.perform(get("/api/activities/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("GET /api/activities/code/{code} - should return activity by code")
    void shouldGetActivityByCode() throws Exception {
        when(activityService.getActivityByCode("ACT-CS-101")).thenReturn(responseDto);

        mockMvc.perform(get("/api/activities/code/ACT-CS-101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityCode", is("ACT-CS-101")));
    }

    @Test
    @DisplayName("GET /api/activities/list - should return list of all activities")
    void shouldGetAllActivitiesList() throws Exception {
        when(activityService.getAllActivitiesList()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/activities/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].activityCode", is("ACT-CS-101")));
    }

    @Test
    @DisplayName("PUT /api/activities/{id} - should update activity")
    void shouldUpdateActivity() throws Exception {
        when(activityService.updateActivity(eq(1L), any(ActivityRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/activities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.activityCode", is("ACT-CS-101")));
    }

    @Test
    @DisplayName("DELETE /api/activities/{id} - should delete activity (204)")
    void shouldDeleteActivity() throws Exception {
        doNothing().when(activityService).deleteActivity(1L);

        mockMvc.perform(delete("/api/activities/1"))
                .andExpect(status().isNoContent());

        verify(activityService).deleteActivity(1L);
    }
}
