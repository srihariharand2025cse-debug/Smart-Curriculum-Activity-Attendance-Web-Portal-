package com.smartcurriculum.portal.repository;

import com.smartcurriculum.portal.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository interface for {@link Attendance} entity.
 * Provides abstracted CRUD operations, student/activity queries, date filtering,
 * and attendance metrics count methods.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Finds all attendance records for a specific student by their entity ID.
     *
     * @param studentId ID of the student
     * @return list of matching attendance records
     */
    List<Attendance> findByStudentId(Long studentId);

    /**
     * Finds all attendance records for a student by their unique roll number.
     *
     * @param rollNumber the student roll number (e.g. "21CSE001")
     * @return list of matching attendance records
     */
    List<Attendance> findByStudentRollNumber(String rollNumber);

    /**
     * Finds all attendance records for an activity by its entity ID.
     *
     * @param activityId ID of the activity
     * @return list of matching attendance records
     */
    List<Attendance> findByActivityId(Long activityId);

    /**
     * Finds all attendance records for an activity by its unique activity code.
     *
     * @param activityCode the activity code (e.g. "ACT-CS501-LAB")
     * @return list of matching attendance records
     */
    List<Attendance> findByActivityActivityCode(String activityCode);

    /**
     * Finds all attendance records on a specific date.
     *
     * @param attendanceDate date of the session
     * @return list of matching attendance records
     */
    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    /**
     * Finds all attendance records matching a given status (e.g. "PRESENT", "ABSENT", "ON_DUTY").
     *
     * @param status attendance status
     * @return list of matching attendance records
     */
    List<Attendance> findByStatus(String status);

    /**
     * Finds attendance records for a student on a specific date.
     *
     * @param studentId      student entity ID
     * @param attendanceDate date of attendance
     * @return list of matching records
     */
    List<Attendance> findByStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    /**
     * Finds all attendance records for a student in a specific activity.
     *
     * @param studentId  student entity ID
     * @param activityId activity entity ID
     * @return list of matching records
     */
    List<Attendance> findByStudentIdAndActivityId(Long studentId, Long activityId);

    /**
     * Finds all attendance records for an activity on a specific date.
     *
     * @param activityId     activity entity ID
     * @param attendanceDate date of activity session
     * @return list of matching records
     */
    List<Attendance> findByActivityIdAndAttendanceDate(Long activityId, LocalDate attendanceDate);

    /**
     * Finds attendance records within an inclusive date range.
     *
     * @param startDate range start date
     * @param endDate   range end date
     * @return list of records in date range
     */
    List<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Finds attendance records for a student within a date range.
     *
     * @param studentId student entity ID
     * @param startDate range start date
     * @param endDate   range end date
     * @return list of matching records
     */
    List<Attendance> findByStudentIdAndAttendanceDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);

    /**
     * Finds attendance records for an activity within a date range.
     *
     * @param activityId activity entity ID
     * @param startDate  range start date
     * @param endDate    range end date
     * @return list of matching records
     */
    List<Attendance> findByActivityIdAndAttendanceDateBetween(Long activityId, LocalDate startDate, LocalDate endDate);

    /**
     * Finds a unique attendance record for a student, activity, date, and session slot.
     *
     * @param studentId      student entity ID
     * @param activityId     activity entity ID
     * @param attendanceDate date of session
     * @param sessionSlot    session slot identifier
     * @return Optional containing matching Attendance
     */
    Optional<Attendance> findByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
            Long studentId, Long activityId, LocalDate attendanceDate, String sessionSlot);

    /**
     * Checks whether an attendance record already exists for the given slot.
     *
     * @param studentId      student entity ID
     * @param activityId     activity entity ID
     * @param attendanceDate date of session
     * @param sessionSlot    session slot identifier
     * @return true if record exists, false otherwise
     */
    boolean existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
            Long studentId, Long activityId, LocalDate attendanceDate, String sessionSlot);

    /**
     * Counts attendance records for a student with a given status.
     *
     * @param studentId student entity ID
     * @param status    attendance status (e.g. "PRESENT")
     * @return total matching count
     */
    long countByStudentIdAndStatus(Long studentId, String status);

    /**
     * Counts attendance records for an activity with a given status.
     *
     * @param activityId activity entity ID
     * @param status     attendance status (e.g. "PRESENT")
     * @return total matching count
     */
    long countByActivityIdAndStatus(Long activityId, String status);

    /**
     * Counts total attendance records for a student.
     *
     * @param studentId student entity ID
     * @return total count
     */
    long countByStudentId(Long studentId);

    /**
     * Counts total attendance records for an activity.
     *
     * @param activityId activity entity ID
     * @return total count
     */
    long countByActivityId(Long activityId);

    /**
     * Counts total attendance records matching a specific status institution-wide.
     *
     * @param status status name
     * @return total count
     */
    long countByStatus(String status);
}
