# Changelog

All notable changes to the **Smart Curriculum Activity & Attendance Web Portal** are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

---

## [1.0.0] — 2026-10-03 — 🎉 Initial Release

### Summary
Full 20-day development cycle completed. 181 automated tests passing. Production-ready Spring Boot 3.2.5 / Java 21 web portal with responsive UI.

---

## Day-by-Day Development Log

### Day 1 — Project Initialization
- Installed JDK 21 LTS, Apache Maven, IntelliJ IDEA / VS Code.
- Bootstrapped Spring Boot 3.2.5 project using Spring Initializr.
- Selected dependencies: Spring Web, Spring Data JPA, Thymeleaf, Validation.

### Day 2 — GitHub Repository & Version Control
- Created GitHub repository: `Smart-Curriculum-Activity-Attendance-Web-Portal-`.
- Initialized local Git repository and linked to remote origin.
- Added `.gitignore` for Maven/IDE artifacts.
- Made first commit with project scaffold.

### Day 3 — Layered Package Architecture
- Created package structure:
  - `controller`, `service`, `service/impl`, `repository`
  - `entity`, `dto`, `exception`, `config`
- Added `package-info.java` Javadoc to each package.
- Stubbed `HomeController` returning portal status.

### Day 4 — MySQL Database Configuration
- Configured MySQL 8.0 datasource in `application.properties`.
- Set up HikariCP connection pool with tuned timeouts.
- Added H2 in-memory database for test isolation.
- Created `application-dev.properties` for development overrides.
- Created `database_setup.sql` with complete schema DDL.

### Day 5 — Student Entity & Database Mapping
- Implemented `Student` JPA entity -> `students` table.
- Fields: `rollNumber`, `firstName`, `lastName`, `email`, `phone`, `department`, `yearOfStudy`, `semester`, `section`, `gender`, `dateOfBirth`, `address`, `status`.
- Added `@PrePersist` / `@PreUpdate` lifecycle hooks for `createdAt` / `updatedAt`.
- Defined database indexes and unique constraint on `roll_number` and `email`.

### Day 6 — Faculty Entity & Database Mapping
- Implemented `Faculty` JPA entity -> `faculty` table.
- Fields: `employeeId`, `firstName`, `lastName`, `email`, `phone`, `department`, `designation`, `qualification`, `specialization`, `joiningDate`, `status`.
- Unique constraints: `employee_id`, `email`.

### Day 7 — Activity Entity & Database Mapping
- Implemented `Activity` JPA entity -> `activities` table.
- Fields: `activityCode`, `title`, `description`, `activityType`, `department`, `academicYear`, `semester`, `credits`, `venue`, `startDate`, `endDate`, `maxEnrollment`, `status`.
- `@ManyToOne` FK relationship to `Faculty` coordinator.
- Multi-column index on `department, semester`.

### Day 8 — Attendance Entity & Database Mapping
- Implemented `Attendance` JPA entity -> `attendance` table.
- Fields: `attendanceDate`, `status` (PRESENT/ABSENT/ON_DUTY), `remarks`, `markedByFaculty`.
- `@ManyToOne` FK relationships to `Student` and `Activity`.
- Composite unique constraint: `(student_id, activity_id, attendance_date)`.

### Day 9 — Student CRUD REST API
- Implemented `StudentRepository`, `StudentService`, `StudentServiceImpl`, `StudentController`.
- Full CRUD: POST/GET/GET/{id}/GET/roll/{rollNumber}/PUT/{id}/DELETE/{id} under `/api/students`.
- Jakarta Bean Validation on `StudentRequestDto`.

### Day 10 — Faculty CRUD REST API
- Implemented `FacultyRepository`, `FacultyService`, `FacultyController`.
- Full CRUD under `/api/faculty`.
- Filter by `department`, `designation`, `status`.

### Day 11 — Activity Management REST API
- Implemented `ActivityRepository`, `ActivityService`, `ActivityController`.
- Full CRUD under `/api/activities`.
- Faculty-Activity relationship wiring.

### Day 12 — Attendance REST API & Calculations
- Implemented `AttendanceRepository`, `AttendanceService`, `AttendanceController`.
- Attendance percentage calculation, eligibility evaluation (>75% ELIGIBLE, 65-75% WARNING, <65% CRITICAL).
- `AttendanceSummaryDto` with per-activity breakdown and eligibility badge.

### Day 13 — Frontend HTML & CSS Foundation
- Created `index.html`, `student.html`, `faculty.html`, `admin.html` under `templates/`.
- Created `style.css` (42 KB) with complete dark glassmorphic design system.
- CSS custom properties, glassmorphic cards, gradient buttons, keyframe animations.

### Day 14 — Frontend REST API Integration
- Created `app.js` (113 KB) — full JavaScript module for all portal views.
- `fetch()` calls wired to all REST endpoints.
- Dynamic table rendering, form submission handlers, toast notification engine.

### Day 15 — Student Dashboard
- Student attendance circular progress gauge, activity-wise breakdown, date-wise records.
- Eligibility status banner with colour-coded badge.

### Day 16 — Faculty Dashboard
- Mark Attendance form, Activity management panel, Today's Sessions stats.

### Day 17 — Admin Dashboard
- `AdminController` with `AdminStatsDto` aggregates.
- KPI cards, department analytics chart, management tables, activity log feed.

### Day 18 — Testing, Error Handling & Bug Fixes
- **181 tests** — Repository, Service, Controller, E2E layers. All passing.
- `GlobalExceptionHandler` covering 9 exception types.

### Day 19 — UI Polish, Responsiveness & UX
- Mobile-first breakpoints: 1200px, 992px, 768px, 480px.
- Animated mobile navigation drawer, responsive data tables with sticky headers.
- Back-to-top button, clipboard helpers, Escape hotkey, print stylesheet.
- `DataInitializer` seeding 4 students, 4 faculty, 4 activities, 6 attendance records.

### Day 20 — Final Verification, Screenshots, Wrap-up & Release
- All 181 tests passing — `mvn test` BUILD SUCCESS.
- Version bump: `pom.xml` -> `1.0.0`.
- UI screenshots generated and committed to `docs/screenshots/`.
- `CHANGELOG.md` and `RELEASE_NOTES.md` created.
- README updated: Day 20 marked Completed, screenshots gallery added.
- HomeController updated to reflect v1.0.0 release.
- Tagged `v1.0.0` for GitHub release.

---

[1.0.0]: https://github.com/srihariharand2025cse-debug/Smart-Curriculum-Activity-Attendance-Web-Portal-/releases/tag/v1.0.0
