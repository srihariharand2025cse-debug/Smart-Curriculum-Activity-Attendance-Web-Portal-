-- ===================================================================
-- Smart Curriculum Activity & Attendance Web Portal
-- Day 5: MySQL Database Initialization & Student Schema Script
-- ===================================================================

-- 1. Create database if it does not already exist
CREATE DATABASE IF NOT EXISTS `smart_curriculum_db`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 2. Switch to the portal database
USE `smart_curriculum_db`;

-- 3. Create students table (Day 5 Database Mapping)
CREATE TABLE IF NOT EXISTS `students` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `roll_number` VARCHAR(50) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NULL,
    `email` VARCHAR(150) NOT NULL,
    `phone_number` VARCHAR(20) NULL,
    `department` VARCHAR(100) NOT NULL,
    `year_of_study` INT NOT NULL,
    `semester` INT NOT NULL,
    `section` VARCHAR(10) NULL,
    `gender` VARCHAR(20) NULL,
    `date_of_birth` DATE NULL,
    `address` VARCHAR(500) NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `uk_students_roll_number` UNIQUE (`roll_number`),
    CONSTRAINT `uk_students_email` UNIQUE (`email`),
    INDEX `idx_students_roll_number` (`roll_number`),
    INDEX `idx_students_department` (`department`),
    INDEX `idx_students_year_dept` (`department`, `year_of_study`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Seed sample student records for demonstration and testing
INSERT IGNORE INTO `students` (
    `roll_number`, `first_name`, `last_name`, `email`, `phone_number`,
    `department`, `year_of_study`, `semester`, `section`, `gender`, `status`
) VALUES
('21CSE001', 'Aarav', 'Sharma', 'aarav.sharma@college.edu', '9876543210', 'Computer Science and Engineering', 3, 5, 'A', 'Male', 'ACTIVE'),
('21CSE002', 'Diya', 'Patel', 'diya.patel@college.edu', '9876543211', 'Computer Science and Engineering', 3, 5, 'A', 'Female', 'ACTIVE'),
('21ECE015', 'Rohan', 'Verma', 'rohan.verma@college.edu', '9876543212', 'Electronics and Communication', 2, 3, 'B', 'Male', 'ACTIVE'),
('21MECH030', 'Pooja', 'Sundaram', 'pooja.sundaram@college.edu', '9876543213', 'Mechanical Engineering', 4, 7, 'A', 'Female', 'ACTIVE');

-- 5. Verify database and schema status
SELECT DATABASE() AS current_database, COUNT(*) AS total_sample_students FROM `students`;
