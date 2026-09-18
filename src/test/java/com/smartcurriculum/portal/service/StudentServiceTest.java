package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.StudentRequestDto;
import com.smartcurriculum.portal.dto.StudentResponseDto;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.StudentRepository;
import com.smartcurriculum.portal.service.impl.StudentServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentRequestDto requestDto;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setRollNumber("21CSE001");
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setEmail("alice.j@college.edu");
        student.setPhoneNumber("9876543210");
        student.setDepartment("Computer Science and Engineering");
        student.setYearOfStudy(3);
        student.setSemester(5);
        student.setSection("A");
        student.setGender("FEMALE");
        student.setDateOfBirth(LocalDate.of(2003, 5, 14));
        student.setStatus("ACTIVE");

        requestDto = new StudentRequestDto(
                "21CSE001",
                "Alice",
                "Johnson",
                "alice.j@college.edu",
                "9876543210",
                "Computer Science and Engineering",
                3,
                5,
                "A",
                "FEMALE",
                LocalDate.of(2003, 5, 14),
                "123 Tech Park Avenue",
                "ACTIVE"
        );
    }

    @Test
    @DisplayName("Should successfully register a new student")
    void shouldCreateStudentSuccessfully() {
        when(studentRepository.existsByRollNumber(anyString())).thenReturn(false);
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        StudentResponseDto response = studentService.createStudent(requestDto);

        assertNotNull(response);
        assertEquals("21CSE001", response.getRollNumber());
        assertEquals("Alice", response.getFirstName());
        assertEquals("Alice Johnson", response.getFullName());
        assertEquals("alice.j@college.edu", response.getEmail());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when roll number already exists")
    void shouldThrowDuplicateException_WhenRollNumberExists() {
        when(studentRepository.existsByRollNumber("21CSE001")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.createStudent(requestDto));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void shouldThrowDuplicateException_WhenEmailExists() {
        when(studentRepository.existsByRollNumber(anyString())).thenReturn(false);
        when(studentRepository.existsByEmail("alice.j@college.edu")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.createStudent(requestDto));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when required fields are blank")
    void shouldThrowException_WhenMandatoryFieldsAreMissing() {
        requestDto.setRollNumber("");
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(requestDto));

        requestDto.setRollNumber("21CSE001");
        requestDto.setFirstName("   ");
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(requestDto));

        requestDto.setFirstName("Alice");
        requestDto.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(requestDto));
    }

    @Test
    @DisplayName("Should return all students")
    void shouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentResponseDto> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        assertEquals("21CSE001", result.get(0).getRollNumber());
        verify(studentRepository).findAll();
    }

    @Test
    @DisplayName("Should retrieve student by ID successfully")
    void shouldReturnStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponseDto result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice Johnson", result.getFullName());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student ID not found")
    void shouldThrowException_WhenStudentIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }

    @Test
    @DisplayName("Should retrieve student by roll number")
    void shouldReturnStudentByRollNumber() {
        when(studentRepository.findByRollNumber("21CSE001")).thenReturn(Optional.of(student));

        StudentResponseDto result = studentService.getStudentByRollNumber("21cse001");

        assertNotNull(result);
        assertEquals("21CSE001", result.getRollNumber());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when roll number not found")
    void shouldThrowException_WhenRollNumberNotFound() {
        when(studentRepository.findByRollNumber("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentByRollNumber("UNKNOWN"));
    }

    @Test
    @DisplayName("Should retrieve students by department")
    void shouldReturnStudentsByDepartment() {
        when(studentRepository.findByDepartment("Computer Science and Engineering")).thenReturn(List.of(student));

        List<StudentResponseDto> result = studentService.getStudentsByDepartment("Computer Science and Engineering");

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
    }

    @Test
    @DisplayName("Should retrieve students by department and year")
    void shouldReturnStudentsByDepartmentAndYear() {
        when(studentRepository.findByDepartmentAndYearOfStudy("Computer Science and Engineering", 3))
                .thenReturn(List.of(student));

        List<StudentResponseDto> result = studentService.getStudentsByDepartmentAndYear("Computer Science and Engineering", 3);

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getYearOfStudy());
    }

    @Test
    @DisplayName("Should retrieve students by status")
    void shouldReturnStudentsByStatus() {
        when(studentRepository.findByStatus("ACTIVE")).thenReturn(List.of(student));

        List<StudentResponseDto> result = studentService.getStudentsByStatus("active");

        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    @DisplayName("Should successfully update an existing student")
    void shouldUpdateStudentSuccessfully() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        requestDto.setFirstName("Alicia");
        requestDto.setLastName("Wonderland");

        StudentResponseDto updated = studentService.updateStudent(1L, requestDto);

        assertNotNull(updated);
        assertEquals("Alicia", updated.getFirstName());
        verify(studentRepository).save(student);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on update if roll number taken by another")
    void shouldThrowDuplicateException_OnUpdate_WhenRollNumberTaken() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        requestDto.setRollNumber("21CSE999");
        when(studentRepository.existsByRollNumber("21CSE999")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> studentService.updateStudent(1L, requestDto));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should delete student successfully")
    void shouldDeleteStudentSuccessfully() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(1L);

        verify(studentRepository).delete(student);
    }

    @Test
    @DisplayName("Should return total student count")
    void shouldReturnTotalStudentCount() {
        when(studentRepository.count()).thenReturn(25L);

        long count = studentService.getTotalStudentCount();

        assertEquals(25L, count);
        verify(studentRepository).count();
    }
}
