package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.FacultyRequestDto;
import com.smartcurriculum.portal.dto.FacultyResponseDto;
import java.util.List;

/**
 * Service interface defining CRUD operations for Faculty entities.
 */
public interface FacultyService {

    /**
     * Creates a new faculty record.
     */
    FacultyResponseDto createFaculty(FacultyRequestDto requestDto);

    /**
     * Retrieves all faculty records.
     */
    List<FacultyResponseDto> getAllFaculties();

    /**
     * Retrieves a faculty by its primary database ID.
     */
    FacultyResponseDto getFacultyById(Long id);

    /**
     * Retrieves a faculty by its unique employee ID.
     */
    FacultyResponseDto getFacultyByEmployeeId(String employeeId);

    /**
     * Retrieves all faculty members belonging to a specific department.
     */
    List<FacultyResponseDto> getFacultiesByDepartment(String department);

    /**
     * Retrieves all faculty members with a specific designation.
     */
    List<FacultyResponseDto> getFacultiesByDesignation(String designation);

    /**
     * Retrieves faculty members by department and designation.
     */
    List<FacultyResponseDto> getFacultiesByDepartmentAndDesignation(String department, String designation);

    /**
     * Retrieves faculty members by active/inactive status.
     */
    List<FacultyResponseDto> getFacultiesByStatus(String status);

    /**
     * Updates an existing faculty record.
     */
    FacultyResponseDto updateFaculty(Long id, FacultyRequestDto requestDto);

    /**
     * Deletes a faculty record.
     */
    void deleteFaculty(Long id);

    /**
     * Returns total count of faculty records.
     */
    long getTotalFacultyCount();
}
