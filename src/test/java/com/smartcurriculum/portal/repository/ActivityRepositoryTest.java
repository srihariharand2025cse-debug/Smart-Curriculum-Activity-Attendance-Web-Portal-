package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Activity;
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
@DisplayName("ActivityRepository Tests - Day 7 Database Mapping Verification")
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Faculty sampleFaculty;
    private Activity sampleActivity;

    @BeforeEach
    void setUp() {
        activityRepository.deleteAll();
        facultyRepository.deleteAll();

        // Prepare sample faculty coordinator
        sampleFaculty = new Faculty();
        sampleFaculty.setEmployeeId("FAC001");
        sampleFaculty.setFirstName("Dr. Ravi");
        sampleFaculty.setLastName("Kumar");
        sampleFaculty.setEmail("ravi.kumar@college.edu");
        sampleFaculty.setDepartment("Computer Science and Engineering");
        sampleFaculty.setDesignation("Professor");
        sampleFaculty.setStatus("ACTIVE");
        sampleFaculty = facultyRepository.save(sampleFaculty);

        // Prepare sample activity
        sampleActivity = new Activity();
        sampleActivity.setActivityCode("ACT-CS501-LAB");
        sampleActivity.setTitle("Data Structures & Algorithms Laboratory");
        sampleActivity.setDescription("Practical programming laboratory for tree and graph traversal.");
        sampleActivity.setActivityType("LAB");
        sampleActivity.setDepartment("Computer Science and Engineering");
        sampleActivity.setAcademicYear("2025-2026");
        sampleActivity.setSemester(5);
        sampleActivity.setCredits(2);
        sampleActivity.setVenue("Computer Lab 3");
        sampleActivity.setFaculty(sampleFaculty);
        sampleActivity.setStartDate(LocalDate.of(2025, 8, 1));
        sampleActivity.setEndDate(LocalDate.of(2025, 12, 1));
        sampleActivity.setMaxEnrollment(60);
        sampleActivity.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("Should persist activity and populate ID with audit timestamps and faculty relation")
    void shouldSaveAndPersistActivity() {
        Activity saved = activityRepository.save(sampleActivity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getActivityCode()).isEqualTo("ACT-CS501-LAB");
        assertThat(saved.getTitle()).isEqualTo("Data Structures & Algorithms Laboratory");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getFaculty()).isNotNull();
        assertThat(saved.getFaculty().getEmployeeId()).isEqualTo("FAC001");
        assertThat(saved.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    @DisplayName("Should find activity by unique activity code")
    void shouldFindActivityByActivityCode() {
        activityRepository.save(sampleActivity);

        Optional<Activity> found = activityRepository.findByActivityCode("ACT-CS501-LAB");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Data Structures & Algorithms Laboratory");
        assertThat(found.get().getActivityType()).isEqualTo("LAB");
    }

    @Test
    @DisplayName("Should find activities by department")
    void shouldFindActivitiesByDepartment() {
        activityRepository.save(sampleActivity);

        Activity secondActivity = new Activity();
        secondActivity.setActivityCode("ACT-CS502-LEC");
        secondActivity.setTitle("Artificial Intelligence");
        secondActivity.setActivityType("LECTURE");
        secondActivity.setDepartment("Computer Science and Engineering");
        secondActivity.setSemester(5);
        secondActivity.setCredits(4);
        activityRepository.save(secondActivity);

        List<Activity> cseActivities = activityRepository.findByDepartment("Computer Science and Engineering");

        assertThat(cseActivities).hasSize(2);
    }

    @Test
    @DisplayName("Should find activities by activity type")
    void shouldFindActivitiesByActivityType() {
        activityRepository.save(sampleActivity);

        Activity workshop = new Activity();
        workshop.setActivityCode("ACT-CS503-WRK");
        workshop.setTitle("Full Stack Cloud Workshop");
        workshop.setActivityType("WORKSHOP");
        workshop.setDepartment("Computer Science and Engineering");
        workshop.setSemester(5);
        workshop.setCredits(1);
        activityRepository.save(workshop);

        List<Activity> labs = activityRepository.findByActivityType("LAB");
        List<Activity> workshops = activityRepository.findByActivityType("WORKSHOP");

        assertThat(labs).hasSize(1);
        assertThat(labs.get(0).getActivityCode()).isEqualTo("ACT-CS501-LAB");
        assertThat(workshops).hasSize(1);
        assertThat(workshops.get(0).getActivityCode()).isEqualTo("ACT-CS503-WRK");
    }

    @Test
    @DisplayName("Should find activities by department and semester")
    void shouldFindActivitiesByDepartmentAndSemester() {
        activityRepository.save(sampleActivity);

        Activity sem7Activity = new Activity();
        sem7Activity.setActivityCode("ACT-CS701-PRJ");
        sem7Activity.setTitle("Capstone Project");
        sem7Activity.setActivityType("PROJECT");
        sem7Activity.setDepartment("Computer Science and Engineering");
        sem7Activity.setSemester(7);
        sem7Activity.setCredits(6);
        activityRepository.save(sem7Activity);

        List<Activity> sem5List = activityRepository.findByDepartmentAndSemester("Computer Science and Engineering", 5);
        List<Activity> sem7List = activityRepository.findByDepartmentAndSemester("Computer Science and Engineering", 7);

        assertThat(sem5List).hasSize(1);
        assertThat(sem7List).hasSize(1);
        assertThat(sem7List.get(0).getActivityCode()).isEqualTo("ACT-CS701-PRJ");
    }

    @Test
    @DisplayName("Should find activities by faculty coordinator employee ID")
    void shouldFindActivitiesByFacultyEmployeeId() {
        activityRepository.save(sampleActivity);

        List<Activity> activities = activityRepository.findByFacultyEmployeeId("FAC001");

        assertThat(activities).hasSize(1);
        assertThat(activities.get(0).getTitle()).isEqualTo("Data Structures & Algorithms Laboratory");
        assertThat(activities.get(0).getFaculty().getFullName()).isEqualTo("Dr. Ravi Kumar");
    }

    @Test
    @DisplayName("Should throw DataIntegrityViolationException on duplicate activity code")
    void shouldEnforceUniqueActivityCodeConstraint() {
        activityRepository.saveAndFlush(sampleActivity);

        Activity duplicate = new Activity();
        duplicate.setActivityCode("ACT-CS501-LAB"); // Same unique code
        duplicate.setTitle("Another Lab Title");
        duplicate.setActivityType("LAB");
        duplicate.setDepartment("Computer Science and Engineering");
        duplicate.setSemester(5);
        duplicate.setCredits(2);

        assertThrows(DataIntegrityViolationException.class, () -> {
            activityRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @DisplayName("Should update activity details and status correctly")
    void shouldUpdateActivityDetails() {
        Activity saved = activityRepository.save(sampleActivity);

        saved.setStatus("COMPLETED");
        saved.setVenue("Auditorium Hall A");
        Activity updated = activityRepository.saveAndFlush(saved);

        assertThat(updated.getStatus()).isEqualTo("COMPLETED");
        assertThat(updated.getVenue()).isEqualTo("Auditorium Hall A");
    }

    @Test
    @DisplayName("Should count activities by department and activity type")
    void shouldCountActivities() {
        activityRepository.save(sampleActivity);

        long cseCount = activityRepository.countByDepartment("Computer Science and Engineering");
        long labCount = activityRepository.countByActivityType("LAB");
        long nonExistentCount = activityRepository.countByDepartment("Civil Engineering");

        assertThat(cseCount).isEqualTo(1);
        assertThat(labCount).isEqualTo(1);
        assertThat(nonExistentCount).isEqualTo(0);
        assertThat(activityRepository.existsByActivityCode("ACT-CS501-LAB")).isTrue();
        assertThat(activityRepository.existsByActivityCode("UNKNOWN")).isFalse();
    }
}
