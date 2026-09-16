package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Faculty;
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
@DisplayName("FacultyRepository Tests - Day 6 Database Mapping Verification")
class FacultyRepositoryTest {

    @Autowired
    private FacultyRepository facultyRepository;

    private Faculty sampleFaculty;

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();

        sampleFaculty = new Faculty();
        sampleFaculty.setEmployeeId("FAC001");
        sampleFaculty.setFirstName("Dr. Ravi");
        sampleFaculty.setLastName("Kumar");
        sampleFaculty.setEmail("ravi.kumar@college.edu");
        sampleFaculty.setPhoneNumber("9800000001");
        sampleFaculty.setDepartment("Computer Science and Engineering");
        sampleFaculty.setDesignation("Professor");
        sampleFaculty.setSpecialization("Artificial Intelligence");
        sampleFaculty.setQualification("Ph.D Computer Science");
        sampleFaculty.setExperienceYears(15);
        sampleFaculty.setGender("Male");
        sampleFaculty.setDateOfBirth(LocalDate.of(1978, 3, 20));
        sampleFaculty.setDateOfJoining(LocalDate.of(2010, 7, 1));
        sampleFaculty.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("Should persist faculty and populate ID with audit timestamps")
    void shouldSaveAndPersistFaculty() {
        Faculty saved = facultyRepository.save(sampleFaculty);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmployeeId()).isEqualTo("FAC001");
        assertThat(saved.getFullName()).isEqualTo("Dr. Ravi Kumar");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("Should find faculty by unique employee ID")
    void shouldFindFacultyByEmployeeId() {
        facultyRepository.save(sampleFaculty);

        Optional<Faculty> found = facultyRepository.findByEmployeeId("FAC001");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("ravi.kumar@college.edu");
        assertThat(found.get().getDesignation()).isEqualTo("Professor");
    }

    @Test
    @DisplayName("Should find faculty by unique email")
    void shouldFindFacultyByEmail() {
        facultyRepository.save(sampleFaculty);

        Optional<Faculty> found = facultyRepository.findByEmail("ravi.kumar@college.edu");

        assertThat(found).isPresent();
        assertThat(found.get().getEmployeeId()).isEqualTo("FAC001");
    }

    @Test
    @DisplayName("Should query faculty by department and designation")
    void shouldFindFacultyByDepartmentAndDesignation() {
        facultyRepository.save(sampleFaculty);

        Faculty faculty2 = new Faculty("FAC002", "Dr. Meena", "Nair", "meena.nair@college.edu",
                "Computer Science and Engineering", "Associate Professor");
        facultyRepository.save(faculty2);

        Faculty faculty3 = new Faculty("FAC003", "Mr. Suresh", "Babu", "suresh.babu@college.edu",
                "Electronics and Communication", "Assistant Professor");
        facultyRepository.save(faculty3);

        List<Faculty> cseFaculty = facultyRepository.findByDepartment("Computer Science and Engineering");
        assertThat(cseFaculty).hasSize(2);

        List<Faculty> professors = facultyRepository.findByDesignation("Professor");
        assertThat(professors).hasSize(1);
        assertThat(professors.get(0).getEmployeeId()).isEqualTo("FAC001");

        List<Faculty> cseAssociates = facultyRepository.findByDepartmentAndDesignation(
                "Computer Science and Engineering", "Associate Professor");
        assertThat(cseAssociates).hasSize(1);
        assertThat(cseAssociates.get(0).getEmployeeId()).isEqualTo("FAC002");
    }

    @Test
    @DisplayName("Should verify existence by employeeId and email")
    void shouldCheckExistenceByEmployeeIdAndEmail() {
        facultyRepository.save(sampleFaculty);

        assertThat(facultyRepository.existsByEmployeeId("FAC001")).isTrue();
        assertThat(facultyRepository.existsByEmployeeId("NONEXISTENT")).isFalse();

        assertThat(facultyRepository.existsByEmail("ravi.kumar@college.edu")).isTrue();
        assertThat(facultyRepository.existsByEmail("unknown@college.edu")).isFalse();
    }

    @Test
    @DisplayName("Should find faculty by status")
    void shouldFindFacultyByStatus() {
        facultyRepository.save(sampleFaculty);

        Faculty inactive = new Faculty("FAC099", "Old", "Staff", "old.staff@college.edu",
                "Computer Science and Engineering", "Lecturer");
        inactive.setStatus("INACTIVE");
        facultyRepository.save(inactive);

        List<Faculty> active = facultyRepository.findByStatus("ACTIVE");
        List<Faculty> inactiveList = facultyRepository.findByStatus("INACTIVE");

        assertThat(active).hasSize(1);
        assertThat(inactiveList).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception for duplicate employee ID")
    void shouldEnforceUniqueEmployeeIdConstraint() {
        facultyRepository.saveAndFlush(sampleFaculty);

        Faculty duplicate = new Faculty("FAC001", "Another", "Person", "another@college.edu",
                "Computer Science and Engineering", "Lecturer");

        assertThrows(DataIntegrityViolationException.class, () -> {
            facultyRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Should throw exception for duplicate email")
    void shouldEnforceUniqueEmailConstraint() {
        facultyRepository.saveAndFlush(sampleFaculty);

        Faculty duplicate = new Faculty("FAC999", "Another", "Person", "ravi.kumar@college.edu",
                "Computer Science and Engineering", "Lecturer");

        assertThrows(DataIntegrityViolationException.class, () -> {
            facultyRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Should update and delete faculty entity")
    void shouldUpdateAndDeleteFaculty() {
        Faculty saved = facultyRepository.save(sampleFaculty);
        Long id = saved.getId();

        saved.setDesignation("Senior Professor");
        saved.setExperienceYears(18);
        Faculty updated = facultyRepository.saveAndFlush(saved);

        assertThat(updated.getDesignation()).isEqualTo("Senior Professor");
        assertThat(updated.getExperienceYears()).isEqualTo(18);

        facultyRepository.delete(updated);
        assertThat(facultyRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("Should count faculty by department")
    void shouldCountFacultyByDepartment() {
        facultyRepository.save(sampleFaculty);

        Faculty faculty2 = new Faculty("FAC002", "Dr. Meena", "Nair", "meena.nair@college.edu",
                "Computer Science and Engineering", "Associate Professor");
        facultyRepository.save(faculty2);

        long count = facultyRepository.countByDepartment("Computer Science and Engineering");
        assertThat(count).isEqualTo(2);
    }
}
