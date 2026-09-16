package com.smartcurriculum.portal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Activity Domain Entity representing a curriculum or extracurricular activity
 * in the Smart Curriculum Activity & Attendance Web Portal.
 *
 * Maps to the relational table `activities` with a foreign key reference
 * to the coordinating {@link Faculty} member.
 */
@Entity
@Table(
        name = "activities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_activities_activity_code", columnNames = "activity_code")
        },
        indexes = {
                @Index(name = "idx_activities_activity_code", columnList = "activity_code"),
                @Index(name = "idx_activities_department", columnList = "department"),
                @Index(name = "idx_activities_activity_type", columnList = "activity_type"),
                @Index(name = "idx_activities_status", columnList = "status"),
                @Index(name = "idx_activities_dept_sem", columnList = "department, semester")
        }
)
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_code", nullable = false, unique = true, length = 50)
    private String activityCode;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "academic_year", length = 20)
    private String academicYear;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "venue", length = 150)
    private String venue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "faculty_id",
            foreignKey = @ForeignKey(name = "fk_activities_faculty")
    )
    private Faculty faculty;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "max_enrollment")
    private Integer maxEnrollment;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Activity() {
    }

    /**
     * Parameterized constructor for mandatory and essential fields.
     */
    public Activity(String activityCode, String title, String activityType,
                    String department, Integer semester, Integer credits) {
        this.activityCode = activityCode;
        this.title = title;
        this.activityType = activityType;
        this.department = department;
        this.semester = semester;
        this.credits = credits;
        this.status = "ACTIVE";
    }

    /**
     * Full parameterized constructor.
     */
    public Activity(Long id, String activityCode, String title, String description,
                    String activityType, String department, String academicYear,
                    Integer semester, Integer credits, String venue, Faculty faculty,
                    LocalDate startDate, LocalDate endDate, Integer maxEnrollment,
                    String status) {
        this.id = id;
        this.activityCode = activityCode;
        this.title = title;
        this.description = description;
        this.activityType = activityType;
        this.department = department;
        this.academicYear = academicYear;
        this.semester = semester;
        this.credits = credits;
        this.venue = venue;
        this.faculty = faculty;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxEnrollment = maxEnrollment;
        this.status = (status != null && !status.isBlank()) ? status : "ACTIVE";
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
        if (this.status == null || this.status.isBlank()) {
            this.status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(Integer maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(activityCode, activity.activityCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityCode);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", activityCode='" + activityCode + '\'' +
                ", title='" + title + '\'' +
                ", activityType='" + activityType + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", credits=" + credits +
                ", status='" + status + '\'' +
                '}';
    }
}
