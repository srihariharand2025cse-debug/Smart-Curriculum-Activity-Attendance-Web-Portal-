package com.smartcurriculum.portal.dto;

import com.smartcurriculum.portal.entity.Attendance;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO representing attendance details returned to the client.
 */
public class AttendanceResponseDto {
    private Long id;
    private Long studentId;
    private String studentRollNumber;
    private Long activityId;
    private String activityCode;
    private Long facultyId;
    private LocalDate attendanceDate;
    private String status;
    private String sessionSlot;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AttendanceResponseDto fromEntity(Attendance attendance) {
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.id = attendance.getId();
        if (attendance.getStudent() != null) {
            dto.studentId = attendance.getStudent().getId();
            dto.studentRollNumber = attendance.getStudent().getRollNumber();
        }
        if (attendance.getActivity() != null) {
            dto.activityId = attendance.getActivity().getId();
            dto.activityCode = attendance.getActivity().getActivityCode();
        }
        if (attendance.getMarkedByFaculty() != null) {
            dto.facultyId = attendance.getMarkedByFaculty().getId();
        }
        dto.attendanceDate = attendance.getAttendanceDate();
        dto.status = attendance.getStatus();
        dto.sessionSlot = attendance.getSessionSlot();
        dto.remarks = attendance.getRemarks();
        dto.createdAt = attendance.getCreatedAt();
        dto.updatedAt = attendance.getUpdatedAt();
        return dto;
    }

    // Getters and setters (omitted for brevity) 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getStudentRollNumber() { return studentRollNumber; }
    public void setStudentRollNumber(String studentRollNumber) { this.studentRollNumber = studentRollNumber; }
    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public String getActivityCode() { return activityCode; }
    public void setActivityCode(String activityCode) { this.activityCode = activityCode; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
