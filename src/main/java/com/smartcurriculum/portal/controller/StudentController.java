package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.ApiResponse;
import com.smartcurriculum.portal.dto.StudentRequestDto;
import com.smartcurriculum.portal.dto.StudentResponseDto;
import com.smartcurriculum.portal.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller exposing CRUD and search endpoints for Student resources.
 * Base route: /api/students
 */
@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Creates / registers a new student record.
     * POST /api/students
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(@RequestBody StudentRequestDto requestDto) {
        StudentResponseDto created = studentService.createStudent(requestDto);
        return new ResponseEntity<>(ApiResponse.success("Student registered successfully", created), HttpStatus.CREATED);
    }

    /**
     * Retrieves all students or filters them by query parameters.
     * GET /api/students
     * GET /api/students?department=Computer%20Science
     * GET /api/students?department=Computer%20Science&yearOfStudy=3
     * GET /api/students?status=ACTIVE
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer yearOfStudy,
            @RequestParam(required = false) String status) {

        List<StudentResponseDto> students;

        if (department != null && !department.isBlank() && yearOfStudy != null) {
            students = studentService.getStudentsByDepartmentAndYear(department, yearOfStudy);
        } else if (department != null && !department.isBlank()) {
            students = studentService.getStudentsByDepartment(department);
        } else if (status != null && !status.isBlank()) {
            students = studentService.getStudentsByStatus(status);
        } else {
            students = studentService.getAllStudents();
        }

        return ResponseEntity.ok(ApiResponse.success("Students retrieved successfully", students));
    }

    /**
     * Retrieves a student by database primary ID.
     * GET /api/students/{id}
     */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable Long id) {
        StudentResponseDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    /**
     * Retrieves a student by unique institutional roll number.
     * GET /api/students/roll/{rollNumber}
     */
    @GetMapping("/roll/{rollNumber}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentByRollNumber(@PathVariable String rollNumber) {
        StudentResponseDto student = studentService.getStudentByRollNumber(rollNumber);
        return ResponseEntity.ok(ApiResponse.success("Student retrieved successfully", student));
    }

    /**
     * Updates an existing student by ID.
     * PUT /api/students/{id}
     */
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto updated = studentService.updateStudent(id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", updated));
    }

    /**
     * Deletes a student by ID.
     * DELETE /api/students/{id}
     */
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Student deleted successfully", null));
    }
}
