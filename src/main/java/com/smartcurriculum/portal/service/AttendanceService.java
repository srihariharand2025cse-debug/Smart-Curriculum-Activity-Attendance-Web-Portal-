package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.AttendanceResponseDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto;

/**
 * Service interface for attendance related operations.
 */
public interface AttendanceService {

    /**
     * Marks attendance for a student in an activity.
     */
    AttendanceResponseDto markAttendance(AttendanceRequestDto request);

    /**
     * Provides summary statistics for a student's attendance.
     */
    AttendanceSummaryDto getStudentAttendanceSummary(Long studentId);

    /**
     * Provides summary statistics for an activity's attendance.
     */
    AttendanceSummaryDto getActivityAttendanceSummary(Long activityId);
}
