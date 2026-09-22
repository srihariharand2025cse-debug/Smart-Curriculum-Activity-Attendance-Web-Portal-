package com.smartcurriculum.portal.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO representing a request to mark attendance for a student in an activity.
 */
public class AttendanceRequestDto {

    @NotNull
    private Long studentId;

    @NotNull
    private Long activityId;

    @NotNull
    private Long facultyId; // faculty who marks the attendance

    @NotNull
    private LocalDate attendanceDate;

    private String status = "PRESENT"; // default status

    private String sessionSlot = "SESSION_1"; // default slot

    private String remarks;

    // Getters and setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSessionSlot() { return sessionSlot; }
    public void setSessionSlot(String sessionSlot) { this.sessionSlot = sessionSlot; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
