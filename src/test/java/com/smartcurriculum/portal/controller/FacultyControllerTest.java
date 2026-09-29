package com.smartcurriculum.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartcurriculum.portal.dto.FacultyRequestDto;
import com.smartcurriculum.portal.dto.FacultyResponseDto;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.service.FacultyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for FacultyController REST endpoints — Day 18 end-to-end testing.
 */
@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    private ObjectMapper objectMapper;
    private FacultyRequestDto requestDto;
    private FacultyResponseDto responseDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        requestDto = new FacultyRequestDto();
        requestDto.setEmployeeId("FAC001");
        requestDto.setFirstName("John");
        requestDto.setLastName("Smith");
        requestDto.setEmail("john.smith@college.edu");
        requestDto.setDepartment("Computer Science and Engineering");
        requestDto.setDesignation("Associate Professor");
        requestDto.setStatus("ACTIVE");

        responseDto = new FacultyResponseDto();
        responseDto.setId(1L);
        responseDto.setEmployeeId("FAC001");
        responseDto.setFirstName("John");
        responseDto.setLastName("Smith");
        responseDto.setFullName("John Smith");
        responseDto.setEmail("john.smith@college.edu");
        responseDto.setDepartment("Computer Science and Engineering");
        responseDto.setDesignation("Associate Professor");
        responseDto.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("POST /api/faculty - should create faculty (201)")
    void shouldCreateFaculty() throws Exception {
        when(facultyService.createFaculty(any(FacultyRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.employeeId", is("FAC001")))
                .andExpect(jsonPath("$.data.fullName", is("John Smith")));

        verify(facultyService).createFaculty(any(FacultyRequestDto.class));
    }

    @Test
    @DisplayName("POST /api/faculty - should return 409 on duplicate")
    void shouldReturn409OnDuplicate() throws Exception {
        when(facultyService.createFaculty(any())).thenThrow(
                new DuplicateResourceException("Faculty", "employeeId", "FAC001"));

        mockMvc.perform(post("/api/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("GET /api/faculty - should return all faculty (200)")
    void shouldReturnAllFaculty() throws Exception {
        when(facultyService.getAllFaculties()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].employeeId", is("FAC001")));
    }

    @Test
    @DisplayName("GET /api/faculty?department=CSE - should filter by department")
    void shouldFilterByDepartment() throws Exception {
        when(facultyService.getFacultiesByDepartment("CSE")).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/faculty").param("department", "CSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/faculty/{id} - should return faculty by ID (200)")
    void shouldReturnFacultyById() throws Exception {
        when(facultyService.getFacultyById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.employeeId", is("FAC001")));
    }

    @Test
    @DisplayName("GET /api/faculty/{id} - should return 404 when not found")
    void shouldReturn404WhenNotFound() throws Exception {
        when(facultyService.getFacultyById(99L)).thenThrow(
                new ResourceNotFoundException("Faculty", "id", 99L));

        mockMvc.perform(get("/api/faculty/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("GET /api/faculty/employee/{empId} - should return faculty by employee ID")
    void shouldReturnFacultyByEmployeeId() throws Exception {
        when(facultyService.getFacultyByEmployeeId("FAC001")).thenReturn(responseDto);

        mockMvc.perform(get("/api/faculty/employee/FAC001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employeeId", is("FAC001")));
    }

    @Test
    @DisplayName("PUT /api/faculty/{id} - should update faculty (200)")
    void shouldUpdateFaculty() throws Exception {
        responseDto.setFirstName("Jonathan");
        when(facultyService.updateFaculty(eq(1L), any(FacultyRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", is("Jonathan")));
    }

    @Test
    @DisplayName("DELETE /api/faculty/{id} - should delete faculty (200)")
    void shouldDeleteFaculty() throws Exception {
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(delete("/api/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        verify(facultyService).deleteFaculty(1L);
    }

    @Test
    @DisplayName("POST /api/faculty - should return 400 on validation error")
    void shouldReturn400OnValidationError() throws Exception {
        requestDto.setEmployeeId("");   // violates @NotBlank
        requestDto.setFirstName("");    // violates @NotBlank
        requestDto.setEmail("invalid"); // violates @Email

        mockMvc.perform(post("/api/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
