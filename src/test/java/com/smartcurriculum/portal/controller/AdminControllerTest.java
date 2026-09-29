package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.repository.AttendanceRepository;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit/Integration tests for AdminController endpoints — Day 18 end-to-end testing.
 */
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private AttendanceRepository attendanceRepository;

    @MockBean
    private DataSource dataSource;

    @Test
    @DisplayName("GET /api/admin/stats - should aggregate system metrics successfully")
    void shouldGetAdminStats() throws Exception {
        Student s1 = new Student();
        s1.setId(1L);
        s1.setDepartment("CSE");
        s1.setYearOfStudy(3);
        s1.setStatus("ACTIVE");

        Faculty f1 = new Faculty();
        f1.setId(1L);
        f1.setDepartment("CSE");
        f1.setDesignation("Professor");
        f1.setStatus("ACTIVE");

        Activity a1 = new Activity();
        a1.setId(1L);
        a1.setActivityCode("ACT-01");
        a1.setActivityType("WORKSHOP");
        a1.setDepartment("CSE");
        a1.setStatus("ACTIVE");

        Connection mockConnection = mock(Connection.class);
        DatabaseMetaData mockMetaData = mock(DatabaseMetaData.class);
        when(mockMetaData.getDatabaseProductName()).thenReturn("H2");
        when(mockMetaData.getDatabaseProductVersion()).thenReturn("2.2.224");
        when(mockConnection.getMetaData()).thenReturn(mockMetaData);
        when(dataSource.getConnection()).thenReturn(mockConnection);

        when(studentRepository.findAll()).thenReturn(List.of(s1));
        when(facultyRepository.findAll()).thenReturn(List.of(f1));
        when(activityRepository.findAll()).thenReturn(List.of(a1));
        when(attendanceRepository.count()).thenReturn(10L);
        when(attendanceRepository.countByStatus("PRESENT")).thenReturn(8L);
        when(attendanceRepository.countByStatus("ABSENT")).thenReturn(2L);
        when(attendanceRepository.countByStatus("ON_DUTY")).thenReturn(0L);

        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.totalStudents", is(1)))
                .andExpect(jsonPath("$.data.activeStudents", is(1)))
                .andExpect(jsonPath("$.data.totalFaculty", is(1)))
                .andExpect(jsonPath("$.data.totalActivities", is(1)))
                .andExpect(jsonPath("$.data.totalAttendanceRecords", is(10)))
                .andExpect(jsonPath("$.data.presentCount", is(8)))
                .andExpect(jsonPath("$.data.overallAttendancePercentage", is(80.0)))
                .andExpect(jsonPath("$.data.databaseConnected", is(true)));
    }

    @Test
    @DisplayName("PUT /api/admin/activities/{id}/status - should update status")
    void shouldUpdateActivityStatus() throws Exception {
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setActivityCode("ACT-01");
        activity.setStatus("UPCOMING");

        Activity updatedActivity = new Activity();
        updatedActivity.setId(1L);
        updatedActivity.setActivityCode("ACT-01");
        updatedActivity.setStatus("ACTIVE");

        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenReturn(updatedActivity);

        mockMvc.perform(put("/api/admin/activities/1/status?status=ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.status", is("ACTIVE")))
                .andExpect(jsonPath("$.message", containsString("ACTIVE")));

        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    @DisplayName("PUT /api/admin/activities/{id}/status - should return 404 when activity not found")
    void shouldReturn404WhenActivityNotFound() throws Exception {
        when(activityRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/admin/activities/999/status?status=ACTIVE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    @DisplayName("PUT /api/admin/activities/{id}/status - should return 400 when status is missing or blank")
    void shouldReturn400WhenStatusBlank() throws Exception {
        Activity activity = new Activity();
        activity.setId(1L);
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        mockMvc.perform(put("/api/admin/activities/1/status?status="))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)));
    }
}
