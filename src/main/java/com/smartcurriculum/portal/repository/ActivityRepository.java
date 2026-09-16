package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository interface for {@link Activity} entity.
 * Provides abstracted CRUD operations and derived custom database queries.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /**
     * Finds an activity by its unique code.
     *
     * @param activityCode the activity code (e.g. "ACT-CSE-101")
     * @return an Optional containing the Activity if found
     */
    Optional<Activity> findByActivityCode(String activityCode);

    /**
     * Finds all activities belonging to a specific department.
     *
     * @param department the department name
     * @return list of activities in that department
     */
    List<Activity> findByDepartment(String department);

    /**
     * Finds all activities by their activity type.
     *
     * @param activityType the type (e.g. "LAB", "WORKSHOP", "LECTURE")
     * @return list of matching activities
     */
    List<Activity> findByActivityType(String activityType);

    /**
     * Finds all activities by their status.
     *
     * @param status the status string (e.g. "ACTIVE", "UPCOMING")
     * @return list of matching activities
     */
    List<Activity> findByStatus(String status);

    /**
     * Finds activities by department and semester.
     *
     * @param department the department name
     * @param semester   the semester number
     * @return list of matching activities
     */
    List<Activity> findByDepartmentAndSemester(String department, Integer semester);

    /**
     * Finds activities by department and activity type.
     *
     * @param department   the department name
     * @param activityType the activity type
     * @return list of matching activities
     */
    List<Activity> findByDepartmentAndActivityType(String department, String activityType);

    /**
     * Finds all activities coordinated by a specific faculty member ID.
     *
     * @param facultyId the ID of the faculty member
     * @return list of activities coordinated by the faculty
     */
    List<Activity> findByFacultyId(Long facultyId);

    /**
     * Finds all activities coordinated by a specific faculty member's employee ID.
     *
     * @param employeeId the employee ID of the faculty
     * @return list of activities
     */
    List<Activity> findByFacultyEmployeeId(String employeeId);

    /**
     * Checks if an activity exists with the specified code.
     *
     * @param activityCode the activity code to check
     * @return true if exists, false otherwise
     */
    boolean existsByActivityCode(String activityCode);

    /**
     * Counts activities in a specific department.
     *
     * @param department the department name
     * @return total count of activities
     */
    long countByDepartment(String department);

    /**
     * Counts activities of a specific type.
     *
     * @param activityType the activity type
     * @return total count of activities
     */
    long countByActivityType(String activityType);
}
