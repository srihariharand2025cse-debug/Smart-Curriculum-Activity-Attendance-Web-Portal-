package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.ApiResponse;
import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.AttendanceResponseDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto;
import com.smartcurriculum.portal.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Attendance operations, calculations, and visual analytics endpoints.
 */
@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * Mark an attendance record.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponseDto>> markAttendance(
            @Valid @RequestBody AttendanceRequestDto requestDto) {
        AttendanceResponseDto response = attendanceService.markAttendance(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Attendance marked successfully", response));
    }

    /**
     * Get attendance records with optional filtering.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getAttendance(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) String rollNumber) {
        List<AttendanceResponseDto> result;
        if (studentId != null) {
            result = attendanceService.getAttendanceByStudentId(studentId);
        } else if (rollNumber != null && !rollNumber.isBlank()) {
            result = attendanceService.getAttendanceByRollNumber(rollNumber);
        } else {
            result = attendanceService.getAllAttendance();
        }
        return ResponseEntity.ok(ApiResponse.success("Attendance records retrieved successfully", result));
    }

    /**
     * Get visual metrics and summary for a student by ID.
     */
    @GetMapping("/student/{studentId}/summary")
    public ResponseEntity<ApiResponse<AttendanceSummaryDto>> getStudentSummary(
            @PathVariable Long studentId) {
        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student attendance summary retrieved successfully", summary));
    }

    /**
     * Get visual metrics and summary for a student by Roll Number.
     */
    @GetMapping("/student/roll/{rollNumber}/summary")
    public ResponseEntity<ApiResponse<AttendanceSummaryDto>> getStudentSummaryByRoll(
            @PathVariable String rollNumber) {
        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummaryByRollNumber(rollNumber);
        return ResponseEntity.ok(ApiResponse.success("Student attendance summary retrieved successfully", summary));
    }

    /**
     * Get visual metrics and summary for an activity by ID.
     */
    @GetMapping("/activity/{activityId}/summary")
    public ResponseEntity<ApiResponse<AttendanceSummaryDto>> getActivitySummary(
            @PathVariable Long activityId) {
        AttendanceSummaryDto summary = attendanceService.getActivityAttendanceSummary(activityId);
        return ResponseEntity.ok(ApiResponse.success("Activity attendance summary retrieved successfully", summary));
    }
}
