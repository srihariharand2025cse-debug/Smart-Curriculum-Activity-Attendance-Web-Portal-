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
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/smartcurriculum/portal/
    │   │       ├── SmartCurriculumPortalApplication.java
    │   │       ├── controller/
    │   │       │   └── HomeController.java
    │   │       ├── entity/        # (Added in upcoming days)
    │   │       ├── repository/    # (Added in upcoming days)
    │   │       └── service/       # (Added in upcoming days)
    │   └── resources/
    │       ├── application.properties
    │       ├── static/            # Frontend CSS, JS, Images
    │       └── templates/         # HTML Views
    └── test/
        └── java/
            └── com/smartcurriculum/portal/
                └── SmartCurriculumPortalApplicationTests.java
```

---

## 🚀 Getting Started

### Prerequisites
* **Java Development Kit (JDK 21+)**
* **Git**
* **MySQL Server** (will be configured in Day 4)
* **VS Code** / IntelliJ IDEA / Eclipse

### Running the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/srihariharand2025cse-debug/Smart-Curriculum-Activity-Attendance-Web-Portal-.git
   cd Smart-Curriculum-Activity-Attendance-Web-Portal-
   ```

2. **Run using Maven**:
   ```powershell
   mvn spring-boot:run
   ```
   *Or open and run `SmartCurriculumPortalApplication.java` from your IDE.*

3. **Open the browser**:
   Visit [http://localhost:8080](http://localhost:8080) to verify that the portal server is running.

---

## 📅 20-Day Development Roadmap

* **Day 1**: Software setup and project initialization. *(Completed)*
* **Day 2**: GitHub repository setup and local repository connection. *(In Progress)*
* **Day 3**: Spring Boot layered package structure setup.
* **Day 4**: MySQL database configuration and connection setup.
* **Day 5**: Create Student entity and database mapping.
* **Day 6**: Create Faculty entity and database mapping.
* **Day 7**: Create Activity entity and database mapping.
* **Day 8**: Create Attendance entity and database mapping.
* **Day 9**: Implement Student CRUD operations (Repository, Service, Controller).
* **Day 10**: Implement Faculty CRUD operations.
* **Day 11**: Implement Curriculum Activity management APIs.
* **Day 12**: Implement Attendance marking and calculation APIs.
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
