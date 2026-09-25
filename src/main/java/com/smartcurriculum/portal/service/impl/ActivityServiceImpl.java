package com.smartcurriculum.portal.service.impl;

import com.smartcurriculum.portal.dto.ActivityRequestDto;
import com.smartcurriculum.portal.dto.ActivityResponseDto;
import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Activity management.
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final com.smartcurriculum.portal.repository.FacultyRepository facultyRepository;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository,
                               com.smartcurriculum.portal.repository.FacultyRepository facultyRepository) {
        this.activityRepository = activityRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public ActivityResponseDto createActivity(ActivityRequestDto requestDto) {
        // Validate uniqueness of activity code
        if (activityRepository.existsByActivityCode(requestDto.getActivityCode())) {
            throw new DuplicateResourceException("Activity", "activityCode", requestDto.getActivityCode());
        }
        // Map DTO to entity
        Activity activity = new Activity();
        activity.setActivityCode(requestDto.getActivityCode());
        activity.setTitle(requestDto.getTitle());
        activity.setDescription(requestDto.getDescription());
        activity.setActivityType(requestDto.getActivityType());
        activity.setDepartment(requestDto.getDepartment());
        activity.setAcademicYear(requestDto.getAcademicYear());
        activity.setSemester(requestDto.getSemester());
        activity.setCredits(requestDto.getCredits());
        activity.setVenue(requestDto.getVenue());
        activity.setStartDate(requestDto.getStartDate());
        activity.setEndDate(requestDto.getEndDate());
        activity.setMaxEnrollment(requestDto.getMaxEnrollment());
        activity.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "ACTIVE");

        if (requestDto.getFacultyId() != null) {
            facultyRepository.findById(requestDto.getFacultyId()).ifPresent(activity::setFaculty);
        }

        Activity saved = activityRepository.save(activity);
        return ActivityResponseDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> getAllActivities(int page, int size) {
        Page<Activity> activityPage = activityRepository.findAll(PageRequest.of(page, size));
        List<ActivityResponseDto> dtoList = activityPage.getContent()
                .stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, activityPage.getPageable(), activityPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponseDto getActivityById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
        return ActivityResponseDto.fromEntity(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivityResponseDto getActivityByCode(String activityCode) {
        Activity activity = activityRepository.findByActivityCode(activityCode)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "activityCode", activityCode));
        return ActivityResponseDto.fromEntity(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getActivitiesByType(String type) {
        return activityRepository.findByActivityType(type)
                .stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityResponseDto updateActivity(Long id, ActivityRequestDto requestDto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
        // If activity code is being changed, ensure uniqueness
        if (!activity.getActivityCode().equals(requestDto.getActivityCode()) &&
                activityRepository.existsByActivityCode(requestDto.getActivityCode())) {
            throw new DuplicateResourceException("Activity", "activityCode", requestDto.getActivityCode());
        }
        // Update fields
        activity.setActivityCode(requestDto.getActivityCode());
        activity.setTitle(requestDto.getTitle());
        activity.setDescription(requestDto.getDescription());
        activity.setActivityType(requestDto.getActivityType());
        activity.setDepartment(requestDto.getDepartment());
        activity.setAcademicYear(requestDto.getAcademicYear());
        activity.setSemester(requestDto.getSemester());
        activity.setCredits(requestDto.getCredits());
        activity.setVenue(requestDto.getVenue());
        activity.setStartDate(requestDto.getStartDate());
        activity.setEndDate(requestDto.getEndDate());
        activity.setMaxEnrollment(requestDto.getMaxEnrollment());
        activity.setStatus(requestDto.getStatus() != null ? requestDto.getStatus() : "ACTIVE");
        if (requestDto.getFacultyId() != null) {
            facultyRepository.findById(requestDto.getFacultyId()).ifPresent(activity::setFaculty);
        }
        Activity saved = activityRepository.save(activity);
        return ActivityResponseDto.fromEntity(saved);
    }

    @Override
    public void deleteActivity(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", "id", id));
        activityRepository.delete(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getAllActivitiesList() {
        return activityRepository.findAll()
                .stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getActivitiesByFacultyId(Long facultyId) {
        return activityRepository.findByFacultyId(facultyId)
                .stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getActivitiesByDepartment(String department) {
        return activityRepository.findByDepartment(department)
                .stream()
                .map(ActivityResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalActivityCount() {
        return activityRepository.count();
    }
}
