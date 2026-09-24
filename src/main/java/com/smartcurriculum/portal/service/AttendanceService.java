package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.AttendanceResponseDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto;

import java.util.List;

/**
 * Service interface for attendance related operations and visual analytics.
 */
public interface AttendanceService {

    /**
     * Marks attendance for a student in an activity.
     */
    AttendanceResponseDto markAttendance(AttendanceRequestDto request);

    /**
     * Provides summary statistics and visual metrics for a student's attendance by ID.
     */
    AttendanceSummaryDto getStudentAttendanceSummary(Long studentId);

    /**
     * Provides summary statistics and visual metrics for a student's attendance by roll number.
     */
    AttendanceSummaryDto getStudentAttendanceSummaryByRollNumber(String rollNumber);

    /**
     * Provides summary statistics for an activity's attendance.
     */
    AttendanceSummaryDto getActivityAttendanceSummary(Long activityId);

    /**
     * Retrieves all attendance records.
     */
    List<AttendanceResponseDto> getAllAttendance();

    /**
     * Retrieves all attendance records for a student by ID.
     */
    List<AttendanceResponseDto> getAttendanceByStudentId(Long studentId);

    /**
     * Retrieves all attendance records for a student by Roll Number.
     */
    List<AttendanceResponseDto> getAttendanceByRollNumber(String rollNumber);
}
