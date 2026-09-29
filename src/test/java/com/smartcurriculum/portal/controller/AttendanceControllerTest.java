package com.smartcurriculum.portal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.AttendanceResponseDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.service.AttendanceService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit/Integration tests for AttendanceController endpoints — Day 18 end-to-end testing.
 */
@WebMvcTest(AttendanceController.class)
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceService attendanceService;

    private ObjectMapper objectMapper;
    private AttendanceRequestDto requestDto;
    private AttendanceResponseDto responseDto;
    private AttendanceSummaryDto summaryDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        requestDto = new AttendanceRequestDto();
        requestDto.setStudentId(1L);
        requestDto.setActivityId(1L);
        requestDto.setFacultyId(1L);
        requestDto.setAttendanceDate(LocalDate.of(2026, 10, 1));
        requestDto.setStatus("PRESENT");
        requestDto.setSessionSlot("SESSION_1");
        requestDto.setRemarks("On time");

        responseDto = new AttendanceResponseDto();
        responseDto.setId(10L);
        responseDto.setStudentId(1L);
        responseDto.setStudentRollNumber("21CSE001");
        responseDto.setActivityId(1L);
        responseDto.setActivityCode("ACT-CS-101");
        responseDto.setAttendanceDate(LocalDate.of(2026, 10, 1));
        responseDto.setStatus("PRESENT");
        responseDto.setSessionSlot("SESSION_1");

        summaryDto = new AttendanceSummaryDto();
        summaryDto.setStudentId(1L);
        summaryDto.setStudentRollNumber("21CSE001");
        summaryDto.setStudentName("Bob Williams");
        summaryDto.setTotalSessions(10);
        summaryDto.setAttendedSessions(8);
        summaryDto.setAbsentSessions(2);
        summaryDto.setOnDutySessions(0);
        summaryDto.setAttendancePercentage(80.0);
        summaryDto.setEligibilityStatus("ELIGIBLE");
        summaryDto.setEligibilityBadge("Exam Eligible (Good Standing)");
    }

    @Test
    @DisplayName("POST /api/attendance - should mark attendance successfully (201)")
    void shouldMarkAttendance() throws Exception {
        when(attendanceService.markAttendance(any(AttendanceRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(10)))
                .andExpect(jsonPath("$.data.studentRollNumber", is("21CSE001")))
                .andExpect(jsonPath("$.data.status", is("PRESENT")));

        verify(attendanceService).markAttendance(any(AttendanceRequestDto.class));
    }

    @Test
    @DisplayName("POST /api/attendance - should return 400 when required fields missing")
    void shouldReturn400OnValidationFailure() throws Exception {
        AttendanceRequestDto invalid = new AttendanceRequestDto();
        // studentId, activityId, attendanceDate are null

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("POST /api/attendance - should return 409 on duplicate attendance entry")
    void shouldReturn409OnDuplicateAttendance() throws Exception {
        when(attendanceService.markAttendance(any(AttendanceRequestDto.class)))
                .thenThrow(new DuplicateResourceException("Attendance already marked for student 21CSE001"));

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already marked")));
    }

    @Test
    @DisplayName("GET /api/attendance - should return all records")
    void shouldGetAllAttendance() throws Exception {
        when(attendanceService.getAllAttendance()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/attendance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].studentRollNumber", is("21CSE001")));
    }

    @Test
    @DisplayName("GET /api/attendance?studentId=1 - should filter by studentId")
    void shouldFilterByStudentId() throws Exception {
        when(attendanceService.getAttendanceByStudentId(1L)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/attendance?studentId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].studentId", is(1)));
    }

    @Test
    @DisplayName("GET /api/attendance?rollNumber=21CSE001 - should filter by rollNumber")
    void shouldFilterByRollNumber() throws Exception {
        when(attendanceService.getAttendanceByRollNumber("21CSE001")).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/attendance?rollNumber=21CSE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].studentRollNumber", is("21CSE001")));
    }

    @Test
    @DisplayName("GET /api/attendance/student/{studentId}/summary - should return student summary")
    void shouldGetStudentSummary() throws Exception {
        when(attendanceService.getStudentAttendanceSummary(1L)).thenReturn(summaryDto);

        mockMvc.perform(get("/api/attendance/student/1/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.attendancePercentage", is(80.0)))
                .andExpect(jsonPath("$.data.eligibilityStatus", is("ELIGIBLE")));
    }

    @Test
    @DisplayName("GET /api/attendance/student/roll/{rollNumber}/summary - should return summary by roll")
    void shouldGetStudentSummaryByRoll() throws Exception {
        when(attendanceService.getStudentAttendanceSummaryByRollNumber("21CSE001")).thenReturn(summaryDto);

        mockMvc.perform(get("/api/attendance/student/roll/21CSE001/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.studentRollNumber", is("21CSE001")));
    }

    @Test
    @DisplayName("GET /api/attendance/activity/{activityId}/summary - should return activity summary")
    void shouldGetActivitySummary() throws Exception {
        AttendanceSummaryDto actSummary = new AttendanceSummaryDto();
        actSummary.setActivityId(1L);
        actSummary.setActivityCode("ACT-CS-101");
        actSummary.setAttendancePercentage(90.0);
        actSummary.setEligibilityStatus("ELIGIBLE");

        when(attendanceService.getActivityAttendanceSummary(1L)).thenReturn(actSummary);

        mockMvc.perform(get("/api/attendance/activity/1/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.activityCode", is("ACT-CS-101")))
                .andExpect(jsonPath("$.data.attendancePercentage", is(90.0)));
    }

    @Test
    @DisplayName("GET /api/attendance/student/{studentId}/summary - should return 404 when student not found")
    void shouldReturn404WhenStudentNotFound() throws Exception {
        when(attendanceService.getStudentAttendanceSummary(999L))
                .thenThrow(new ResourceNotFoundException("Student", "id", 999L));

        mockMvc.perform(get("/api/attendance/student/999/summary"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));
    }
}
