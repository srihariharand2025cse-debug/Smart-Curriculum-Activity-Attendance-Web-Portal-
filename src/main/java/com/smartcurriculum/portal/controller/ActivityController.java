package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.ActivityRequestDto;
import com.smartcurriculum.portal.dto.ActivityResponseDto;
import com.smartcurriculum.portal.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing curriculum activities.
 */
@RestController
@RequestMapping("/api/activities")
@Validated
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    // Create activity
    @PostMapping
    public ResponseEntity<ActivityResponseDto> createActivity(@RequestBody ActivityRequestDto requestDto) {
        ActivityResponseDto created = activityService.createActivity(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get all activities with pagination
    @GetMapping
    public ResponseEntity<Page<ActivityResponseDto>> getAllActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ActivityResponseDto> result = activityService.getAllActivities(page, size);
        return ResponseEntity.ok(result);
    }

    // Get activity by ID
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> getActivityById(@PathVariable Long id) {
        ActivityResponseDto dto = activityService.getActivityById(id);
        return ResponseEntity.ok(dto);
    }

    // Get activity by code
    @GetMapping("/code/{code}")
    public ResponseEntity<ActivityResponseDto> getActivityByCode(@PathVariable("code") String activityCode) {
        ActivityResponseDto dto = activityService.getActivityByCode(activityCode);
        return ResponseEntity.ok(dto);
    }

    // Get all activities as list
    @GetMapping("/list")
    public ResponseEntity<List<ActivityResponseDto>> getAllActivitiesList() {
        List<ActivityResponseDto> list = activityService.getAllActivitiesList();
        return ResponseEntity.ok(list);
    }

    // Get activities by faculty ID
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<ActivityResponseDto>> getActivitiesByFaculty(@PathVariable Long facultyId) {
        List<ActivityResponseDto> list = activityService.getActivitiesByFacultyId(facultyId);
        return ResponseEntity.ok(list);
    }

    // Get activities by department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<ActivityResponseDto>> getActivitiesByDepartment(@PathVariable String department) {
        List<ActivityResponseDto> list = activityService.getActivitiesByDepartment(department);
        return ResponseEntity.ok(list);
    }

    // Get activities by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ActivityResponseDto>> getActivitiesByType(@PathVariable String type) {
        List<ActivityResponseDto> list = activityService.getActivitiesByType(type);
        return ResponseEntity.ok(list);
    }

    // Update activity
    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> updateActivity(@PathVariable Long id,
                                                               @RequestBody ActivityRequestDto requestDto) {
        ActivityResponseDto updated = activityService.updateActivity(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    // Delete activity
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
}
