package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.ApiResponse;
import com.smartcurriculum.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller to verify portal status, health, database connection,
 * and Day 5 Student Entity & Database Mapping readiness.
 */
@RestController
public class HomeController {

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired(required = false)
    private StudentRepository studentRepository;

    @GetMapping("/")
    public String home() {
        return "Welcome to Smart Curriculum Activity & Attendance Web Portal! Day 5 Student Entity and Database Mapping is Complete.";
    }

    @GetMapping("/api/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus() {
        Map<String, Object> statusData = new LinkedHashMap<>();
        statusData.put("status", "UP");
        statusData.put("project", "Smart Curriculum Activity & Attendance Web Portal");
        statusData.put("currentMilestone", "Day 5: Create Student Entity and Database Mapping");
        statusData.put("layersConfigured", new String[]{
                "controller",
                "service (interface & impl)",
                "repository (StudentRepository)",
                "entity (Student)",
                "dto",
                "exception",
                "database (MySQL DataSource & Hibernate JPA)"
        });
        statusData.put("databaseConfigured", true);
        statusData.put("studentEntityMapped", true);
        statusData.put("totalStudents", studentRepository != null ? studentRepository.count() : 0L);
        statusData.put("nextMilestone", "Day 6: Create Faculty Entity and Database Mapping");

        return ResponseEntity.ok(ApiResponse.success("Portal API is running smoothly", statusData));
    }

    @GetMapping("/api/db-status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDatabaseStatus() {
        Map<String, Object> dbInfo = new LinkedHashMap<>();

        if (dataSource == null) {
            dbInfo.put("connected", false);
            dbInfo.put("message", "No DataSource bean configured");
            return ResponseEntity.ok(ApiResponse.error("Database connection unavailable", dbInfo));
        }

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            dbInfo.put("connected", true);
            dbInfo.put("databaseProductName", metaData.getDatabaseProductName());
            dbInfo.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            dbInfo.put("driverName", metaData.getDriverName());
            dbInfo.put("driverVersion", metaData.getDriverVersion());
            dbInfo.put("databaseUrl", metaData.getURL());
            return ResponseEntity.ok(ApiResponse.success("Database connection is healthy", dbInfo));
        } catch (Exception e) {
            dbInfo.put("connected", false);
            dbInfo.put("error", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Failed to connect to database: " + e.getMessage(), dbInfo));
        }
    }

    @GetMapping("/api/students/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStudentSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();
        boolean isAvailable = (studentRepository != null);
        summary.put("entityMapped", true);
        summary.put("repositoryActive", isAvailable);
        summary.put("totalStudents", isAvailable ? studentRepository.count() : 0L);
        summary.put("entityClass", "com.smartcurriculum.portal.entity.Student");
        summary.put("tableName", "students");
        return ResponseEntity.ok(ApiResponse.success("Student entity and database mapping are active", summary));
    }
}
