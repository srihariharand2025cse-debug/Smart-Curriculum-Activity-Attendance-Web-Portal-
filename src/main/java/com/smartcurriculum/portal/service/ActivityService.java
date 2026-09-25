package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.ActivityRequestDto;
import com.smartcurriculum.portal.dto.ActivityResponseDto;
import org.springframework.data.domain.Page;

/**
 * Service interface defining CRUD operations and queries for Activity entities.
 */
public interface ActivityService {

    /**
     * Creates a new activity.
     */
    ActivityResponseDto createActivity(ActivityRequestDto requestDto);

    /**
     * Retrieves a paginated list of activities.
     *
     * @param page zero‑based page index
     * @param size page size
     * @return a page of activity response DTOs
     */
    Page<ActivityResponseDto> getAllActivities(int page, int size);

    /**
     * Retrieves an activity by its primary database ID.
     */
    ActivityResponseDto getActivityById(Long id);

    /**
     * Retrieves an activity by its unique activity code.
     */
    ActivityResponseDto getActivityByCode(String activityCode);

    /**
     * Retrieves all activities of a given type.
     */
    java.util.List<ActivityResponseDto> getActivitiesByType(String type);

    /**
     * Updates an existing activity.
     */
    ActivityResponseDto updateActivity(Long id, ActivityRequestDto requestDto);

    /**
     * Deletes an activity.
     */
    void deleteActivity(Long id);

    /**
     * Retrieves all activities as an unpaginated list.
     */
    java.util.List<ActivityResponseDto> getAllActivitiesList();

    /**
     * Retrieves all activities coordinated by a specific faculty member ID.
     */
    java.util.List<ActivityResponseDto> getActivitiesByFacultyId(Long facultyId);

    /**
     * Retrieves all activities belonging to a department.
     */
    java.util.List<ActivityResponseDto> getActivitiesByDepartment(String department);

    /**
     * Returns total count of activities.
     */
    long getTotalActivityCount();
}
