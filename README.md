# Smart Curriculum Activity & Attendance Web Portal

A comprehensive, role-based Java web application designed to streamline academic curriculum activity tracking and student attendance management.

---

## 📌 Project Overview

In educational institutions, managing curriculum activities, tracking co-curricular/extra-curricular participations, and maintaining accurate attendance records are critical tasks. The **Smart Curriculum Activity & Attendance Web Portal** provides a centralized, easy-to-use platform for administrators, faculty members, and students to record, monitor, and evaluate student involvement and attendance.

---

## 👥 User Roles & Core Features

### 1. 🛡️ Admin Module
* **Student Management**: Add, update, and manage student profiles and records.
* **Faculty Management**: Add and assign faculty members to courses and activities.
* **Activity Oversight**: View, approve, and manage institution-wide curriculum activities.
* **Attendance Oversight**: Monitor department-wide and class-wide attendance analytics.

### 2. 👨‍🏫 Faculty Module
* **Authentication**: Secure login and profile management.
* **Attendance Marking**: Daily/lecture-wise attendance marking for assigned students.
* **Activity Management**: Create and schedule curriculum activities, workshops, and seminars.
* **Participation Tracking**: Review and update student participation in activities.

### 3. 🎓 Student Module
* **Authentication**: Secure student login.
* **Attendance Dashboard**: View real-time attendance percentage and detailed date-wise attendance logs.
* **Activity Exploration**: Discover upcoming academic and extracurricular activities.
* **Participation Records**: Check participation status and earned activity credits/certificates.

---

## 💻 Technology Stack

* **Backend**: Java 21 LTS, Spring Boot 3.2.5 (Spring Web, Spring Data JPA)
* **Database**: MySQL Server
* **Frontend**: HTML5, CSS3, Modern Vanilla JavaScript
* **Build & Dependency Management**: Apache Maven
* **Version Control**: Git & GitHub

---

## 📁 Project Structure

```text
java project/
├── .gitignore
├── mvnw / mvnw.cmd
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/smartcurriculum/portal/
    │   │       ├── SmartCurriculumPortalApplication.java
    │   │       ├── controller/        # REST & Web endpoints (HomeController)
    │   │       ├── service/           # Business logic interfaces
    │   │       │   └── impl/          # Business logic implementations
    │   │       ├── repository/        # Spring Data JPA repositories
    │   │       ├── entity/            # JPA entities (Student, Faculty, Activity, Attendance)
    │   │       ├── dto/               # Data Transfer Objects & ApiResponse wrapper
    │   │       └── exception/         # Custom exceptions & GlobalExceptionHandler
    │   └── resources/
    │       ├── application.properties
    │       ├── static/                # Static assets (css/style.css, js/app.js, images)
    │       └── templates/             # HTML Views (index.html)
    └── test/
        └── java/
            └── com/smartcurriculum/portal/
                └── SmartCurriculumPortalApplicationTests.java
```

---

## 🏛️ Layered Architecture Overview

The application follows the enterprise Spring Boot layered architecture:

1. **Controller Layer (`com.smartcurriculum.portal.controller`)**:
   - Handles incoming HTTP requests, performs parameter validation, and produces JSON responses or views.
2. **Service Layer (`com.smartcurriculum.portal.service` & `impl`)**:
   - Encapsulates business logic, data validation, and manages transaction boundaries.
3. **Repository Layer (`com.smartcurriculum.portal.repository`)**:
   - Data access abstraction leveraging Spring Data JPA (`JpaRepository`) for database CRUD operations.
4. **Entity / Domain Layer (`com.smartcurriculum.portal.entity`)**:
   - Object-relational mapping (ORM) entities mapped directly to MySQL database tables.
5. **DTO Layer (`com.smartcurriculum.portal.dto`)**:
   - Standardized `ApiResponse<T>` wrapper and decoupled request/response data carriers.
6. **Exception Layer (`com.smartcurriculum.portal.exception`)**:
   - Global exception handling (`@RestControllerAdvice`) delivering unified, client-friendly error structures.

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK 21+)**
* **Git**
* **MySQL Server (8.0+)**
* **VS Code** / IntelliJ IDEA / Eclipse

### Database Setup (Day 5, 6, 7 & 8)

1. Ensure MySQL Server is running locally on port `3306`.
2. Initialize the database using the provided SQL script:
   ```bash
   mysql -u root -p < src/main/resources/database_setup.sql
   ```
   *(Or create it in MySQL Workbench: `CREATE DATABASE IF NOT EXISTS smart_curriculum_db;`)*
3. If your MySQL credentials differ from `root` / `root`, set environment variables or edit `application.properties`:
   ```powershell
   $env:DB_USERNAME = "your_username"
   $env:DB_PASSWORD = "your_password"
   ```

### Running the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/srihariharand2025cse-debug/Smart-Curriculum-Activity-Attendance-Web-Portal-.git
   cd Smart-Curriculum-Activity-Attendance-Web-Portal-
   ```

2. **Run using Maven Wrapper**:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```
   *Or open and run `SmartCurriculumPortalApplication.java` from your IDE.*

3. **Open the browser**:
   - Web Portal UI: [http://localhost:8080](http://localhost:8080)
   - Student REST APIs: [http://localhost:8080/api/students](http://localhost:8080/api/students)
   - API Status: [http://localhost:8080/api/status](http://localhost:8080/api/status)
   - Database Connectivity Status: [http://localhost:8080/api/db-status](http://localhost:8080/api/db-status)
   - Student Summary: [http://localhost:8080/api/students/summary](http://localhost:8080/api/students/summary)
   - Faculty Summary: [http://localhost:8080/api/faculty/summary](http://localhost:8080/api/faculty/summary)
   - Activity Summary: [http://localhost:8080/api/activities/summary](http://localhost:8080/api/activities/summary)
   - Attendance Summary: [http://localhost:8080/api/attendance/summary](http://localhost:8080/api/attendance/summary)

---

## 📡 Student REST API Endpoints (Day 9)

| Method | Endpoint | Description | Status Code |
|:-------|:---------|:------------|:------------|
| `POST` | `/api/students` | Register a new student | `201 Created` |
| `GET` | `/api/students` | Retrieve all students (filters: `department`, `yearOfStudy`, `status`) | `200 OK` |
| `GET` | `/api/students/{id}` | Retrieve student by primary ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/students/roll/{rollNumber}` | Retrieve student by roll number | `200 OK` / `404 Not Found` |
| `PUT` | `/api/students/{id}` | Update student details by ID | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/students/{id}` | Delete student by ID | `200 OK` / `404 Not Found` |

---

## 📅 20-Day Development Roadmap

* **Day 1**: Software setup and project initialization. *(Completed)*
* **Day 2**: GitHub repository setup and local repository connection. *(Completed)*
* **Day 3**: Spring Boot layered package structure setup. *(Completed)*
* **Day 4**: MySQL database configuration and connection setup. *(Completed)*
* **Day 5**: Create Student entity and database mapping. *(Completed)*
* **Day 6**: Create Faculty entity and database mapping. *(Completed)*
* **Day 7**: Create Activity entity and database mapping. *(Completed)*
* **Day 8**: Create Attendance entity and database mapping. *(Completed)*
* **Day 9**: Implement Student CRUD operations (Repository, Service, Controller). *(Completed)*
* **Day 10**: Implement Faculty CRUD operations. *(Completed)*
* **Day 11**: Implement Curriculum Activity management APIs. *(Completed)*
* **Day 12**: Implement Attendance marking and calculation APIs.
  - Create REST endpoints for attendance marking.
  - Implement service methods for attendance calculation (percentage, total days).
  - Add unit and integration tests.
* **Day 13**: Build frontend pages (HTML & modern CSS layout).
* **Day 14**: Connect frontend forms and views with Spring Boot REST APIs.
* **Day 15**: Implement Student Dashboard with attendance visual metrics.
* **Day 16**: Implement Faculty Dashboard for attendance & activity logging.
* **Day 17**: Implement Admin Dashboard with centralized stats and controls.
* **Day 18**: End-to-end testing, error handling, and bug fixing.
* **Day 19**: Polish UI design, responsiveness, and user experience.
* **Day 20**: Final verification, screenshots, project wrap-up, and release.

---

## 📄 License
This project is developed for educational purposes.
