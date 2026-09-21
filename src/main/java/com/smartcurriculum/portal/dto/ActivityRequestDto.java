package com.smartcurriculum.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO for creating or updating an Activity.
 */
public class ActivityRequestDto {

    @NotBlank(message = "Activity code is required")
    @Size(max = 50)
    private String activityCode;

    @NotBlank(message = "Title is required")
    @Size(max = 150)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotBlank(message = "Activity type is required")
    @Size(max = 50)
    private String activityType;

    @NotBlank(message = "Department is required")
    @Size(max = 100)
    private String department;

    @Size(max = 20)
    private String academicYear;

    @PositiveOrZero(message = "Semester must be zero or positive")
    private Integer semester;

    @PositiveOrZero(message = "Credits must be zero or positive")
    private Integer credits;

    @Size(max = 150)
    private String venue;

    /**
     * ID of the faculty coordinating the activity. Optional – can be set later.
     */
    private Long facultyId;

    private LocalDate startDate;
    private LocalDate endDate;

    @PositiveOrZero(message = "Max enrollment must be zero or positive")
    private Integer maxEnrollment;

    @NotBlank(message = "Status is required")
    @Size(max = 30)
    private String status = "ACTIVE";

    // Getters and setters
    public String getActivityCode() { return activityCode; }
    public void setActivityCode(String activityCode) { this.activityCode = activityCode; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Integer getMaxEnrollment() { return maxEnrollment; }
    public void setMaxEnrollment(Integer maxEnrollment) { this.maxEnrollment = maxEnrollment; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
