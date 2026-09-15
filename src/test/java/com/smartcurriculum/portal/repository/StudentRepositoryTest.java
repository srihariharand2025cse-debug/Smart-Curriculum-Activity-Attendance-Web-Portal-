package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("StudentRepository Tests - Day 5 Database Mapping Verification")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student sampleStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();

        sampleStudent = new Student();
        sampleStudent.setRollNumber("21CSE001");
        sampleStudent.setFirstName("Aarav");
        sampleStudent.setLastName("Sharma");
        sampleStudent.setEmail("aarav.sharma@college.edu");
        sampleStudent.setPhoneNumber("9876543210");
        sampleStudent.setDepartment("Computer Science and Engineering");
        sampleStudent.setYearOfStudy(3);
        sampleStudent.setSemester(5);
        sampleStudent.setSection("A");
        sampleStudent.setGender("Male");
        sampleStudent.setDateOfBirth(LocalDate.of(2003, 5, 14));
        sampleStudent.setAddress("123 Tech Campus Road, Coimbatore");
        sampleStudent.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("Should persist student and populate ID with audit timestamps")
    void shouldSaveAndPersistStudent() {
        Student saved = studentRepository.save(sampleStudent);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getRollNumber()).isEqualTo("21CSE001");
        assertThat(saved.getFullName()).isEqualTo("Aarav Sharma");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("Should find student by unique roll number")
    void shouldFindStudentByRollNumber() {
        studentRepository.save(sampleStudent);

        Optional<Student> found = studentRepository.findByRollNumber("21CSE001");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("aarav.sharma@college.edu");
        assertThat(found.get().getDepartment()).isEqualTo("Computer Science and Engineering");
    }

    @Test
    @DisplayName("Should find student by unique email")
    void shouldFindStudentByEmail() {
        studentRepository.save(sampleStudent);

        Optional<Student> found = studentRepository.findByEmail("aarav.sharma@college.edu");

        assertThat(found).isPresent();
        assertThat(found.get().getRollNumber()).isEqualTo("21CSE001");
    }

    @Test
    @DisplayName("Should query students by department and year")
    void shouldFindStudentsByDepartmentAndYear() {
        studentRepository.save(sampleStudent);

        Student student2 = new Student("21CSE002", "Diya", "Patel", "diya.patel@college.edu",
                "Computer Science and Engineering", 3, 5, "A");
        studentRepository.save(student2);

        Student student3 = new Student("22ECE001", "Rohan", "Verma", "rohan.verma@college.edu",
                "Electronics and Communication", 2, 3, "B");
        studentRepository.save(student3);

        List<Student> cseStudents = studentRepository.findByDepartment("Computer Science and Engineering");
        assertThat(cseStudents).hasSize(2);

        List<Student> year3Students = studentRepository.findByDepartmentAndYearOfStudy("Computer Science and Engineering", 3);
        assertThat(year3Students).hasSize(2);

        List<Student> sectionAStudents = studentRepository.findByDepartmentAndYearOfStudyAndSection(
                "Computer Science and Engineering", 3, "A"
        );
        assertThat(sectionAStudents).hasSize(2);
    }

    @Test
    @DisplayName("Should verify existence by rollNumber and email")
    void shouldCheckExistenceByRollNumberAndEmail() {
        studentRepository.save(sampleStudent);

        assertThat(studentRepository.existsByRollNumber("21CSE001")).isTrue();
        assertThat(studentRepository.existsByRollNumber("NONEXISTENT")).isFalse();

        assertThat(studentRepository.existsByEmail("aarav.sharma@college.edu")).isTrue();
        assertThat(studentRepository.existsByEmail("unknown@college.edu")).isFalse();
    }

    @Test
    @DisplayName("Should throw exception when duplicate roll number is saved")
    void shouldEnforceUniqueRollNumberConstraint() {
        studentRepository.saveAndFlush(sampleStudent);

        Student duplicate = new Student("21CSE001", "Another", "Student", "another@college.edu",
                "Computer Science and Engineering", 3, 5, "A");

        assertThrows(DataIntegrityViolationException.class, () -> {
            studentRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Should throw exception when duplicate email is saved")
    void shouldEnforceUniqueEmailConstraint() {
        studentRepository.saveAndFlush(sampleStudent);

        Student duplicate = new Student("21CSE999", "Another", "Student", "aarav.sharma@college.edu",
                "Computer Science and Engineering", 3, 5, "A");

        assertThrows(DataIntegrityViolationException.class, () -> {
            studentRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Should update and delete student entity")
    void shouldUpdateAndDeleteStudent() {
        Student saved = studentRepository.save(sampleStudent);
        Long id = saved.getId();

        saved.setSection("B");
        saved.setPhoneNumber("9999999999");
        Student updated = studentRepository.saveAndFlush(saved);

        assertThat(updated.getSection()).isEqualTo("B");
        assertThat(updated.getPhoneNumber()).isEqualTo("9999999999");

        studentRepository.delete(updated);
        assertThat(studentRepository.findById(id)).isEmpty();
    }
}
