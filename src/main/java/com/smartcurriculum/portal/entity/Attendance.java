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
 * Attendance Domain Entity representing an attendance log entry for a student
 * attending a curriculum activity or session in the Smart Curriculum Activity & Attendance Web Portal.
 *
 * Maps to relational table `attendance` with foreign keys referencing {@link Student},
 * {@link Activity}, and the approving {@link Faculty} member.
 */
@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_attendance_record",
                        columnNames = {"student_id", "activity_id", "attendance_date", "session_slot"}
                )
        },
        indexes = {
                @Index(name = "idx_attendance_student_id", columnList = "student_id"),
                @Index(name = "idx_attendance_activity_id", columnList = "activity_id"),
                @Index(name = "idx_attendance_faculty_id", columnList = "faculty_id"),
                @Index(name = "idx_attendance_date", columnList = "attendance_date"),
                @Index(name = "idx_attendance_status", columnList = "status"),
                @Index(name = "idx_attendance_student_date", columnList = "student_id, attendance_date"),
                @Index(name = "idx_attendance_act_date", columnList = "activity_id, attendance_date")
        }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attendance_student")
    )
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "activity_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attendance_activity")
    )
    private Activity activity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "faculty_id",
            foreignKey = @ForeignKey(name = "fk_attendance_faculty")
    )
    private Faculty markedByFaculty;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "PRESENT";

    @Column(name = "session_slot", nullable = false, length = 50)
    private String sessionSlot = "SESSION_1";

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Attendance() {
    }

    /**
     * Parameterized constructor for mandatory and core attendance fields.
     */
    public Attendance(Student student, Activity activity, Faculty markedByFaculty,
                      LocalDate attendanceDate, String status, String sessionSlot) {
        this.student = student;
        this.activity = activity;
        this.markedByFaculty = markedByFaculty;
        this.attendanceDate = attendanceDate;
        this.status = status != null ? status : "PRESENT";
        this.sessionSlot = sessionSlot != null ? sessionSlot : "SESSION_1";
    }

    /**
     * Parameterized constructor with remarks.
     */
    public Attendance(Student student, Activity activity, Faculty markedByFaculty,
                      LocalDate attendanceDate, String status, String sessionSlot, String remarks) {
        this(student, activity, markedByFaculty, attendanceDate, status, sessionSlot);
        this.remarks = remarks;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "PRESENT";
        }
        if (this.sessionSlot == null || this.sessionSlot.trim().isEmpty()) {
            this.sessionSlot = "SESSION_1";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "PRESENT";
        }
        if (this.sessionSlot == null || this.sessionSlot.trim().isEmpty()) {
            this.sessionSlot = "SESSION_1";
        }
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Faculty getMarkedByFaculty() {
        return markedByFaculty;
    }

    public void setMarkedByFaculty(Faculty markedByFaculty) {
        this.markedByFaculty = markedByFaculty;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionSlot() {
        return sessionSlot;
    }

    public void setSessionSlot(String sessionSlot) {
        this.sessionSlot = sessionSlot;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    // --- Equals, HashCode, ToString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }
        return Objects.equals(student, that.student) &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(attendanceDate, that.attendanceDate) &&
                Objects.equals(sessionSlot, that.sessionSlot);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(student, activity, attendanceDate, sessionSlot);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", studentRollNumber=" + (student != null ? student.getRollNumber() : "null") +
                ", activityCode=" + (activity != null ? activity.getActivityCode() : "null") +
                ", attendanceDate=" + attendanceDate +
                ", status='" + status + '\'' +
                ", sessionSlot='" + sessionSlot + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
