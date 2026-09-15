package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository interface for {@link Student} entity.
 * Provides abstracted CRUD operations and derived custom database queries.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Finds a student by their unique roll number.
     *
     * @param rollNumber the student's roll number
     * @return an Optional containing the student if found
     */
    Optional<Student> findByRollNumber(String rollNumber);

    /**
     * Finds a student by their institutional email address.
     *
     * @param email the student's email address
     * @return an Optional containing the student if found
     */
    Optional<Student> findByEmail(String email);

    /**
     * Finds all students belonging to a specific department.
     *
     * @param department the department name (e.g. "Computer Science and Engineering")
     * @return list of matching students
     */
    List<Student> findByDepartment(String department);

    /**
     * Finds students by department and year of study.
     *
     * @param department  the department name
     * @param yearOfStudy year of study (1, 2, 3, 4)
     * @return list of matching students
     */
    List<Student> findByDepartmentAndYearOfStudy(String department, Integer yearOfStudy);

    /**
     * Finds students by department, year of study, and class section.
     *
     * @param department  the department name
     * @param yearOfStudy year of study
     * @param section     class section (e.g. "A", "B")
     * @return list of matching students
     */
    List<Student> findByDepartmentAndYearOfStudyAndSection(String department, Integer yearOfStudy, String section);

    /**
     * Finds all students by their active/inactive status.
     *
     * @param status the status string (e.g. "ACTIVE", "INACTIVE")
     * @return list of students matching the status
     */
    List<Student> findByStatus(String status);

    /**
     * Checks if a student exists with the given roll number.
     *
     * @param rollNumber the roll number to check
     * @return true if exists, false otherwise
     */
    boolean existsByRollNumber(String rollNumber);

    /**
     * Checks if a student exists with the given email.
     *
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Counts students in a specific department.
     *
     * @param department the department name
     * @return total count of students in the department
     */
    long countByDepartment(String department);
}
