package com.smartcurriculum.portal.controller;

import com.smartcurriculum.portal.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller to verify portal status, health, and layered architecture readiness.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Smart Curriculum Activity & Attendance Web Portal! Day 3 Layered Architecture Setup is Complete.";
    }

    @GetMapping("/api/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatus() {
        Map<String, Object> statusData = new LinkedHashMap<>();
        statusData.put("status", "UP");
        statusData.put("project", "Smart Curriculum Activity & Attendance Web Portal");
        statusData.put("currentMilestone", "Day 3: Layered Package Structure Setup");
        statusData.put("layersConfigured", new String[]{
                "controller",
                "service (interface & impl)",
                "repository",
                "entity",
                "dto",
                "exception"
        });
        statusData.put("nextMilestone", "Day 4: MySQL Database Configuration");

        return ResponseEntity.ok(ApiResponse.success("Portal API is running smoothly", statusData));
    }
}
