package com.smartcurriculum.portal.service.impl;

import com.smartcurriculum.portal.dto.FacultyRequestDto;
import com.smartcurriculum.portal.dto.FacultyResponseDto;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.service.FacultyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation providing business logic, validation, and persistence
 * operations for {@link Faculty} entities.
 */
@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public FacultyResponseDto createFaculty(FacultyRequestDto requestDto) {
        validateFacultyRequest(requestDto);

        String normalizedEmpId = requestDto.getEmployeeId().trim().toUpperCase();
        if (facultyRepository.existsByEmployeeId(normalizedEmpId)) {
            throw new DuplicateResourceException("Faculty", "employeeId", normalizedEmpId);
        }

        String normalizedEmail = requestDto.getEmail().trim().toLowerCase();
        if (facultyRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("Faculty", "email", normalizedEmail);
        }

        Faculty faculty = new Faculty();
        faculty.setEmployeeId(normalizedEmpId);
        faculty.setFirstName(requestDto.getFirstName().trim());
        faculty.setLastName(requestDto.getLastName() != null ? requestDto.getLastName().trim() : null);
        faculty.setEmail(normalizedEmail);
        faculty.setPhoneNumber(requestDto.getPhoneNumber());
        faculty.setDepartment(requestDto.getDepartment().trim());
        faculty.setDesignation(requestDto.getDesignation().trim());
        faculty.setSpecialization(requestDto.getSpecialization());
        faculty.setQualification(requestDto.getQualification());
        faculty.setExperienceYears(requestDto.getExperienceYears());
        faculty.setGender(requestDto.getGender());
        faculty.setDateOfBirth(requestDto.getDateOfBirth());
        faculty.setDateOfJoining(requestDto.getDateOfJoining());
        faculty.setAddress(requestDto.getAddress());
        faculty.setStatus(requestDto.getStatus() != null && !requestDto.getStatus().isBlank()
                ? requestDto.getStatus().trim().toUpperCase() : "ACTIVE");

        Faculty savedFaculty = facultyRepository.save(faculty);
        return FacultyResponseDto.fromEntity(savedFaculty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDto> getAllFaculties() {
        return facultyRepository.findAll()
                .stream()
                .map(FacultyResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponseDto getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", id));
        return FacultyResponseDto.fromEntity(faculty);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponseDto getFacultyByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        Faculty faculty = facultyRepository.findByEmployeeId(employeeId.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "employeeId", employeeId));
        return FacultyResponseDto.fromEntity(faculty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDto> getFacultiesByDepartment(String department) {
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department name cannot be null or empty");
        }
        return facultyRepository.findByDepartment(department.trim())
                .stream()
                .map(FacultyResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDto> getFacultiesByDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation cannot be null or empty");
        }
        return facultyRepository.findByDesignation(designation.trim())
                .stream()
                .map(FacultyResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDto> getFacultiesByDepartmentAndDesignation(String department, String designation) {
        if (department == null || department.isBlank() || designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Department and designation cannot be null or empty");
        }
        return facultyRepository.findByDepartmentAndDesignation(department.trim(), designation.trim())
                .stream()
                .map(FacultyResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDto> getFacultiesByStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        return facultyRepository.findByStatus(status.trim().toUpperCase())
                .stream()
                .map(FacultyResponseDto::fromEntity)
                .toList();
    }

    @Override
    public FacultyResponseDto updateFaculty(Long id, FacultyRequestDto requestDto) {
        Faculty existingFaculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", id));

        validateFacultyRequest(requestDto);

        String normalizedEmpId = requestDto.getEmployeeId().trim().toUpperCase();
        if (!existingFaculty.getEmployeeId().equalsIgnoreCase(normalizedEmpId)
                && facultyRepository.existsByEmployeeId(normalizedEmpId)) {
            throw new DuplicateResourceException("Faculty", "employeeId", normalizedEmpId);
        }

        String normalizedEmail = requestDto.getEmail().trim().toLowerCase();
        if (!existingFaculty.getEmail().equalsIgnoreCase(normalizedEmail)
                && facultyRepository.existsByEmail(normalizedEmail)) {
            throw new DuplicateResourceException("Faculty", "email", normalizedEmail);
        }

        existingFaculty.setEmployeeId(normalizedEmpId);
        existingFaculty.setFirstName(requestDto.getFirstName().trim());
        existingFaculty.setLastName(requestDto.getLastName() != null ? requestDto.getLastName().trim() : null);
        existingFaculty.setEmail(normalizedEmail);
        existingFaculty.setPhoneNumber(requestDto.getPhoneNumber());
        existingFaculty.setDepartment(requestDto.getDepartment().trim());
        existingFaculty.setDesignation(requestDto.getDesignation().trim());
        existingFaculty.setSpecialization(requestDto.getSpecialization());
        existingFaculty.setQualification(requestDto.getQualification());
        existingFaculty.setExperienceYears(requestDto.getExperienceYears());
        existingFaculty.setGender(requestDto.getGender());
        existingFaculty.setDateOfBirth(requestDto.getDateOfBirth());
        existingFaculty.setDateOfJoining(requestDto.getDateOfJoining());
        existingFaculty.setAddress(requestDto.getAddress());
        if (requestDto.getStatus() != null && !requestDto.getStatus().isBlank()) {
            existingFaculty.setStatus(requestDto.getStatus().trim().toUpperCase());
        }

        Faculty updatedFaculty = facultyRepository.save(existingFaculty);
        return FacultyResponseDto.fromEntity(updatedFaculty);
    }

    @Override
    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", id));
        facultyRepository.delete(faculty);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalFacultyCount() {
        return facultyRepository.count();
    }

    private void validateFacultyRequest(FacultyRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Faculty request payload cannot be null");
        }
        if (dto.getEmployeeId() == null || dto.getEmployeeId().isBlank()) {
            throw new IllegalArgumentException("Employee ID is required");
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
        if (dto.getDesignation() == null || dto.getDesignation().isBlank()) {
            throw new IllegalArgumentException("Designation is required");
        }
    }
}
