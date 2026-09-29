package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.FacultyRequestDto;
import com.smartcurriculum.portal.dto.FacultyResponseDto;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.service.impl.FacultyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FacultyServiceImpl — Day 18 end-to-end testing.
 */
@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyServiceImpl facultyService;

    private Faculty faculty;
    private FacultyRequestDto requestDto;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setEmployeeId("FAC001");
        faculty.setFirstName("John");
        faculty.setLastName("Smith");
        faculty.setEmail("john.smith@college.edu");
        faculty.setPhoneNumber("9876543210");
        faculty.setDepartment("Computer Science and Engineering");
        faculty.setDesignation("Associate Professor");
        faculty.setSpecialization("Machine Learning");
        faculty.setQualification("Ph.D.");
        faculty.setExperienceYears(10);
        faculty.setGender("MALE");
        faculty.setDateOfBirth(LocalDate.of(1985, 3, 15));
        faculty.setDateOfJoining(LocalDate.of(2015, 7, 1));
        faculty.setStatus("ACTIVE");

        requestDto = new FacultyRequestDto();
        requestDto.setEmployeeId("FAC001");
        requestDto.setFirstName("John");
        requestDto.setLastName("Smith");
        requestDto.setEmail("john.smith@college.edu");
        requestDto.setPhoneNumber("9876543210");
        requestDto.setDepartment("Computer Science and Engineering");
        requestDto.setDesignation("Associate Professor");
        requestDto.setSpecialization("Machine Learning");
        requestDto.setQualification("Ph.D.");
        requestDto.setExperienceYears(10);
        requestDto.setGender("MALE");
        requestDto.setDateOfBirth(LocalDate.of(1985, 3, 15));
        requestDto.setDateOfJoining(LocalDate.of(2015, 7, 1));
        requestDto.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("Should successfully create a new faculty member")
    void shouldCreateFacultySuccessfully() {
        when(facultyRepository.existsByEmployeeId(anyString())).thenReturn(false);
        when(facultyRepository.existsByEmail(anyString())).thenReturn(false);
        when(facultyRepository.save(any(Faculty.class))).thenAnswer(inv -> {
            Faculty f = inv.getArgument(0);
            f.setId(1L);
            return f;
        });

        FacultyResponseDto response = facultyService.createFaculty(requestDto);

        assertNotNull(response);
        assertEquals("FAC001", response.getEmployeeId());
        assertEquals("John", response.getFirstName());
        assertEquals("john.smith@college.edu", response.getEmail());
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when employee ID already exists")
    void shouldThrowDuplicate_WhenEmployeeIdExists() {
        when(facultyRepository.existsByEmployeeId("FAC001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> facultyService.createFaculty(requestDto));
        verify(facultyRepository, never()).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void shouldThrowDuplicate_WhenEmailExists() {
        when(facultyRepository.existsByEmployeeId(anyString())).thenReturn(false);
        when(facultyRepository.existsByEmail("john.smith@college.edu")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> facultyService.createFaculty(requestDto));
        verify(facultyRepository, never()).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when mandatory fields are blank")
    void shouldThrowException_WhenMandatoryFieldsMissing() {
        requestDto.setEmployeeId("");
        assertThrows(IllegalArgumentException.class, () -> facultyService.createFaculty(requestDto));

        requestDto.setEmployeeId("FAC001");
        requestDto.setFirstName(null);
        assertThrows(IllegalArgumentException.class, () -> facultyService.createFaculty(requestDto));

        requestDto.setFirstName("John");
        requestDto.setEmail("  ");
        assertThrows(IllegalArgumentException.class, () -> facultyService.createFaculty(requestDto));

        requestDto.setEmail("john@college.edu");
        requestDto.setDepartment(null);
        assertThrows(IllegalArgumentException.class, () -> facultyService.createFaculty(requestDto));

        requestDto.setDepartment("CSE");
        requestDto.setDesignation(null);
        assertThrows(IllegalArgumentException.class, () -> facultyService.createFaculty(requestDto));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when request DTO is null")
    void shouldThrowException_WhenRequestIsNull() {
        assertThrows(IllegalArgumentException.class, () -> facultyService.createFaculty(null));
    }

    @Test
    @DisplayName("Should return all faculty members")
    void shouldReturnAllFaculties() {
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));

        List<FacultyResponseDto> result = facultyService.getAllFaculties();

        assertEquals(1, result.size());
        assertEquals("FAC001", result.get(0).getEmployeeId());
        verify(facultyRepository).findAll();
    }

    @Test
    @DisplayName("Should retrieve faculty by ID")
    void shouldReturnFacultyById() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        FacultyResponseDto result = facultyService.getFacultyById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Smith", result.getFullName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when faculty ID not found")
    void shouldThrowException_WhenFacultyIdNotFound() {
        when(facultyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.getFacultyById(99L));
    }

    @Test
    @DisplayName("Should retrieve faculty by employee ID")
    void shouldReturnFacultyByEmployeeId() {
        when(facultyRepository.findByEmployeeId("FAC001")).thenReturn(Optional.of(faculty));

        FacultyResponseDto result = facultyService.getFacultyByEmployeeId("fac001");

        assertNotNull(result);
        assertEquals("FAC001", result.getEmployeeId());
    }

    @Test
    @DisplayName("Should throw exception when employee ID is blank")
    void shouldThrowException_WhenEmployeeIdBlank() {
        assertThrows(IllegalArgumentException.class, () -> facultyService.getFacultyByEmployeeId(""));
        assertThrows(IllegalArgumentException.class, () -> facultyService.getFacultyByEmployeeId(null));
    }

    @Test
    @DisplayName("Should retrieve faculty by department")
    void shouldReturnFacultiesByDepartment() {
        when(facultyRepository.findByDepartment("Computer Science and Engineering")).thenReturn(List.of(faculty));

        List<FacultyResponseDto> result = facultyService.getFacultiesByDepartment("Computer Science and Engineering");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should retrieve faculty by designation")
    void shouldReturnFacultiesByDesignation() {
        when(facultyRepository.findByDesignation("Associate Professor")).thenReturn(List.of(faculty));

        List<FacultyResponseDto> result = facultyService.getFacultiesByDesignation("Associate Professor");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should retrieve faculty by department and designation")
    void shouldReturnFacultiesByDepartmentAndDesignation() {
        when(facultyRepository.findByDepartmentAndDesignation("Computer Science and Engineering", "Associate Professor"))
                .thenReturn(List.of(faculty));

        List<FacultyResponseDto> result = facultyService.getFacultiesByDepartmentAndDesignation(
                "Computer Science and Engineering", "Associate Professor");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should retrieve faculty by status")
    void shouldReturnFacultiesByStatus() {
        when(facultyRepository.findByStatus("ACTIVE")).thenReturn(List.of(faculty));

        List<FacultyResponseDto> result = facultyService.getFacultiesByStatus("active");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should update faculty successfully")
    void shouldUpdateFacultySuccessfully() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        requestDto.setFirstName("Jonathan");
        FacultyResponseDto updated = facultyService.updateFaculty(1L, requestDto);

        assertNotNull(updated);
        assertEquals("Jonathan", updated.getFirstName());
        verify(facultyRepository).save(faculty);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on update when employee ID taken by another")
    void shouldThrowDuplicate_OnUpdate_WhenEmployeeIdTaken() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        requestDto.setEmployeeId("FAC999");
        when(facultyRepository.existsByEmployeeId("FAC999")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> facultyService.updateFaculty(1L, requestDto));
        verify(facultyRepository, never()).save(any(Faculty.class));
    }

    @Test
    @DisplayName("Should delete faculty successfully")
    void shouldDeleteFacultySuccessfully() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        facultyService.deleteFaculty(1L);

        verify(facultyRepository).delete(faculty);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException on delete when not found")
    void shouldThrowException_OnDelete_WhenNotFound() {
        when(facultyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facultyService.deleteFaculty(99L));
    }

    @Test
    @DisplayName("Should return total faculty count")
    void shouldReturnTotalFacultyCount() {
        when(facultyRepository.count()).thenReturn(15L);

        long count = facultyService.getTotalFacultyCount();

        assertEquals(15L, count);
        verify(facultyRepository).count();
    }
}
