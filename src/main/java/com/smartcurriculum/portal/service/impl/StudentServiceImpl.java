package com.smartcurriculum.portal.service.impl;

import com.smartcurriculum.portal.dto.StudentRequestDto;
import com.smartcurriculum.portal.dto.StudentResponseDto;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.StudentRepository;
import com.smartcurriculum.portal.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation providing business logic, validation, and persistence
 * operations for {@link Student} entities.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDto createStudent(StudentRequestDto requestDto) {
        validateStudentRequest(requestDto);

        if (studentRepository.existsByRollNumber(requestDto.getRollNumber())) {
            throw new DuplicateResourceException("Student", "rollNumber", requestDto.getRollNumber());
        }

        if (studentRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Student", "email", requestDto.getEmail());
        }

        Student student = new Student();
        student.setRollNumber(requestDto.getRollNumber().trim().toUpperCase());
        student.setFirstName(requestDto.getFirstName().trim());
        student.setLastName(requestDto.getLastName() != null ? requestDto.getLastName().trim() : null);
        student.setEmail(requestDto.getEmail().trim().toLowerCase());
        student.setPhoneNumber(requestDto.getPhoneNumber());
        student.setDepartment(requestDto.getDepartment().trim());
        student.setYearOfStudy(requestDto.getYearOfStudy());
        student.setSemester(requestDto.getSemester());
        student.setSection(requestDto.getSection());
        student.setGender(requestDto.getGender());
        student.setDateOfBirth(requestDto.getDateOfBirth());
        student.setAddress(requestDto.getAddress());
        student.setStatus(requestDto.getStatus() != null && !requestDto.getStatus().isBlank()
                ? requestDto.getStatus().trim().toUpperCase() : "ACTIVE");

        Student savedStudent = studentRepository.save(student);
        return StudentResponseDto.fromEntity(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return StudentResponseDto.fromEntity(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentByRollNumber(String rollNumber) {
        if (rollNumber == null || rollNumber.isBlank()) {
            throw new IllegalArgumentException("Roll number cannot be null or empty");
        }
        Student student = studentRepository.findByRollNumber(rollNumber.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "rollNumber", rollNumber));
        return StudentResponseDto.fromEntity(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getStudentsByDepartment(String department) {
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        return studentRepository.findByDepartment(department.trim())
                .stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getStudentsByDepartmentAndYear(String department, Integer yearOfStudy) {
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        if (yearOfStudy == null || yearOfStudy < 1 || yearOfStudy > 5) {
            throw new IllegalArgumentException("Year of study must be between 1 and 5");
        }
        return studentRepository.findByDepartmentAndYearOfStudy(department.trim(), yearOfStudy)
                .stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getStudentsByStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        return studentRepository.findByStatus(status.trim().toUpperCase())
                .stream()
                .map(StudentResponseDto::fromEntity)
                .toList();
    }

    @Override
    public StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        validateStudentRequest(requestDto);

        String normalizedRollNumber = requestDto.getRollNumber().trim().toUpperCase();
        if (!existingStudent.getRollNumber().equalsIgnoreCase(normalizedRollNumber)
                && studentRepository.existsByRollNumber(normalizedRollNumber)) {
            throw new DuplicateResourceException("Student", "rollNumber", normalizedRollNumber);
        }

        String normalizedEmail = requestDto.getEmail().trim().toLowerCase();
        if (!existingStudent.getEmail().equalsIgnoreCase(normalizedEmail)
                && studentRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("Student", "email", normalizedEmail);
        }

        existingStudent.setRollNumber(normalizedRollNumber);
        existingStudent.setFirstName(requestDto.getFirstName().trim());
        existingStudent.setLastName(requestDto.getLastName() != null ? requestDto.getLastName().trim() : null);
        existingStudent.setEmail(normalizedEmail);
        existingStudent.setPhoneNumber(requestDto.getPhoneNumber());
        existingStudent.setDepartment(requestDto.getDepartment().trim());
        existingStudent.setYearOfStudy(requestDto.getYearOfStudy());
        existingStudent.setSemester(requestDto.getSemester());
        existingStudent.setSection(requestDto.getSection());
        existingStudent.setGender(requestDto.getGender());
        existingStudent.setDateOfBirth(requestDto.getDateOfBirth());
        existingStudent.setAddress(requestDto.getAddress());
        if (requestDto.getStatus() != null && !requestDto.getStatus().isBlank()) {
            existingStudent.setStatus(requestDto.getStatus().trim().toUpperCase());
        }

        Student updatedStudent = studentRepository.save(existingStudent);
        return StudentResponseDto.fromEntity(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        studentRepository.delete(student);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    /**
     * Validates required student fields before saving or updating.
     */
    private void validateStudentRequest(StudentRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Student request payload cannot be null");
        }
        if (dto.getRollNumber() == null || dto.getRollNumber().isBlank()) {
            throw new IllegalArgumentException("Roll number is required");
        }
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email address is required");
        }
        if (dto.getDepartment() == null || dto.getDepartment().isBlank()) {
            throw new IllegalArgumentException("Department is required");
        }
        if (dto.getYearOfStudy() == null || dto.getYearOfStudy() < 1 || dto.getYearOfStudy() > 5) {
            throw new IllegalArgumentException("Valid year of study (1 to 5) is required");
        }
        if (dto.getSemester() == null || dto.getSemester() < 1 || dto.getSemester() > 10) {
            throw new IllegalArgumentException("Valid semester (1 to 10) is required");
        }
    }
}
