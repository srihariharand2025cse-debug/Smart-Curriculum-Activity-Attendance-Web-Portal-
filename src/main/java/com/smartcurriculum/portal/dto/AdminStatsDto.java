package com.smartcurriculum.portal.dto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object aggregating centralized institutional statistics,
 * health status, and analytics for the Admin Dashboard (Day 17).
 */
public class AdminStatsDto {

    // Student Metrics
    private long totalStudents;
    private long activeStudents;
    private long inactiveStudents;
    private Map<String, Long> studentsByDepartment;
    private Map<Integer, Long> studentsByYear;

    // Faculty Metrics
    private long totalFaculty;
    private long activeFaculty;
    private long inactiveFaculty;
    private Map<String, Long> facultyByDepartment;
    private Map<String, Long> facultyByDesignation;

    // Activity Metrics
    private long totalActivities;
    private long activeActivities;
    private long upcomingActivities;
    private long completedActivities;
    private Map<String, Long> activitiesByType;
    private Map<String, Long> activitiesByDepartment;

    // Attendance Metrics
    private long totalAttendanceRecords;
    private long presentCount;
    private long absentCount;
    private long onDutyCount;
    private double overallAttendancePercentage;

    // System Status
    private boolean databaseConnected;
    private String databaseProductName;
    private LocalDateTime serverTimestamp;

    public AdminStatsDto() {
        this.serverTimestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(long activeStudents) {
        this.activeStudents = activeStudents;
    }

    public long getInactiveStudents() {
        return inactiveStudents;
    }

    public void setInactiveStudents(long inactiveStudents) {
        this.inactiveStudents = inactiveStudents;
    }

    public Map<String, Long> getStudentsByDepartment() {
        return studentsByDepartment;
    }

    public void setStudentsByDepartment(Map<String, Long> studentsByDepartment) {
        this.studentsByDepartment = studentsByDepartment;
    }

    public Map<Integer, Long> getStudentsByYear() {
        return studentsByYear;
    }

    public void setStudentsByYear(Map<Integer, Long> studentsByYear) {
        this.studentsByYear = studentsByYear;
    }

    public long getTotalFaculty() {
        return totalFaculty;
    }

    public void setTotalFaculty(long totalFaculty) {
        this.totalFaculty = totalFaculty;
    }

    public long getActiveFaculty() {
        return activeFaculty;
    }

    public void setActiveFaculty(long activeFaculty) {
        this.activeFaculty = activeFaculty;
    }

    public long getInactiveFaculty() {
        return inactiveFaculty;
    }

    public void setInactiveFaculty(long inactiveFaculty) {
        this.inactiveFaculty = inactiveFaculty;
    }

    public Map<String, Long> getFacultyByDepartment() {
        return facultyByDepartment;
    }

    public void setFacultyByDepartment(Map<String, Long> facultyByDepartment) {
        this.facultyByDepartment = facultyByDepartment;
    }

    public Map<String, Long> getFacultyByDesignation() {
        return facultyByDesignation;
    }

    public void setFacultyByDesignation(Map<String, Long> facultyByDesignation) {
        this.facultyByDesignation = facultyByDesignation;
    }

    public long getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(long totalActivities) {
        this.totalActivities = totalActivities;
    }

    public long getActiveActivities() {
        return activeActivities;
    }

    public void setActiveActivities(long activeActivities) {
        this.activeActivities = activeActivities;
    }

    public long getUpcomingActivities() {
        return upcomingActivities;
    }

    public void setUpcomingActivities(long upcomingActivities) {
        this.upcomingActivities = upcomingActivities;
    }

    public long getCompletedActivities() {
        return completedActivities;
    }

    public void setCompletedActivities(long completedActivities) {
        this.completedActivities = completedActivities;
    }

    public Map<String, Long> getActivitiesByType() {
        return activitiesByType;
    }

    public void setActivitiesByType(Map<String, Long> activitiesByType) {
        this.activitiesByType = activitiesByType;
    }

    public Map<String, Long> getActivitiesByDepartment() {
        return activitiesByDepartment;
    }

    public void setActivitiesByDepartment(Map<String, Long> activitiesByDepartment) {
        this.activitiesByDepartment = activitiesByDepartment;
    }

    public long getTotalAttendanceRecords() {
        return totalAttendanceRecords;
    }

    public void setTotalAttendanceRecords(long totalAttendanceRecords) {
        this.totalAttendanceRecords = totalAttendanceRecords;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(long presentCount) {
        this.presentCount = presentCount;
    }

    public long getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(long absentCount) {
        this.absentCount = absentCount;
    }

    public long getOnDutyCount() {
        return onDutyCount;
    }

    public void setOnDutyCount(long onDutyCount) {
        this.onDutyCount = onDutyCount;
    }

    public double getOverallAttendancePercentage() {
        return overallAttendancePercentage;
    }

    public void setOverallAttendancePercentage(double overallAttendancePercentage) {
        this.overallAttendancePercentage = overallAttendancePercentage;
    }

    public boolean isDatabaseConnected() {
        return databaseConnected;
    }

    public void setDatabaseConnected(boolean databaseConnected) {
        this.databaseConnected = databaseConnected;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public LocalDateTime getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(LocalDateTime serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }
}
