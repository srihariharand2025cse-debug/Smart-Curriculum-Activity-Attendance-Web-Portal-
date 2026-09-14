package com.smartcurriculum.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class for Smart Curriculum Activity & Attendance Web Portal.
 * Day 4: MySQL Database Configuration & Connection Setup enabled.
 */
@SpringBootApplication
public class SmartCurriculumPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCurriculumPortalApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" Smart Curriculum Activity & Attendance Portal ");
        System.out.println(" Server started on http://localhost:8080 ");
        System.out.println("=================================================");
    }
}
