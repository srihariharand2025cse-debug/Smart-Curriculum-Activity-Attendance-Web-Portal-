package com.smartcurriculum.portal.dto;

import com.smartcurriculum.portal.entity.Faculty;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning Faculty details in API responses.
 */
public class FacultyResponseDto {

    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String department;
    private String designation;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private String address;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FacultyResponseDto() {
    }

    public static FacultyResponseDto fromEntity(Faculty faculty) {
        if (faculty == null) return null;
        FacultyResponseDto dto = new FacultyResponseDto();
        dto.setId(faculty.getId());
        dto.setEmployeeId(faculty.getEmployeeId());
        dto.setFirstName(faculty.getFirstName());
        dto.setLastName(faculty.getLastName());
        dto.setFullName(faculty.getFullName());
        dto.setEmail(faculty.getEmail());
        dto.setPhoneNumber(faculty.getPhoneNumber());
        dto.setDepartment(faculty.getDepartment());
        dto.setDesignation(faculty.getDesignation());
        dto.setSpecialization(faculty.getSpecialization());
        dto.setQualification(faculty.getQualification());
        dto.setExperienceYears(faculty.getExperienceYears());
        dto.setGender(faculty.getGender());
        dto.setDateOfBirth(faculty.getDateOfBirth());
        dto.setDateOfJoining(faculty.getDateOfJoining());
        dto.setAddress(faculty.getAddress());
        dto.setStatus(faculty.getStatus());
        dto.setCreatedAt(faculty.getCreatedAt());
        dto.setUpdatedAt(faculty.getUpdatedAt());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
