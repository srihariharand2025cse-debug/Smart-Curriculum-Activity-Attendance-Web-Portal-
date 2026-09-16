package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository interface for {@link Faculty} entity.
 * Provides abstracted CRUD operations and derived custom database queries.
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    /**
     * Finds a faculty member by their unique employee ID.
     *
     * @param employeeId the institutional employee ID
     * @return an Optional containing the faculty if found
     */
    Optional<Faculty> findByEmployeeId(String employeeId);

    /**
     * Finds a faculty member by their institutional email address.
     *
     * @param email the faculty's email address
     * @return an Optional containing the faculty if found
     */
    Optional<Faculty> findByEmail(String email);

    /**
     * Finds all faculty members belonging to a specific department.
     *
     * @param department the department name
     * @return list of faculty in the department
     */
    List<Faculty> findByDepartment(String department);

    /**
     * Finds all faculty members with a specific designation.
     *
     * @param designation the designation (e.g. "Assistant Professor")
     * @return list of matching faculty
     */
    List<Faculty> findByDesignation(String designation);

    /**
     * Finds faculty by department and designation.
     *
     * @param department  the department name
     * @param designation the designation
     * @return list of matching faculty
     */
    List<Faculty> findByDepartmentAndDesignation(String department, String designation);

    /**
     * Finds all faculty members by their active/inactive status.
     *
     * @param status the status string (e.g. "ACTIVE", "INACTIVE")
     * @return list of faculty matching the status
     */
    List<Faculty> findByStatus(String status);

    /**
     * Checks if a faculty member exists with the given employee ID.
     *
     * @param employeeId the employee ID to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmployeeId(String employeeId);

    /**
     * Checks if a faculty member exists with the given email.
     *
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Counts faculty members in a specific department.
     *
     * @param department the department name
     * @return total count of faculty in the department
     */
    long countByDepartment(String department);
}
