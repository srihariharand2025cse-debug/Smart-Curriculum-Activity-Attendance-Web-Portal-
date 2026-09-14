-- ===================================================================
-- Smart Curriculum Activity & Attendance Web Portal
-- Day 4: MySQL Database Initialization Script
-- ===================================================================

-- 1. Create database if it does not already exist
CREATE DATABASE IF NOT EXISTS `smart_curriculum_db`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 2. Switch to the portal database
USE `smart_curriculum_db`;

-- 3. Verify database creation
SELECT DATABASE() AS current_database, 'Database initialized successfully for Day 4' AS status;
