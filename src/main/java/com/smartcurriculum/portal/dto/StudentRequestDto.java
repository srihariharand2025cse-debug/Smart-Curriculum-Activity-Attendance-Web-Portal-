package com.smartcurriculum.portal.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for incoming student registration and update requests.
 * Encapsulates the payload sent by clients to prevent direct entity manipulation.
 */
public class StudentRequestDto {

    private String rollNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String department;
    private Integer yearOfStudy;
    private Integer semester;
    private String section;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String status;

    public StudentRequestDto() {
    }

    public StudentRequestDto(String rollNumber, String firstName, String lastName, String email,
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

    public StudentRequestDto(String rollNumber, String firstName, String lastName, String email,
                             String phoneNumber, String department, Integer yearOfStudy,
                             Integer semester, String section, String gender,
                             LocalDate dateOfBirth, String address, String status) {
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

    // Getters and Setters

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

    @Override
    public String toString() {
        return "StudentRequestDto{" +
                "rollNumber='" + rollNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", yearOfStudy=" + yearOfStudy +
                ", semester=" + semester +
                ", section='" + section + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
