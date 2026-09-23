package com.smartcurriculum.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Simple view controller for static dashboard pages added on Day 13.
 * Returns the Thymeleaf template names located under src/main/resources/templates.
 */
@Controller
public class DashboardController {

    @GetMapping("/student")
    public String studentDashboard() {
        return "student"; // resolves to student.html
    }

    @GetMapping("/faculty")
    public String facultyDashboard() {
        return "faculty";
    }

    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin";
    }
}
