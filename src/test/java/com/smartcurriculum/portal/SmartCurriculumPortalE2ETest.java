package com.smartcurriculum.portal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartcurriculum.portal.dto.ActivityRequestDto;
import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.FacultyRequestDto;
import com.smartcurriculum.portal.dto.StudentRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Complete End-to-End (E2E) Integration Test Suite for the Smart Curriculum Activity & Attendance Web Portal.
 * Exercises real end-to-end flows across controllers, services, repositories, database constraints,
 * calculations, and global error handling — Day 18.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SmartCurriculumPortalE2ETest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Shared state across the sequential E2E test workflow
    private static Long createdStudentId;
    private static Long createdFacultyId;
    private static Long createdActivityId;

    @Test
    @Order(1)
    @DisplayName("E2E Step 1: Register Student & Verify Retrieval")
    void testRegisterAndRetrieveStudent() throws Exception {
        StudentRequestDto studentDto = new StudentRequestDto(
                "21CSE999",
                "Alice",
                "Johnson",
                "alice.johnson@sece.ac.in",
                "9876543210",
                "Computer Science and Engineering",
                3,
                5,
                "A",
                "FEMALE",
                LocalDate.of(2003, 5, 15),
                "123 Tech Park, Coimbatore",
                "ACTIVE"
        );

        MvcResult createResult = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.rollNumber", is("21CSE999")))
                .andExpect(jsonPath("$.data.fullName", is("Alice Johnson")))
                .andReturn();

        JsonNode responseNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
        createdStudentId = responseNode.get("data").get("id").asLong();
        assertNotNull(createdStudentId);

        // Retrieve by primary ID
        mockMvc.perform(get("/api/students/" + createdStudentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rollNumber", is("21CSE999")));

        // Retrieve by Roll Number
        mockMvc.perform(get("/api/students/roll/21CSE999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(createdStudentId.intValue())));
    }

    @Test
    @Order(2)
    @DisplayName("E2E Step 2: Register Faculty & Verify Retrieval")
    void testRegisterAndRetrieveFaculty() throws Exception {
        FacultyRequestDto facultyDto = new FacultyRequestDto();
        facultyDto.setEmployeeId("FAC999");
        facultyDto.setFirstName("Dr. Sarah");
        facultyDto.setLastName("Connor");
        facultyDto.setEmail("sarah.connor@sece.ac.in");
        facultyDto.setPhoneNumber("9812345678");
        facultyDto.setDepartment("Computer Science and Engineering");
        facultyDto.setDesignation("Professor");
        facultyDto.setStatus("ACTIVE");

        MvcResult createResult = mockMvc.perform(post("/api/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.employeeId", is("FAC999")))
                .andReturn();

        JsonNode responseNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
        createdFacultyId = responseNode.get("data").get("id").asLong();
        assertNotNull(createdFacultyId);

        // Retrieve by employee ID
        mockMvc.perform(get("/api/faculty/employee/FAC999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email", is("sarah.connor@sece.ac.in")));
    }

    @Test
    @Order(3)
    @DisplayName("E2E Step 3: Create Curriculum Activity Linked to Faculty")
    void testCreateCurriculumActivity() throws Exception {
        ActivityRequestDto actDto = new ActivityRequestDto();
        actDto.setActivityCode("ACT-AI-900");
        actDto.setTitle("Deep Learning & LLM Symposium");
        actDto.setDescription("Advanced neural networks and transformers workshop");
        actDto.setActivityType("WORKSHOP");
        actDto.setDepartment("Computer Science and Engineering");
        actDto.setAcademicYear("2026-2027");
        actDto.setSemester(5);
        actDto.setCredits(3);
        actDto.setVenue("Auditorium Hall 2");
        actDto.setFacultyId(createdFacultyId);
        actDto.setStartDate(LocalDate.of(2026, 10, 10));
        actDto.setEndDate(LocalDate.of(2026, 10, 12));
        actDto.setMaxEnrollment(120);
        actDto.setStatus("UPCOMING");

        MvcResult actResult = mockMvc.perform(post("/api/activities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.activityCode", is("ACT-AI-900")))
                .andExpect(jsonPath("$.status", is("UPCOMING")))
                .andReturn();

        JsonNode responseNode = objectMapper.readTree(actResult.getResponse().getContentAsString());
        createdActivityId = responseNode.get("id").asLong();
        assertNotNull(createdActivityId);

        // Admin approves / updates activity status to ACTIVE
        mockMvc.perform(put("/api/admin/activities/" + createdActivityId + "/status?status=ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("ACTIVE")));
    }

    @Test
    @Order(4)
    @DisplayName("E2E Step 4: Mark Attendance & Verify Real-Time Summary Calculation")
    void testMarkAttendanceAndVerifyCalculations() throws Exception {
        // Record 1: Present
        AttendanceRequestDto att1 = new AttendanceRequestDto();
        att1.setStudentId(createdStudentId);
        att1.setActivityId(createdActivityId);
        att1.setFacultyId(createdFacultyId);
        att1.setAttendanceDate(LocalDate.of(2026, 10, 10));
        att1.setSessionSlot("SESSION_1");
        att1.setStatus("PRESENT");
        att1.setRemarks("Present on day 1");

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(att1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("PRESENT")));

        // Record 2: On Duty (OD)
        AttendanceRequestDto att2 = new AttendanceRequestDto();
        att2.setStudentId(createdStudentId);
        att2.setActivityId(createdActivityId);
        att2.setFacultyId(createdFacultyId);
        att2.setAttendanceDate(LocalDate.of(2026, 10, 11));
        att2.setSessionSlot("SESSION_1");
        att2.setStatus("ON_DUTY");
        att2.setRemarks("Representing college at hackathon");

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(att2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)));

        // Verify Student Summary calculations: 2 total, 2 attended (PRESENT + ON_DUTY) -> 100% ELIGIBLE
        mockMvc.perform(get("/api/attendance/student/" + createdStudentId + "/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.studentRollNumber", is("21CSE999")))
                .andExpect(jsonPath("$.data.totalSessions", is(2)))
                .andExpect(jsonPath("$.data.attendedSessions", is(2)))
                .andExpect(jsonPath("$.data.attendancePercentage", is(100.0)))
                .andExpect(jsonPath("$.data.eligibilityStatus", is("ELIGIBLE")))
                .andExpect(jsonPath("$.data.activityBreakdown", hasSize(greaterThanOrEqualTo(1))));

        // Verify Activity Summary calculations
        mockMvc.perform(get("/api/attendance/activity/" + createdActivityId + "/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.activityCode", is("ACT-AI-900")))
                .andExpect(jsonPath("$.data.totalSessions", is(2)));
    }

    @Test
    @Order(5)
    @DisplayName("E2E Step 5: Verify Centralized Admin Statistics Aggregation")
    void testAdminStatisticsAggregation() throws Exception {
        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalStudents", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.data.totalFaculty", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.data.totalActivities", greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.data.totalAttendanceRecords", greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$.data.databaseConnected", is(true)));
    }

    @Test
    @Order(6)
    @DisplayName("E2E Step 6: Verify Comprehensive Error Handling & Status Codes")
    void testErrorHandlingScenarios() throws Exception {
        // 1. Duplicate Student -> 409 CONFLICT
        StudentRequestDto dupStudent = new StudentRequestDto(
                "21CSE999", // already exists
                "Duplicate",
                "User",
                "unique.email@sece.ac.in",
                "9000000000",
                "CSE",
                1,
                1,
                "A",
                "MALE",
                LocalDate.of(2004, 1, 1),
                "Address",
                "ACTIVE"
        );
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupStudent)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already exists")));

        // 2. Duplicate Attendance for same student + activity + date + slot -> 409 CONFLICT
        AttendanceRequestDto dupAtt = new AttendanceRequestDto();
        dupAtt.setStudentId(createdStudentId);
        dupAtt.setActivityId(createdActivityId);
        dupAtt.setFacultyId(createdFacultyId);
        dupAtt.setAttendanceDate(LocalDate.of(2026, 10, 10));
        dupAtt.setSessionSlot("SESSION_1");
        dupAtt.setStatus("PRESENT");

        mockMvc.perform(post("/api/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupAtt)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("already marked")));

        // 3. Validation failure: missing required student fields -> 400 BAD_REQUEST
        StudentRequestDto invalidStudent = new StudentRequestDto();
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Validation failed")));

        // 4. Resource Not Found -> 404 NOT_FOUND
        mockMvc.perform(get("/api/students/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(get("/api/faculty/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        mockMvc.perform(get("/api/activities/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        // 5. Malformed JSON payload -> 400 BAD_REQUEST
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json-body}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("Malformed or unreadable JSON")));

        // 6. Unsupported HTTP Method -> 405 METHOD_NOT_ALLOWED
        mockMvc.perform(patch("/api/attendance"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("is not supported")));
    }
}
