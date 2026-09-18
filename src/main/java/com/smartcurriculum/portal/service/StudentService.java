package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.StudentRequestDto;
import com.smartcurriculum.portal.dto.StudentResponseDto;

import java.util.List;

/**
 * Service interface defining business operations for managing {@link com.smartcurriculum.portal.entity.Student} records.
 */
public interface StudentService {

    /**
     * Registers a new student in the portal.
     *
     * @param requestDto student data payload
     * @return registered student response DTO
     */
    StudentResponseDto createStudent(StudentRequestDto requestDto);

    /**
     * Retrieves all students.
     *
     * @return list of student response DTOs
     */
    List<StudentResponseDto> getAllStudents();

    /**
     * Retrieves a student by unique primary ID.
     *
     * @param id student database ID
     * @return student response DTO
     */
    StudentResponseDto getStudentById(Long id);

    /**
     * Retrieves a student by unique academic roll number.
     *
     * @param rollNumber unique roll number
     * @return student response DTO
     */
    StudentResponseDto getStudentByRollNumber(String rollNumber);

    /**
     * Retrieves all students belonging to a specific department.
     *
     * @param department academic department name
     * @return list of matching student response DTOs
     */
    List<StudentResponseDto> getStudentsByDepartment(String department);

    /**
     * Retrieves all students belonging to a specific department and year of study.
     *
     * @param department  academic department name
     * @param yearOfStudy year of study (1-4)
     * @return list of matching student response DTOs
     */
    List<StudentResponseDto> getStudentsByDepartmentAndYear(String department, Integer yearOfStudy);

    /**
     * Retrieves all students by active/inactive status.
     *
     * @param status status string (e.g. "ACTIVE", "INACTIVE")
     * @return list of matching student response DTOs
     */
    List<StudentResponseDto> getStudentsByStatus(String status);

    /**
     * Updates an existing student record.
     *
     * @param id         database ID of the student to update
     * @param requestDto updated student payload
     * @return updated student response DTO
     */
    StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto);

    /**
     * Permanently deletes a student by ID.
     *
     * @param id database ID of the student to delete
     */
    void deleteStudent(Long id);

    /**
     * Returns total count of registered students.
     *
     * @return student count
     */
    long getTotalStudentCount();
}
