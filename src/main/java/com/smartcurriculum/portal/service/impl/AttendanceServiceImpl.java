package com.smartcurriculum.portal.service.impl;

import com.smartcurriculum.portal.dto.AttendanceRequestDto;
import com.smartcurriculum.portal.dto.AttendanceResponseDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto;
import com.smartcurriculum.portal.dto.AttendanceSummaryDto.ActivityAttendanceMetric;
import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Attendance;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.repository.AttendanceRepository;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.repository.StudentRepository;
import com.smartcurriculum.portal.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for Attendance tracking, calculation, and visual metrics.
 */
@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ActivityRepository activityRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 StudentRepository studentRepository,
                                 ActivityRepository activityRepository,
                                 FacultyRepository facultyRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.activityRepository = activityRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public AttendanceResponseDto markAttendance(AttendanceRequestDto request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));

        Activity activity = activityRepository.findById(request.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", request.getActivityId()));

        Faculty faculty = null;
        if (request.getFacultyId() != null) {
            faculty = facultyRepository.findById(request.getFacultyId())
                    .orElse(null);
        }

        String sessionSlot = (request.getSessionSlot() != null && !request.getSessionSlot().isBlank())
                ? request.getSessionSlot() : "SESSION_1";

        boolean exists = attendanceRepository.existsByStudentIdAndActivityIdAndAttendanceDateAndSessionSlot(
                student.getId(), activity.getId(), request.getAttendanceDate(), sessionSlot);

        if (exists) {
            throw new DuplicateResourceException("Attendance already marked for student " + student.getRollNumber()
                    + " in activity " + activity.getActivityCode() + " on " + request.getAttendanceDate() + " slot " + sessionSlot);
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setActivity(activity);
        attendance.setMarkedByFaculty(faculty);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus() != null ? request.getStatus().toUpperCase() : "PRESENT");
        attendance.setSessionSlot(sessionSlot);
        attendance.setRemarks(request.getRemarks());

        Attendance saved = attendanceRepository.save(attendance);
        return AttendanceResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSummaryDto getStudentAttendanceSummary(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        return buildStudentSummary(student);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSummaryDto getStudentAttendanceSummaryByRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "rollNumber", rollNumber));
        return buildStudentSummary(student);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceSummaryDto getActivityAttendanceSummary(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", activityId));

        List<Attendance> records = attendanceRepository.findByActivityId(activityId);
        long total = records.size();
        long present = records.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        long absent = records.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();
        long onDuty = records.stream().filter(a -> "ON_DUTY".equalsIgnoreCase(a.getStatus()) || "OD".equalsIgnoreCase(a.getStatus())).count();

        double percentage = total > 0 ? ((double) (present + onDuty) / total) * 100.0 : 0.0;
        percentage = Math.round(percentage * 100.0) / 100.0;

        AttendanceSummaryDto summary = new AttendanceSummaryDto();
        summary.setActivityId(activity.getId());
        summary.setActivityCode(activity.getActivityCode());
        summary.setActivityTitle(activity.getTitle());
        summary.setTotalSessions(total);
        summary.setAttendedSessions(present + onDuty);
        summary.setAbsentSessions(absent);
        summary.setOnDutySessions(onDuty);
        summary.setAttendancePercentage(percentage);

        if (percentage >= 75.0) {
            summary.setEligibilityStatus("ELIGIBLE");
            summary.setEligibilityBadge("Good Standing (>=75%)");
            summary.setStatusMessage("Activity turnout meets institutional standards");
        } else if (percentage >= 65.0) {
            summary.setEligibilityStatus("WARNING");
            summary.setEligibilityBadge("Average Attendance (65-75%)");
            summary.setStatusMessage("Attendance requires attention");
        } else {
            summary.setEligibilityStatus("CRITICAL");
            summary.setEligibilityBadge("Low Turnout (<65%)");
            summary.setStatusMessage("Low attendance recorded for this activity");
        }

        List<AttendanceResponseDto> dtoList = records.stream()
                .map(AttendanceResponseDto::fromEntity)
                .collect(Collectors.toList());
        summary.setRecentRecords(dtoList);

        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(AttendanceResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getAttendanceByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(AttendanceResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponseDto> getAttendanceByRollNumber(String rollNumber) {
        return attendanceRepository.findByStudentRollNumber(rollNumber).stream()
                .map(AttendanceResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    private AttendanceSummaryDto buildStudentSummary(Student student) {
        List<Attendance> records = attendanceRepository.findByStudentId(student.getId());

        long total = records.size();
        long present = records.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
        long absent = records.stream().filter(a -> "ABSENT".equalsIgnoreCase(a.getStatus())).count();
        long onDuty = records.stream().filter(a -> "ON_DUTY".equalsIgnoreCase(a.getStatus()) || "OD".equalsIgnoreCase(a.getStatus())).count();

        long attended = present + onDuty;
        double percentage = total > 0 ? ((double) attended / total) * 100.0 : 0.0;
        percentage = Math.round(percentage * 100.0) / 100.0;

        AttendanceSummaryDto summary = new AttendanceSummaryDto();
        summary.setStudentId(student.getId());
        summary.setStudentRollNumber(student.getRollNumber());
        summary.setStudentName(student.getFullName());
        summary.setDepartment(student.getDepartment());
        summary.setYearOfStudy(student.getYearOfStudy());

        summary.setTotalSessions(total);
        summary.setAttendedSessions(attended);
        summary.setAbsentSessions(absent);
        summary.setOnDutySessions(onDuty);
        summary.setAttendancePercentage(percentage);

        if (total == 0) {
            summary.setEligibilityStatus("NO_DATA");
            summary.setEligibilityBadge("No Records Yet");
            summary.setStatusMessage("No attendance records logged for current term");
        } else if (percentage >= 75.0) {
            summary.setEligibilityStatus("ELIGIBLE");
            summary.setEligibilityBadge("Exam Eligible (Good Standing)");
            summary.setStatusMessage("Attendance is above the mandatory 75% threshold.");
        } else if (percentage >= 65.0) {
            summary.setEligibilityStatus("WARNING");
            summary.setEligibilityBadge("Condonation Required (65-75%)");
            summary.setStatusMessage("Attendance is below 75%. Institutional condonation will be required.");
        } else {
            summary.setEligibilityStatus("CRITICAL");
            summary.setEligibilityBadge("Shortage Alert (<65%)");
            summary.setStatusMessage("Critical shortage! Attendance is below 65%. Ineligible for exams without exemption.");
        }

        // Breakdown by activity
        Map<Activity, List<Attendance>> byActivity = records.stream()
                .filter(a -> a.getActivity() != null)
                .collect(Collectors.groupingBy(Attendance::getActivity));

        List<ActivityAttendanceMetric> metrics = new ArrayList<>();
        for (Map.Entry<Activity, List<Attendance>> entry : byActivity.entrySet()) {
            Activity act = entry.getKey();
            List<Attendance> actRecords = entry.getValue();
            long actTotal = actRecords.size();
            long actAttended = actRecords.stream()
                    .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()) || "ON_DUTY".equalsIgnoreCase(a.getStatus()) || "OD".equalsIgnoreCase(a.getStatus()))
                    .count();
            double actPct = actTotal > 0 ? ((double) actAttended / actTotal) * 100.0 : 0.0;
            actPct = Math.round(actPct * 100.0) / 100.0;

            metrics.add(new ActivityAttendanceMetric(
                    act.getId(),
                    act.getActivityCode(),
                    act.getTitle(),
                    act.getActivityType(),
                    actTotal,
                    actAttended,
                    actPct
            ));
        }
        summary.setActivityBreakdown(metrics);

        // Recent records
        List<AttendanceResponseDto> recentDto = records.stream()
                .sorted((a, b) -> {
                    if (a.getAttendanceDate() == null || b.getAttendanceDate() == null) return 0;
                    return b.getAttendanceDate().compareTo(a.getAttendanceDate());
                })
                .limit(20)
                .map(AttendanceResponseDto::fromEntity)
                .collect(Collectors.toList());
        summary.setRecentRecords(recentDto);

        return summary;
    }
}
