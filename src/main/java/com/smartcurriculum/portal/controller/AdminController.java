package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.AdminStatsDto;
import com.smartcurriculum.portal.dto.ApiResponse;
import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.repository.AttendanceRepository;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller providing centralized administration controls,
 * system-wide KPI statistics, and management endpoints for the Admin Dashboard (Day 17).
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final DataSource dataSource;

    @Autowired
    public AdminController(StudentRepository studentRepository,
                           FacultyRepository facultyRepository,
                           ActivityRepository activityRepository,
                           AttendanceRepository attendanceRepository,
                           @Autowired(required = false) DataSource dataSource) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.activityRepository = activityRepository;
        this.attendanceRepository = attendanceRepository;
        this.dataSource = dataSource;
    }

    /**
     * Retrieves centralized statistical metrics, attendance distribution,
     * and department breakdowns for the Admin Dashboard.
     * GET /api/admin/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsDto>> getAdminDashboardStats() {
        AdminStatsDto stats = new AdminStatsDto();

        // 1. Student Statistics
        List<Student> students = studentRepository.findAll();
        stats.setTotalStudents(students.size());
        stats.setActiveStudents(students.stream()
                .filter(s -> "ACTIVE".equalsIgnoreCase(s.getStatus()))
                .count());
        stats.setInactiveStudents(students.stream()
                .filter(s -> !"ACTIVE".equalsIgnoreCase(s.getStatus()))
                .count());

        Map<String, Long> studentsByDept = students.stream()
                .filter(s -> s.getDepartment() != null)
                .collect(Collectors.groupingBy(Student::getDepartment, LinkedHashMap::new, Collectors.counting()));
        stats.setStudentsByDepartment(studentsByDept);

        Map<Integer, Long> studentsByYear = students.stream()
                .filter(s -> s.getYearOfStudy() != null)
                .collect(Collectors.groupingBy(Student::getYearOfStudy, LinkedHashMap::new, Collectors.counting()));
        stats.setStudentsByYear(studentsByYear);

        // 2. Faculty Statistics
        List<Faculty> facultyList = facultyRepository.findAll();
        stats.setTotalFaculty(facultyList.size());
        stats.setActiveFaculty(facultyList.stream()
                .filter(f -> "ACTIVE".equalsIgnoreCase(f.getStatus()))
                .count());
        stats.setInactiveFaculty(facultyList.stream()
                .filter(f -> !"ACTIVE".equalsIgnoreCase(f.getStatus()))
                .count());

        Map<String, Long> facultyByDept = facultyList.stream()
                .filter(f -> f.getDepartment() != null)
                .collect(Collectors.groupingBy(Faculty::getDepartment, LinkedHashMap::new, Collectors.counting()));
        stats.setFacultyByDepartment(facultyByDept);

        Map<String, Long> facultyByDesig = facultyList.stream()
                .filter(f -> f.getDesignation() != null)
                .collect(Collectors.groupingBy(Faculty::getDesignation, LinkedHashMap::new, Collectors.counting()));
        stats.setFacultyByDesignation(facultyByDesig);

        // 3. Activity Statistics
        List<Activity> activities = activityRepository.findAll();
        stats.setTotalActivities(activities.size());
        stats.setActiveActivities(activities.stream()
                .filter(a -> "ACTIVE".equalsIgnoreCase(a.getStatus()))
                .count());
        stats.setUpcomingActivities(activities.stream()
                .filter(a -> "UPCOMING".equalsIgnoreCase(a.getStatus()))
                .count());
        stats.setCompletedActivities(activities.stream()
                .filter(a -> "COMPLETED".equalsIgnoreCase(a.getStatus()))
                .count());

        Map<String, Long> activitiesByType = activities.stream()
                .filter(a -> a.getActivityType() != null)
                .collect(Collectors.groupingBy(Activity::getActivityType, LinkedHashMap::new, Collectors.counting()));
        stats.setActivitiesByType(activitiesByType);

        Map<String, Long> activitiesByDept = activities.stream()
                .filter(a -> a.getDepartment() != null)
                .collect(Collectors.groupingBy(Activity::getDepartment, LinkedHashMap::new, Collectors.counting()));
        stats.setActivitiesByDepartment(activitiesByDept);

        // 4. Attendance Statistics
        long totalAtt = attendanceRepository.count();
        long presentCount = attendanceRepository.countByStatus("PRESENT");
        long absentCount = attendanceRepository.countByStatus("ABSENT");
        long onDutyCount = attendanceRepository.countByStatus("ON_DUTY");

        stats.setTotalAttendanceRecords(totalAtt);
        stats.setPresentCount(presentCount);
        stats.setAbsentCount(absentCount);
        stats.setOnDutyCount(onDutyCount);

        double rate = 0.0;
        if (totalAtt > 0) {
            rate = ((double) (presentCount + onDutyCount) / totalAtt) * 100.0;
        }
        stats.setOverallAttendancePercentage(Math.round(rate * 10.0) / 10.0);

        // 5. System & Database Health
        boolean isConnected = false;
        String dbProduct = "MySQL Server";
        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                isConnected = true;
                dbProduct = meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion();
            } catch (Exception ignored) {
                isConnected = false;
            }
        }
        stats.setDatabaseConnected(isConnected);
        stats.setDatabaseProductName(dbProduct);

        return ResponseEntity.ok(ApiResponse.success("Centralized admin statistics retrieved successfully", stats));
    }

    /**
     * Admin quick status toggle / approval for an activity.
     * PUT /api/admin/activities/{id}/status?status=ACTIVE
     */
    @PutMapping("/activities/{id}/status")
    public ResponseEntity<ApiResponse<Activity>> updateActivityStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + id));
        activity.setStatus(status.toUpperCase());
        Activity updated = activityRepository.save(activity);
        return ResponseEntity.ok(ApiResponse.success("Activity status updated successfully to " + status.toUpperCase(), updated));
    }
}
