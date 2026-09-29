package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.AttendanceResponseDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto;
import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Attendance;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.repository.AttendanceRepository;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.repository.StudentRepository;
import com.smartcurriculum.portal.service.impl.AttendanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AttendanceServiceImpl — Day 18 end-to-end testing.
 */
@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Student student;
    private Activity activity;
    private Faculty faculty;
    private Attendance attendance;
    private AttendanceRequestDto requestDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setRollNumber("21CSE001");
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setDepartment("Computer Science and Engineering");
        student.setYearOfStudy(3);
        student.setSemester(5);
        student.setStatus("ACTIVE");

        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setEmployeeId("FAC001");
        faculty.setFirstName("Dr. Kumar");

        activity = new Activity();
        activity.setId(1L);
        activity.setActivityCode("ACT-CS501-LAB");
        activity.setTitle("Data Structures Lab");
        activity.setActivityType("LABORATORY");
        activity.setDepartment("Computer Science and Engineering");

        attendance = new Attendance();
        attendance.setId(1L);
        attendance.setStudent(student);
        attendance.setActivity(activity);
        attendance.setMarkedByFaculty(faculty);
        attendance.setAttendanceDate(LocalDate.of(2025, 9, 15));
        attendance.setStatus("PRESENT");
        attendance.setSessionSlot("SESSION_1");

        requestDto = new AttendanceRequestDto();
        requestDto.setStudentId(1L);
        requestDto.setActivityId(1L);
        requestDto.setFacultyId(1L);
        requestDto.setAttendanceDate(LocalDate.of(2025, 9, 15));
        requestDto.setStatus("PRESENT");
        requestDto.setSessionSlot("SESSION_1");
    }

    @Test
    @DisplayName("Should mark attendance successfully")
    void shouldMarkAttendanceSuccessfully() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(attendanceRepository.existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
                1L, 1L, LocalDate.of(2025, 9, 15), "SESSION_1")).thenReturn(false);
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> {
            Attendance a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        AttendanceResponseDto response = attendanceService.markAttendance(requestDto);

        assertNotNull(response);
        assertEquals("PRESENT", response.getStatus());
        assertEquals(1L, response.getStudentId());
        assertEquals(1L, response.getActivityId());
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student not found for attendance")
    void shouldThrowException_WhenStudentNotFoundForAttendance() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> attendanceService.markAttendance(requestDto));
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when activity not found for attendance")
    void shouldThrowException_WhenActivityNotFoundForAttendance() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> attendanceService.markAttendance(requestDto));
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when attendance already marked")
    void shouldThrowDuplicate_WhenAttendanceAlreadyMarked() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(attendanceRepository.existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
                1L, 1L, LocalDate.of(2025, 9, 15), "SESSION_1")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> attendanceService.markAttendance(requestDto));
        verify(attendanceRepository, never()).save(any(Attendance.class));
    }

    @Test
    @DisplayName("Should mark attendance with default session slot when not provided")
    void shouldUseDefaultSessionSlot() {
        requestDto.setSessionSlot(null);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(attendanceRepository.existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
                eq(1L), eq(1L), any(), eq("SESSION_1"))).thenReturn(false);
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> {
            Attendance a = inv.getArgument(0);
            a.setId(2L);
            return a;
        });

        AttendanceResponseDto response = attendanceService.markAttendance(requestDto);

        assertNotNull(response);
        assertEquals("SESSION_1", response.getSessionSlot());
    }

    @Test
    @DisplayName("Should get student attendance summary with ELIGIBLE status")
    void shouldReturnStudentSummary_Eligible() {
        Attendance a1 = createAttendance("PRESENT");
        Attendance a2 = createAttendance("PRESENT");
        Attendance a3 = createAttendance("PRESENT");
        Attendance a4 = createAttendance("ABSENT");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Arrays.asList(a1, a2, a3, a4));

        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(1L);

        assertNotNull(summary);
        assertEquals(4, summary.getTotalSessions());
        assertEquals(3, summary.getAttendedSessions());
        assertEquals(75.0, summary.getAttendancePercentage());
        assertEquals("ELIGIBLE", summary.getEligibilityStatus());
    }

    @Test
    @DisplayName("Should get student attendance summary with WARNING status")
    void shouldReturnStudentSummary_Warning() {
        Attendance a1 = createAttendance("PRESENT");
        Attendance a2 = createAttendance("PRESENT");
        Attendance a3 = createAttendance("ABSENT");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Arrays.asList(a1, a2, a3));

        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(1L);

        assertNotNull(summary);
        assertEquals("WARNING", summary.getEligibilityStatus());
    }

    @Test
    @DisplayName("Should get student attendance summary with CRITICAL status")
    void shouldReturnStudentSummary_Critical() {
        Attendance a1 = createAttendance("PRESENT");
        Attendance a2 = createAttendance("ABSENT");
        Attendance a3 = createAttendance("ABSENT");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Arrays.asList(a1, a2, a3));

        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(1L);

        assertNotNull(summary);
        assertEquals("CRITICAL", summary.getEligibilityStatus());
    }

    @Test
    @DisplayName("Should return NO_DATA status when no attendance records exist")
    void shouldReturnNoData_WhenNoRecords() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Collections.emptyList());

        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(1L);

        assertNotNull(summary);
        assertEquals("NO_DATA", summary.getEligibilityStatus());
        assertEquals(0, summary.getTotalSessions());
    }

    @Test
    @DisplayName("Should get student summary by roll number")
    void shouldReturnStudentSummaryByRollNumber() {
        when(studentRepository.findByRollNumber("21CSE001")).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentId(1L)).thenReturn(List.of(attendance));

        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummaryByRollNumber("21CSE001");

        assertNotNull(summary);
        assertEquals("21CSE001", summary.getStudentRollNumber());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when roll number not found for summary")
    void shouldThrowException_WhenRollNotFoundForSummary() {
        when(studentRepository.findByRollNumber("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> attendanceService.getStudentAttendanceSummaryByRollNumber("UNKNOWN"));
    }

    @Test
    @DisplayName("Should get activity attendance summary")
    void shouldReturnActivitySummary() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(attendanceRepository.findByActivityId(1L)).thenReturn(List.of(attendance));

        AttendanceSummaryDto summary = attendanceService.getActivityAttendanceSummary(1L);

        assertNotNull(summary);
        assertEquals("ACT-CS501-LAB", summary.getActivityCode());
        assertEquals(1, summary.getTotalSessions());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when activity not found for summary")
    void shouldThrowException_WhenActivityNotFoundForSummary() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> attendanceService.getActivityAttendanceSummary(99L));
    }

    @Test
    @DisplayName("Should return all attendance records")
    void shouldReturnAllAttendance() {
        when(attendanceRepository.findAll()).thenReturn(List.of(attendance));

        List<AttendanceResponseDto> result = attendanceService.getAllAttendance();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return attendance by student ID")
    void shouldReturnAttendanceByStudentId() {
        when(attendanceRepository.findByStudentId(1L)).thenReturn(List.of(attendance));

        List<AttendanceResponseDto> result = attendanceService.getAttendanceByStudentId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStudentId());
    }

    @Test
    @DisplayName("Should return attendance by roll number")
    void shouldReturnAttendanceByRollNumber() {
        when(attendanceRepository.findByStudentRollNumber("21CSE001")).thenReturn(List.of(attendance));

        List<AttendanceResponseDto> result = attendanceService.getAttendanceByRollNumber("21CSE001");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should count ON_DUTY status as attended in summary")
    void shouldCountOnDutyAsAttended() {
        Attendance a1 = createAttendance("ON_DUTY");
        Attendance a2 = createAttendance("PRESENT");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(attendanceRepository.findByStudentId(1L)).thenReturn(Arrays.asList(a1, a2));

        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(1L);

        assertEquals(2, summary.getAttendedSessions());
        assertEquals(100.0, summary.getAttendancePercentage());
    }

    // --- Helper ---

    private Attendance createAttendance(String status) {
        Attendance a = new Attendance();
        a.setStudent(student);
        a.setActivity(activity);
        a.setMarkedByFaculty(faculty);
        a.setAttendanceDate(LocalDate.of(2025, 9, 15));
        a.setStatus(status);
        a.setSessionSlot("SESSION_1");
        return a;
    }
}
