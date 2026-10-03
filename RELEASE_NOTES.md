# Release Notes — v1.0.0

**Smart Curriculum Activity & Attendance Web Portal**
**Release Date**: 2026-10-03
**Version**: 1.0.0 — Initial Production Release

---

## 🎉 What's in This Release

This is the **first stable release** of the Smart Curriculum Activity & Attendance Web Portal, the outcome of a structured 20-day development sprint. The portal is a full-stack, role-based Java web application built for educational institutions to manage curriculum activities and student attendance.

---

## ✅ Build & Test Status

| Metric | Result |
|:---|:---|
| Build Tool | Apache Maven 3.9.x |
| Java Version | 21.0.10 LTS |
| Spring Boot | 3.2.5 |
| Total Tests | **181** |
| Failures | **0** |
| Errors | **0** |
| Build Status | ✅ **SUCCESS** |

---

## 🚀 Features Delivered

### Backend (Spring Boot 3.2.5 / Java 21)

#### Data Model
- **4 JPA Entities**: `Student`, `Faculty`, `Activity`, `Attendance`
- **MySQL** primary database with full schema DDL (`database_setup.sql`)
- **H2 in-memory** database for test isolation
- HikariCP connection pool (pool size 10, tuned timeouts)

#### REST API Endpoints

| Domain | Endpoints | Methods |
|:---|:---|:---|
| Student | `/api/students` | POST, GET, GET/{id}, GET/roll/{rollNumber}, PUT/{id}, DELETE/{id} |
| Faculty | `/api/faculty` | POST, GET, GET/{id}, GET/employee/{empId}, PUT/{id}, DELETE/{id} |
| Activity | `/api/activities` | POST, GET, GET/{id}, GET/code/{code}, PUT/{id}, DELETE/{id} |
| Attendance | `/api/attendance` | POST, GET, GET/{id}, GET/student/{id}, GET/activity/{id}, PUT/{id}, DELETE/{id} |
| Admin Stats | `/api/admin/stats` | GET |
| Status | `/api/status`, `/api/db-status` | GET |
| Summaries | `/api/students/summary`, `/api/faculty/summary`, `/api/activities/summary`, `/api/attendance/summary` | GET |

#### Business Logic
- Attendance percentage calculation per student per activity
- Eligibility evaluation: **ELIGIBLE** (>75%), **WARNING** (65–75%), **CRITICAL** (<65%)
- Bulk attendance marking
- Admin KPI statistics aggregation

#### Validation & Error Handling
- Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Size`, `@Min`, `@Max`) on all DTOs
- `GlobalExceptionHandler` (`@RestControllerAdvice`) handling 9 exception types
- Standardized `ApiResponse<T>` wrapper for all API responses

### Frontend (HTML5 + Vanilla CSS + JavaScript)

#### Pages
| Page | URL | Description |
|:---|:---|:---|
| Portal Hub | `/` or `/portal` | Landing page with role cards and live KPI counters |
| Student Dashboard | `/portal/student` | Attendance gauge, activity breakdown, record table |
| Faculty Dashboard | `/portal/faculty` | Mark attendance, manage activities, session stats |
| Admin Control Center | `/portal/admin` | KPI cards, management tables, analytics chart |

#### UI Highlights
- 🌙 **Dark glassmorphic design** with cyan/purple/gold accent palette
- 📱 **Mobile-first responsive** — breakpoints at 1200px, 992px, 768px, 480px
- 🍔 **Animated mobile navigation drawer** (hamburger toggle)
- 📊 **Circular attendance gauge**, horizontal activity bars, department bar chart
- 🔔 **Toast notification engine** (Success/Error/Warning/Info with auto-dismiss)
- 📋 **Copy-to-clipboard** for roll numbers, employee IDs, API endpoints
- ⬆️ **Floating back-to-top button**
- ⌨️ **Keyboard accessibility** — Escape to dismiss modals
- 🖨️ **Print stylesheet** for attendance cards
- 📜 **Sticky table headers** with backdrop blur

---

## 📁 Project Structure Highlights

```
java project/
├── CHANGELOG.md          ← NEW (Day 20)
├── RELEASE_NOTES.md      ← NEW (Day 20)
├── README.md             ← UPDATED (Day 20)
├── pom.xml               ← v1.0.0 (Day 20)
├── docs/
│   └── screenshots/      ← NEW — 4 UI screenshots (Day 20)
│       ├── 01_home_portal.jpg
│       ├── 02_student_dashboard.jpg
│       ├── 03_faculty_dashboard.jpg
│       └── 04_admin_dashboard.jpg
└── src/
    ├── main/
    │   ├── java/com/smartcurriculum/portal/
    │   │   ├── controller/   (7 controllers)
    │   │   ├── service/      (4 interfaces + 4 impls)
    │   │   ├── repository/   (4 repositories)
    │   │   ├── entity/       (4 entities)
    │   │   ├── dto/          (11 DTOs + ApiResponse)
    │   │   ├── exception/    (GlobalExceptionHandler + 3 exceptions)
    │   │   └── config/       (DataInitializer)
    │   └── resources/
    │       ├── templates/    (index, student, faculty, admin HTML)
    │       └── static/       (style.css 42KB, app.js 113KB)
    └── test/               (181 tests across 18 test classes)
```

---

## 🗄️ Database Setup

```bash
# Initialize MySQL schema
mysql -u root -p < src/main/resources/database_setup.sql

# Set credentials if non-default
$env:DB_USERNAME = "your_username"
$env:DB_PASSWORD = "your_password"
```

---

## ▶️ Running the Application

```powershell
# Set JAVA_HOME (if not already set)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.10"

# Run with Maven Wrapper
.\mvnw.cmd spring-boot:run
```

Then open: [http://localhost:8080](http://localhost:8080)

---

## 🧪 Running Tests

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.10"
.\mvnw.cmd test
```

Expected output:
```
[INFO] Tests run: 181, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 📸 Screenshots

| View | Screenshot |
|:---|:---|
| Portal Hub | `docs/screenshots/01_home_portal.jpg` |
| Student Dashboard | `docs/screenshots/02_student_dashboard.jpg` |
| Faculty Dashboard | `docs/screenshots/03_faculty_dashboard.jpg` |
| Admin Control Center | `docs/screenshots/04_admin_dashboard.jpg` |

---

## 🔗 GitHub

- **Repository**: https://github.com/srihariharand2025cse-debug/Smart-Curriculum-Activity-Attendance-Web-Portal-
- **Release Tag**: `v1.0.0`
- **License**: Educational use

---

## 👨‍💻 Author

**Sri Hariharan D** — Computer Science & Engineering, 2025 Batch
