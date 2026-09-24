package com.smartcurriculum.portal.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing comprehensive attendance summary
 * and visual metrics for a Student or Activity.
 */
public class AttendanceSummaryDto {

    private Long studentId;
    private String studentRollNumber;
    private String studentName;
    private String department;
    private Integer yearOfStudy;

    private Long activityId;
    private String activityCode;
    private String activityTitle;

    private long totalSessions;
    private long attendedSessions;
    private long absentSessions;
    private long onDutySessions;
    private double attendancePercentage;

    private String eligibilityStatus; // "ELIGIBLE", "WARNING", "CRITICAL"
    private String eligibilityBadge;  // "Good Standing (>75%)", "Condonation Required (65-75%)", "Low Attendance Alert (<65%)"
    private String statusMessage;

    private List<ActivityAttendanceMetric> activityBreakdown = new ArrayList<>();
    private List<AttendanceResponseDto> recentRecords = new ArrayList<>();

    public AttendanceSummaryDto() {
    }

    public static class ActivityAttendanceMetric {
        private Long activityId;
        private String activityCode;
        private String activityTitle;
        private String activityType;
        private long totalSessions;
        private long attendedSessions;
        private double percentage;

        public ActivityAttendanceMetric() {
        }

        public ActivityAttendanceMetric(Long activityId, String activityCode, String activityTitle,
                                        String activityType, long totalSessions, long attendedSessions, double percentage) {
            this.activityId = activityId;
            this.activityCode = activityCode;
            this.activityTitle = activityTitle;
            this.activityType = activityType;
            this.totalSessions = totalSessions;
            this.attendedSessions = attendedSessions;
            this.percentage = percentage;
        }

        public Long getActivityId() { return activityId; }
        public void setActivityId(Long activityId) { this.activityId = activityId; }
        public String getActivityCode() { return activityCode; }
        public void setActivityCode(String activityCode) { this.activityCode = activityCode; }
        public String getActivityTitle() { return activityTitle; }
        public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }
        public String getActivityType() { return activityType; }
        public void setActivityType(String activityType) { this.activityType = activityType; }
        public long getTotalSessions() { return totalSessions; }
        public void setTotalSessions(long totalSessions) { this.totalSessions = totalSessions; }
        public long getAttendedSessions() { return attendedSessions; }
        public void setAttendedSessions(long attendedSessions) { this.attendedSessions = attendedSessions; }
        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
    }

    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentRollNumber() { return studentRollNumber; }
    public void setStudentRollNumber(String studentRollNumber) { this.studentRollNumber = studentRollNumber; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }

    public String getActivityCode() { return activityCode; }
    public void setActivityCode(String activityCode) { this.activityCode = activityCode; }

    public String getActivityTitle() { return activityTitle; }
    public void setActivityTitle(String activityTitle) { this.activityTitle = activityTitle; }

    public long getTotalSessions() { return totalSessions; }
    public void setTotalSessions(long totalSessions) { this.totalSessions = totalSessions; }

    public long getAttendedSessions() { return attendedSessions; }
    public void setAttendedSessions(long attendedSessions) { this.attendedSessions = attendedSessions; }

    public long getAbsentSessions() { return absentSessions; }
    public void setAbsentSessions(long absentSessions) { this.absentSessions = absentSessions; }

    public long getOnDutySessions() { return onDutySessions; }
    public void setOnDutySessions(long onDutySessions) { this.onDutySessions = onDutySessions; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public String getEligibilityStatus() { return eligibilityStatus; }
    public void setEligibilityStatus(String eligibilityStatus) { this.eligibilityStatus = eligibilityStatus; }

    public String getEligibilityBadge() { return eligibilityBadge; }
    public void setEligibilityBadge(String eligibilityBadge) { this.eligibilityBadge = eligibilityBadge; }

    public String getStatusMessage() { return statusMessage; }
    public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }

    public List<ActivityAttendanceMetric> getActivityBreakdown() { return activityBreakdown; }
    public void setActivityBreakdown(List<ActivityAttendanceMetric> activityBreakdown) { this.activityBreakdown = activityBreakdown; }

    public List<AttendanceResponseDto> getRecentRecords() { return recentRecords; }
    public void setRecentRecords(List<AttendanceResponseDto> recentRecords) { this.recentRecords = recentRecords; }
}
