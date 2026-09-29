package com.smartcurriculum.portal.service;

import com.smartcurriculum.portal.dto.ActivityRequestDto;
import com.smartcurriculum.portal.dto.ActivityResponseDto;
import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.exception.DuplicateResourceException;
import com.smartcurriculum.portal.exception.ResourceNotFoundException;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.service.impl.ActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ActivityServiceImpl — Day 18 end-to-end testing.
 */
@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity activity;
    private ActivityRequestDto requestDto;
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setId(1L);
        faculty.setEmployeeId("FAC001");
        faculty.setFirstName("Dr. Kumar");

        activity = new Activity();
        activity.setId(1L);
        activity.setActivityCode("ACT-CS501-LAB");
        activity.setTitle("Data Structures Lab");
        activity.setDescription("Practical lab sessions on data structures");
        activity.setActivityType("LABORATORY");
        activity.setDepartment("Computer Science and Engineering");
        activity.setAcademicYear("2025-2026");
        activity.setSemester(5);
        activity.setCredits(2);
        activity.setVenue("Lab Hall 3");
        activity.setFaculty(faculty);
        activity.setStartDate(LocalDate.of(2025, 8, 1));
        activity.setEndDate(LocalDate.of(2025, 12, 15));
        activity.setMaxEnrollment(60);
        activity.setStatus("ACTIVE");

        requestDto = new ActivityRequestDto();
        requestDto.setActivityCode("ACT-CS501-LAB");
        requestDto.setTitle("Data Structures Lab");
        requestDto.setDescription("Practical lab sessions on data structures");
        requestDto.setActivityType("LABORATORY");
        requestDto.setDepartment("Computer Science and Engineering");
        requestDto.setAcademicYear("2025-2026");
        requestDto.setSemester(5);
        requestDto.setCredits(2);
        requestDto.setVenue("Lab Hall 3");
        requestDto.setFacultyId(1L);
        requestDto.setStartDate(LocalDate.of(2025, 8, 1));
        requestDto.setEndDate(LocalDate.of(2025, 12, 15));
        requestDto.setMaxEnrollment(60);
        requestDto.setStatus("ACTIVE");
    }

    @Test
    @DisplayName("Should create activity successfully")
    void shouldCreateActivitySuccessfully() {
        when(activityRepository.existsByActivityCode("ACT-CS501-LAB")).thenReturn(false);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> {
            Activity a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        ActivityResponseDto response = activityService.createActivity(requestDto);

        assertNotNull(response);
        assertEquals("ACT-CS501-LAB", response.getActivityCode());
        assertEquals("Data Structures Lab", response.getTitle());
        assertEquals(1L, response.getFacultyId());
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when activity code exists")
    void shouldThrowDuplicate_WhenActivityCodeExists() {
        when(activityRepository.existsByActivityCode("ACT-CS501-LAB")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> activityService.createActivity(requestDto));
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("Should create activity without faculty when facultyId is null")
    void shouldCreateActivity_WithoutFaculty() {
        requestDto.setFacultyId(null);
        when(activityRepository.existsByActivityCode("ACT-CS501-LAB")).thenReturn(false);
        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> {
            Activity a = inv.getArgument(0);
            a.setId(2L);
            return a;
        });

        ActivityResponseDto response = activityService.createActivity(requestDto);

        assertNotNull(response);
        assertNull(response.getFacultyId());
    }

    @Test
    @DisplayName("Should return paginated activities")
    void shouldReturnPaginatedActivities() {
        Page<Activity> activityPage = new PageImpl<>(List.of(activity), PageRequest.of(0, 10), 1);
        when(activityRepository.findAll(PageRequest.of(0, 10))).thenReturn(activityPage);

        Page<ActivityResponseDto> result = activityService.getAllActivities(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("ACT-CS501-LAB", result.getContent().get(0).getActivityCode());
    }

    @Test
    @DisplayName("Should retrieve activity by ID")
    void shouldReturnActivityById() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        ActivityResponseDto result = activityService.getActivityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Data Structures Lab", result.getTitle());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when activity ID not found")
    void shouldThrowException_WhenActivityIdNotFound() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.getActivityById(99L));
    }

    @Test
    @DisplayName("Should retrieve activity by code")
    void shouldReturnActivityByCode() {
        when(activityRepository.findByActivityCode("ACT-CS501-LAB")).thenReturn(Optional.of(activity));

        ActivityResponseDto result = activityService.getActivityByCode("ACT-CS501-LAB");

        assertNotNull(result);
        assertEquals("ACT-CS501-LAB", result.getActivityCode());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when activity code not found")
    void shouldThrowException_WhenActivityCodeNotFound() {
        when(activityRepository.findByActivityCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.getActivityByCode("UNKNOWN"));
    }

    @Test
    @DisplayName("Should retrieve activities by type")
    void shouldReturnActivitiesByType() {
        when(activityRepository.findByActivityType("LABORATORY")).thenReturn(List.of(activity));

        List<ActivityResponseDto> result = activityService.getActivitiesByType("LABORATORY");

        assertEquals(1, result.size());
        assertEquals("LABORATORY", result.get(0).getActivityType());
    }

    @Test
    @DisplayName("Should retrieve all activities as list")
    void shouldReturnAllActivitiesList() {
        when(activityRepository.findAll()).thenReturn(List.of(activity));

        List<ActivityResponseDto> result = activityService.getAllActivitiesList();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should retrieve activities by faculty ID")
    void shouldReturnActivitiesByFacultyId() {
        when(activityRepository.findByFacultyId(1L)).thenReturn(List.of(activity));

        List<ActivityResponseDto> result = activityService.getActivitiesByFacultyId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getFacultyId());
    }

    @Test
    @DisplayName("Should retrieve activities by department")
    void shouldReturnActivitiesByDepartment() {
        when(activityRepository.findByDepartment("Computer Science and Engineering")).thenReturn(List.of(activity));

        List<ActivityResponseDto> result = activityService.getActivitiesByDepartment("Computer Science and Engineering");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should update activity successfully")
    void shouldUpdateActivitySuccessfully() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);

        requestDto.setTitle("Advanced Data Structures Lab");
        ActivityResponseDto updated = activityService.updateActivity(1L, requestDto);

        assertNotNull(updated);
        verify(activityRepository).save(activity);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on update when activity code taken")
    void shouldThrowDuplicate_OnUpdate_WhenCodeTaken() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        requestDto.setActivityCode("ACT-NEW-CODE");
        when(activityRepository.existsByActivityCode("ACT-NEW-CODE")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> activityService.updateActivity(1L, requestDto));
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    @DisplayName("Should delete activity successfully")
    void shouldDeleteActivitySuccessfully() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        activityService.deleteActivity(1L);

        verify(activityRepository).delete(activity);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException on delete when not found")
    void shouldThrowException_OnDelete_WhenNotFound() {
        when(activityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> activityService.deleteActivity(99L));
    }

    @Test
    @DisplayName("Should return total activity count")
    void shouldReturnTotalActivityCount() {
        when(activityRepository.count()).thenReturn(8L);

        long count = activityService.getTotalActivityCount();

        assertEquals(8L, count);
    }
}
