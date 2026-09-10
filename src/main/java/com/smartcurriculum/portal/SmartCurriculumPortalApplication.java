package com.smartcurriculum.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Main Application Class for Smart Curriculum Activity & Attendance Web Portal.
 * Note: DataSourceAutoConfiguration is excluded temporarily for Day 1-3
 * so the application runs before configuring MySQL on Day 4.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SmartCurriculumPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCurriculumPortalApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" Smart Curriculum Activity & Attendance Portal ");
        System.out.println(" Server started on http://localhost:8080 ");
        System.out.println("=================================================");
    }
}
