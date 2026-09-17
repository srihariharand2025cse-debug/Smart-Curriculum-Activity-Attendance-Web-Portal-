package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Attendance;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("AttendanceRepository Tests - Day 8 Database Mapping Verification")
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Student sampleStudent;
    private Student sampleStudent2;
    private Faculty sampleFaculty;
    private Activity sampleActivity;
    private Attendance sampleAttendance;

    @BeforeEach
    void setUp() {
        attendanceRepository.deleteAll();
        activityRepository.deleteAll();
        facultyRepository.deleteAll();
        studentRepository.deleteAll();

        // Prepare sample students
        sampleStudent = new Student("21CSE001", "Aarav", "Sharma", "aarav.sharma@college.edu",
                "Computer Science and Engineering", 3, 5, "A");
        sampleStudent = studentRepository.save(sampleStudent);

        sampleStudent2 = new Student("21CSE002", "Diya", "Patel", "diya.patel@college.edu",
                "Computer Science and Engineering", 3, 5, "A");
        sampleStudent2 = studentRepository.save(sampleStudent2);

        // Prepare sample faculty
        sampleFaculty = new Faculty("FAC001", "Dr. Ravi", "Kumar", "ravi.kumar@college.edu",
                "Computer Science and Engineering", "Professor");
        sampleFaculty = facultyRepository.save(sampleFaculty);

        // Prepare sample activity
        sampleActivity = new Activity();
        sampleActivity.setActivityCode("ACT-CS501-LAB");
        sampleActivity.setTitle("Data Structures & Algorithms Laboratory");
        sampleActivity.setActivityType("LAB");
        sampleActivity.setDepartment("Computer Science and Engineering");
        sampleActivity.setFaculty(sampleFaculty);
        sampleActivity.setStatus("ACTIVE");
        sampleActivity = activityRepository.save(sampleActivity);

        // Prepare sample attendance
        sampleAttendance = new Attendance();
        sampleAttendance.setStudent(sampleStudent);
        sampleAttendance.setActivity(sampleActivity);
        sampleAttendance.setMarkedByFaculty(sampleFaculty);
        sampleAttendance.setAttendanceDate(LocalDate.of(2026, 9, 10));
        sampleAttendance.setStatus("PRESENT");
        sampleAttendance.setSessionSlot("SESSION_1");
        sampleAttendance.setRemarks("Attended DSA Lab on Trees");
    }

    @Test
    @DisplayName("Should persist attendance record and populate ID, audit timestamps, and relations")
    void shouldSaveAndPersistAttendance() {
        Attendance saved = attendanceRepository.save(sampleAttendance);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStudent()).isNotNull();
        assertThat(saved.getStudent().getRollNumber()).isEqualTo("21CSE001");
        assertThat(saved.getActivity()).isNotNull();
        assertThat(saved.getActivity().getActivityCode()).isEqualTo("ACT-CS501-LAB");
        assertThat(saved.getMarkedByFaculty().getEmployeeId()).isEqualTo("FAC001");
        assertThat(saved.getAttendanceDate()).isEqualTo(LocalDate.of(2026, 9, 10));
        assertThat(saved.getStatus()).isEqualTo("PRESENT");
        assertThat(saved.getSessionSlot()).isEqualTo("SESSION_1");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should find attendance records by student roll number")
    void shouldFindAttendanceByStudentRollNumber() {
        attendanceRepository.save(sampleAttendance);

        List<Attendance> records = attendanceRepository.findByStudentRollNumber("21CSE001");

        assertThat(records).hasSize(1);
        assertThat(records.get(0).getStudent().getRollNumber()).isEqualTo("21CSE001");
        assertThat(records.get(0).getActivity().getActivityCode()).isEqualTo("ACT-CS501-LAB");
    }

    @Test
    @DisplayName("Should find attendance records by activity code")
    void shouldFindAttendanceByActivityCode() {
        attendanceRepository.save(sampleAttendance);

        Attendance secondRecord = new Attendance(sampleStudent2, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 10), "ABSENT", "SESSION_1", "Medical leave");
        attendanceRepository.save(secondRecord);

        List<Attendance> activityLogs = attendanceRepository.findByActivityActivityCode("ACT-CS501-LAB");

        assertThat(activityLogs).hasSize(2);
    }

    @Test
    @DisplayName("Should find attendance records by specific date")
    void shouldFindAttendanceByDate() {
        attendanceRepository.save(sampleAttendance);

        Attendance day2Record = new Attendance(sampleStudent, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 11), "PRESENT", "SESSION_1", "Graph BFS Session");
        attendanceRepository.save(day2Record);

        List<Attendance> sept10 = attendanceRepository.findByAttendanceDate(LocalDate.of(2026, 9, 10));
        List<Attendance> sept11 = attendanceRepository.findByAttendanceDate(LocalDate.of(2026, 9, 11));

        assertThat(sept10).hasSize(1);
        assertThat(sept11).hasSize(1);
    }

    @Test
    @DisplayName("Should find attendance records by status")
    void shouldFindAttendanceByStatus() {
        attendanceRepository.save(sampleAttendance); // PRESENT

        Attendance absentRecord = new Attendance(sampleStudent2, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 10), "ABSENT", "SESSION_1");
        attendanceRepository.save(absentRecord);

        List<Attendance> presentList = attendanceRepository.findByStatus("PRESENT");
        List<Attendance> absentList = attendanceRepository.findByStatus("ABSENT");

        assertThat(presentList).hasSize(1);
        assertThat(absentList).hasSize(1);
    }

    @Test
    @DisplayName("Should query attendance within date range")
    void shouldFindAttendanceWithinDateRange() {
        attendanceRepository.save(sampleAttendance); // 2026-09-10

        Attendance day2 = new Attendance(sampleStudent, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 12), "PRESENT", "SESSION_1");
        attendanceRepository.save(day2);

        Attendance day3 = new Attendance(sampleStudent, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 20), "PRESENT", "SESSION_1");
        attendanceRepository.save(day3);

        List<Attendance> rangeRecords = attendanceRepository.findByAttendanceDateBetween(
                LocalDate.of(2026, 9, 9), LocalDate.of(2026, 9, 15));

        assertThat(rangeRecords).hasSize(2);
    }

    @Test
    @DisplayName("Should count attendance records by student and status")
    void shouldCountAttendanceByStudentAndStatus() {
        attendanceRepository.save(sampleAttendance); // 1 PRESENT for sampleStudent

        Attendance record2 = new Attendance(sampleStudent, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 11), "PRESENT", "SESSION_1");
        attendanceRepository.save(record2);

        Attendance record3 = new Attendance(sampleStudent, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 12), "ABSENT", "SESSION_1");
        attendanceRepository.save(record3);

        long presentCount = attendanceRepository.countByStudentIdAndStatus(sampleStudent.getId(), "PRESENT");
        long absentCount = attendanceRepository.countByStudentIdAndStatus(sampleStudent.getId(), "ABSENT");
        long totalStudentCount = attendanceRepository.countByStudentId(sampleStudent.getId());

        assertThat(presentCount).isEqualTo(2);
        assertThat(absentCount).isEqualTo(1);
        assertThat(totalStudentCount).isEqualTo(3);
    }

    @Test
    @DisplayName("Should check existence by student, activity, date, and session slot")
    void shouldCheckExistenceBySlot() {
        attendanceRepository.save(sampleAttendance);

        boolean exists = attendanceRepository.existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
                sampleStudent.getId(), sampleActivity.getId(), LocalDate.of(2026, 9, 10), "SESSION_1");
        boolean nonExistent = attendanceRepository.existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
                sampleStudent.getId(), sampleActivity.getId(), LocalDate.of(2026, 9, 10), "SESSION_2");

        assertThat(exists).isTrue();
        assertThat(nonExistent).isFalse();
    }

    @Test
    @DisplayName("Should throw exception for duplicate attendance in same session slot")
    void shouldEnforceUniqueConstraintOnSessionSlot() {
        attendanceRepository.saveAndFlush(sampleAttendance);

        Attendance duplicate = new Attendance(sampleStudent, sampleActivity, sampleFaculty,
                LocalDate.of(2026, 9, 10), "ABSENT", "SESSION_1", "Duplicate test");

        assertThrows(DataIntegrityViolationException.class, () -> {
            attendanceRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Should update status and remarks of attendance")
    void shouldUpdateAttendanceRecord() {
        Attendance saved = attendanceRepository.save(sampleAttendance);
        Long id = saved.getId();

        saved.setStatus("LATE");
        saved.setRemarks("Arrived 15 minutes late due to bus delay");
        Attendance updated = attendanceRepository.saveAndFlush(saved);

        assertThat(updated.getStatus()).isEqualTo("LATE");
        assertThat(updated.getRemarks()).contains("bus delay");

        attendanceRepository.delete(updated);
        assertThat(attendanceRepository.findById(id)).isEmpty();
    }
}
