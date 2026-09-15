package com.smartcurriculum.portal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Student Domain Entity representing a registered student in the
 * Smart Curriculum Activity & Attendance Web Portal.
 *
 * Maps to the relational table `students` with constraints on roll number and email.
 */
@Entity
@Table(
        name = "students",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_students_roll_number", columnNames = "roll_number"),
                @UniqueConstraint(name = "uk_students_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_students_roll_number", columnList = "roll_number"),
                @Index(name = "idx_students_department", columnList = "department"),
                @Index(name = "idx_students_year_dept", columnList = "department, year_of_study")
        }
)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_number", nullable = false, unique = true, length = 50)
    private String rollNumber;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "department", nullable = false, length = 100)
    private String department;

    @Column(name = "year_of_study", nullable = false)
    private Integer yearOfStudy;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "section", length = 10)
    private String section;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Default no-argument constructor required by JPA.
     */
    public Student() {
    }

    /**
     * Parameterized constructor for mandatory and core fields.
     */
    public Student(String rollNumber, String firstName, String lastName, String email,
                   String department, Integer yearOfStudy, Integer semester, String section) {
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.semester = semester;
        this.section = section;
        this.status = "ACTIVE";
    }

    /**
     * Full parameterized constructor.
     */
    public Student(Long id, String rollNumber, String firstName, String lastName, String email,
                   String phoneNumber, String department, Integer yearOfStudy, Integer semester,
                   String section, String gender, LocalDate dateOfBirth, String address, String status) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.semester = semester;
        this.section = section;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
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

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        if (lastName == null || lastName.isBlank()) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        Student student = (Student) o;
        return Objects.equals(rollNumber, student.rollNumber) && Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rollNumber, email);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", rollNumber='" + rollNumber + '\'' +
                ", name='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                ", semester=" + semester +
                ", section='" + section + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
