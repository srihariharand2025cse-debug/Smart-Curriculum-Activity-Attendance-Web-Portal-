-- ===================================================================
-- Smart Curriculum Activity & Attendance Web Portal
-- Day 5 - Day 8: MySQL Schema (Students, Faculty, Activities, Attendance)
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

-- 4. Create faculty table (Day 6 Database Mapping)
CREATE TABLE IF NOT EXISTS `faculty` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `employee_id` VARCHAR(50) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NULL,
    `email` VARCHAR(150) NOT NULL,
    `phone_number` VARCHAR(20) NULL,
    `department` VARCHAR(100) NOT NULL,
    `designation` VARCHAR(100) NOT NULL,
    `specialization` VARCHAR(200) NULL,
    `qualification` VARCHAR(150) NULL,
    `experience_years` INT NULL,
    `gender` VARCHAR(20) NULL,
    `date_of_birth` DATE NULL,
    `date_of_joining` DATE NULL,
    `address` VARCHAR(500) NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `uk_faculty_employee_id` UNIQUE (`employee_id`),
    CONSTRAINT `uk_faculty_email` UNIQUE (`email`),
    INDEX `idx_faculty_employee_id` (`employee_id`),
    INDEX `idx_faculty_department` (`department`),
    INDEX `idx_faculty_designation` (`designation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Create activities table (Day 7 Database Mapping)
CREATE TABLE IF NOT EXISTS `activities` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `activity_code` VARCHAR(50) NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `description` VARCHAR(1000) NULL,
    `activity_type` VARCHAR(50) NOT NULL,
    `department` VARCHAR(100) NOT NULL,
    `academic_year` VARCHAR(20) NULL,
    `semester` INT NULL,
    `credits` INT NULL,
    `venue` VARCHAR(150) NULL,
    `faculty_id` BIGINT NULL,
    `start_date` DATE NULL,
    `end_date` DATE NULL,
    `max_enrollment` INT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `uk_activities_activity_code` UNIQUE (`activity_code`),
    CONSTRAINT `fk_activities_faculty` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`id`) ON DELETE SET NULL,
    INDEX `idx_activities_activity_code` (`activity_code`),
    INDEX `idx_activities_department` (`department`),
    INDEX `idx_activities_activity_type` (`activity_type`),
    INDEX `idx_activities_status` (`status`),
    INDEX `idx_activities_dept_sem` (`department`, `semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Create attendance table (Day 8 Database Mapping)
CREATE TABLE IF NOT EXISTS `attendance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `activity_id` BIGINT NOT NULL,
    `faculty_id` BIGINT NULL,
    `attendance_date` DATE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'PRESENT',
    `session_slot` VARCHAR(50) NOT NULL DEFAULT 'SESSION_1',
    `remarks` VARCHAR(500) NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `uk_attendance_record` UNIQUE (`student_id`, `activity_id`, `attendance_date`, `session_slot`),
    CONSTRAINT `fk_attendance_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_attendance_activity` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_attendance_faculty` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`id`) ON DELETE SET NULL,
    INDEX `idx_attendance_student_id` (`student_id`),
    INDEX `idx_attendance_activity_id` (`activity_id`),
    INDEX `idx_attendance_faculty_id` (`faculty_id`),
    INDEX `idx_attendance_date` (`attendance_date`),
    INDEX `idx_attendance_status` (`status`),
    INDEX `idx_attendance_student_date` (`student_id`, `attendance_date`),
    INDEX `idx_attendance_act_date` (`activity_id`, `attendance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Seed sample student records
INSERT IGNORE INTO `students` (
    `roll_number`, `first_name`, `last_name`, `email`, `phone_number`,
    `department`, `year_of_study`, `semester`, `section`, `gender`, `status`
) VALUES
('21CSE001', 'Aarav', 'Sharma', 'aarav.sharma@college.edu', '9876543210', 'Computer Science and Engineering', 3, 5, 'A', 'Male', 'ACTIVE'),
('21CSE002', 'Diya', 'Patel', 'diya.patel@college.edu', '9876543211', 'Computer Science and Engineering', 3, 5, 'A', 'Female', 'ACTIVE'),
('21ECE015', 'Rohan', 'Verma', 'rohan.verma@college.edu', '9876543212', 'Electronics and Communication', 2, 3, 'B', 'Male', 'ACTIVE'),
('21MECH030', 'Pooja', 'Sundaram', 'pooja.sundaram@college.edu', '9876543213', 'Mechanical Engineering', 4, 7, 'A', 'Female', 'ACTIVE');

-- 8. Seed sample faculty records
INSERT IGNORE INTO `faculty` (
    `employee_id`, `first_name`, `last_name`, `email`, `phone_number`,
    `department`, `designation`, `specialization`, `qualification`, `experience_years`,
    `gender`, `date_of_joining`, `status`
) VALUES
('FAC001', 'Dr. Ravi', 'Kumar', 'ravi.kumar@college.edu', '9800000001', 'Computer Science and Engineering', 'Professor', 'Artificial Intelligence', 'Ph.D Computer Science', 15, 'Male', '2010-07-01', 'ACTIVE'),
('FAC002', 'Dr. Meena', 'Nair', 'meena.nair@college.edu', '9800000002', 'Computer Science and Engineering', 'Associate Professor', 'Data Structures & Algorithms', 'Ph.D Information Technology', 10, 'Female', '2015-06-15', 'ACTIVE'),
('FAC003', 'Mr. Suresh', 'Babu', 'suresh.babu@college.edu', '9800000003', 'Electronics and Communication', 'Assistant Professor', 'VLSI Design', 'M.E Electronics', 5, 'Male', '2020-08-01', 'ACTIVE'),
('FAC004', 'Ms. Lakshmi', 'Priya', 'lakshmi.priya@college.edu', '9800000004', 'Mechanical Engineering', 'Assistant Professor', 'Thermal Engineering', 'M.E Mechanical', 3, 'Female', '2022-07-20', 'ACTIVE');

-- 9. Seed sample activity records
INSERT IGNORE INTO `activities` (
    `activity_code`, `title`, `description`, `activity_type`, `department`,
    `academic_year`, `semester`, `credits`, `venue`, `faculty_id`, `status`, `max_enrollment`
) VALUES
('ACT-CS501-LAB', 'Data Structures & Algorithms Laboratory', 'Hands-on practical session implementing trees, graphs, and dynamic programming.', 'LAB', 'Computer Science and Engineering', '2025-2026', 5, 2, 'Computer Lab 3', 2, 'ACTIVE', 60),
('ACT-CS502-LEC', 'Artificial Intelligence & Machine Learning', 'Core theory lecture on heuristic search, neural networks, and modern LLM foundations.', 'LECTURE', 'Computer Science and Engineering', '2025-2026', 5, 4, 'Hall 204', 1, 'ACTIVE', 75),
('ACT-EC301-LAB', 'VLSI Design & Digital Simulation', 'Hardware description language synthesis and FPGA verification session.', 'LAB', 'Electronics and Communication', '2025-2026', 3, 2, 'VLSI Centre', 3, 'ACTIVE', 50),
('ACT-ME701-SEM', 'Renewable Energy & Thermal Systems Workshop', 'Industry guest speaker workshop on solar photovoltaic and electric mobility.', 'WORKSHOP', 'Mechanical Engineering', '2025-2026', 7, 1, 'Auditorium B', 4, 'ACTIVE', 120);

-- 10. Seed sample attendance records (Day 8 Mapping)
INSERT IGNORE INTO `attendance` (
    `student_id`, `activity_id`, `faculty_id`, `attendance_date`, `status`, `session_slot`, `remarks`
) VALUES
(1, 1, 2, '2026-09-10', 'PRESENT', 'SESSION_1', 'Attended DSA Lab on Binary Trees'),
(2, 1, 2, '2026-09-10', 'PRESENT', 'SESSION_1', 'Attended DSA Lab on Binary Trees'),
(1, 2, 1, '2026-09-11', 'PRESENT', 'SESSION_2', 'Attended AI lecture on A* Search'),
(2, 2, 1, '2026-09-11', 'ABSENT', 'SESSION_2', 'Medical leave submitted'),
(3, 3, 3, '2026-09-12', 'PRESENT', 'SESSION_1', 'Completed VLSI simulation module 1'),
(4, 4, 4, '2026-09-15', 'ON_DUTY', 'SESSION_1', 'Representing college at Renewable Energy Summit');

-- 11. Verify database and schema status
SELECT
    (SELECT COUNT(*) FROM `students`) AS total_students,
    (SELECT COUNT(*) FROM `faculty`) AS total_faculty,
    (SELECT COUNT(*) FROM `activities`) AS total_activities,
    (SELECT COUNT(*) FROM `attendance`) AS total_attendance,
    'Day 8: Attendance schema and data initialized successfully' AS status;


