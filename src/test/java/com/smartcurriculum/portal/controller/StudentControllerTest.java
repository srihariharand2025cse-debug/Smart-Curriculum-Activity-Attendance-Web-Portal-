package com.smartcurriculum.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcurriculum.portal.dto.StudentRequestDto;
import com.smartcurriculum.portal.dto.StudentResponseDto;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private StudentRequestDto requestDto;
    private StudentResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new StudentRequestDto(
                "21CSE001",
                "Bob",
                "Williams",
                "bob.williams@college.edu",
                "9876543210",
                "Computer Science and Engineering",
                3,
                5,
                "A",
                "MALE",
                LocalDate.of(2003, 8, 20),
                "789 Avenue",
                "ACTIVE"
        );

        responseDto = new StudentResponseDto(
                1L,
                "21CSE001",
                "Bob",
                "Williams",
                "Bob Williams",
                "bob.williams@college.edu",
                "9876543210",
                "Computer Science and Engineering",
                3,
                5,
                "A",
                "MALE",
                LocalDate.of(2003, 8, 20),
                "789 Avenue",
                "ACTIVE",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("POST /api/students - Should create student and return 201 CREATED")
    void shouldCreateStudentSuccessfully() throws Exception {
        when(studentService.createStudent(any(StudentRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student registered successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.rollNumber").value("21CSE001"))
                .andExpect(jsonPath("$.data.fullName").value("Bob Williams"));
    }

    @Test
    @DisplayName("POST /api/students - Should return 409 CONFLICT on duplicate student")
    void shouldReturnConflict_WhenDuplicateResource() throws Exception {
        when(studentService.createStudent(any(StudentRequestDto.class)))
                .thenThrow(new DuplicateResourceException("Student", "rollNumber", "21CSE001"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Student already exists with rollNumber: '21CSE001'"));
    }

    @Test
    @DisplayName("GET /api/students - Should return all students with 200 OK")
    void shouldReturnAllStudents() throws Exception {
        when(studentService.getAllStudents()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/students").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].rollNumber").value("21CSE001"));
    }

    @Test
    @DisplayName("GET /api/students?department=CSE - Should return filtered students")
    void shouldFilterStudentsByDepartment() throws Exception {
        when(studentService.getStudentsByDepartment("CSE")).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/students").param("department", "CSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/students?department=CSE&yearOfStudy=3 - Should return filtered students")
    void shouldFilterStudentsByDepartmentAndYear() throws Exception {
        when(studentService.getStudentsByDepartmentAndYear("CSE", 3)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/students")
                        .param("department", "CSE")
                        .param("yearOfStudy", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Should return student by ID")
    void shouldReturnStudentById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.rollNumber").value("21CSE001"));
    }

    @Test
    @DisplayName("GET /api/students/{id} - Should return 404 NOT FOUND when ID does not exist")
    void shouldReturnNotFound_WhenIdMissing() throws Exception {
        when(studentService.getStudentById(99L))
                .thenThrow(new ResourceNotFoundException("Student", "id", 99L));

        mockMvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Student not found with id: '99'"));
    }

    @Test
    @DisplayName("GET /api/students/roll/{rollNumber} - Should return student by roll number")
    void shouldReturnStudentByRollNumber() throws Exception {
        when(studentService.getStudentByRollNumber("21CSE001")).thenReturn(responseDto);

        mockMvc.perform(get("/api/students/roll/21CSE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.rollNumber").value("21CSE001"));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Should update student and return 200 OK")
    void shouldUpdateStudentSuccessfully() throws Exception {
        when(studentService.updateStudent(eq(1L), any(StudentRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student updated successfully"));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Should delete student and return 200 OK")
    void shouldDeleteStudentSuccessfully() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student deleted successfully"));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Should return 404 when student not found")
    void shouldReturnNotFound_OnDeleteMissingStudent() throws Exception {
        doThrow(new ResourceNotFoundException("Student", "id", 99L)).when(studentService).deleteStudent(99L);

        mockMvc.perform(delete("/api/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
