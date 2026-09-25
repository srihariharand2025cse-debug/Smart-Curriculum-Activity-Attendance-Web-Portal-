package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.ApiResponse;
import com.smartcurriculum.portal.dto.FacultyRequestDto;
import com.smartcurriculum.portal.dto.FacultyResponseDto;
import com.smartcurriculum.portal.service.FacultyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing CRUD operations and lookup endpoints for {@link com.smartcurriculum.portal.entity.Faculty}.
 */
@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /**
     * Create a new faculty member.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FacultyResponseDto>> createFaculty(
            @Valid @RequestBody FacultyRequestDto requestDto) {
        FacultyResponseDto createdFaculty = facultyService.createFaculty(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Faculty member registered successfully", createdFaculty));
    }

    /**
     * Retrieve all faculty members with optional filtering.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FacultyResponseDto>>> getAllFaculty(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String status) {

        List<FacultyResponseDto> facultyList;
        if (department != null && designation != null) {
            facultyList = facultyService.getFacultiesByDepartmentAndDesignation(department, designation);
        } else if (department != null) {
            facultyList = facultyService.getFacultiesByDepartment(department);
        } else if (designation != null) {
            facultyList = facultyService.getFacultiesByDesignation(designation);
        } else if (status != null) {
            facultyList = facultyService.getFacultiesByStatus(status);
        } else {
            facultyList = facultyService.getAllFaculties();
        }

        return ResponseEntity.ok(ApiResponse.success("Faculty members retrieved successfully", facultyList));
    }

    /**
     * Retrieve faculty by database ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyResponseDto>> getFacultyById(@PathVariable Long id) {
        FacultyResponseDto faculty = facultyService.getFacultyById(id);
        return ResponseEntity.ok(ApiResponse.success("Faculty member found", faculty));
    }

    /**
     * Retrieve faculty by unique Employee ID.
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<FacultyResponseDto>> getFacultyByEmployeeId(
            @PathVariable String employeeId) {
        FacultyResponseDto faculty = facultyService.getFacultyByEmployeeId(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Faculty member found", faculty));
    }

    /**
     * Update faculty details by ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacultyResponseDto>> updateFaculty(
            @PathVariable Long id,
            @Valid @RequestBody FacultyRequestDto requestDto) {
        FacultyResponseDto updated = facultyService.updateFaculty(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Faculty member updated successfully", updated));
    }

    /**
     * Delete faculty member by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok(ApiResponse.success("Faculty member deleted successfully", null));
    }
}
