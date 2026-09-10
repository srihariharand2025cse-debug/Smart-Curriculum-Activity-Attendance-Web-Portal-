package com.smartcurriculum.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Basic controller to verify that our Spring Boot web application
 * is running and able to handle HTTP web requests.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Smart Curriculum Activity & Attendance Web Portal! Day 1 Setup is Successful.";
    }

    @GetMapping("/api/status")
    public String status() {
        return "Application is running smoothly!";
    }
}
