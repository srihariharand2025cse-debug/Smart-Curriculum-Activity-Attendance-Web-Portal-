package com.smartcurriculum.portal.config;

import com.smartcurriculum.portal.entity.Activity;
import com.smartcurriculum.portal.entity.Attendance;
import com.smartcurriculum.portal.entity.Faculty;
import com.smartcurriculum.portal.entity.Student;
import com.smartcurriculum.portal.repository.ActivityRepository;
import com.smartcurriculum.portal.repository.AttendanceRepository;
import com.smartcurriculum.portal.repository.FacultyRepository;
import com.smartcurriculum.portal.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Data initializer component that bootstraps baseline sample records
 * when the portal starts with a blank database (e.g. in dev or demo mode).
 * Disabled in test profile to maintain test isolation.
 *
 * Seeds: 15 students, 10 faculty, 12 activities, and 60+ attendance records
 * across 5 departments (CSE, ECE, MECH, CIVIL, IT).
 */
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            log.info("Database already contains data, skipping sample data initialization.");
            return;
        }

        log.info("Initializing comprehensive sample data for Smart Curriculum Portal...");

        // =====================================================================
        // 1. SEED STUDENTS (15 across 5 departments)
        // =====================================================================

        // --- Computer Science and Engineering (CSE) ---
        Student s1 = createStudent("21CSE001", "Aarav", "Sharma", "aarav.sharma@college.edu", "9876543210",
                "Computer Science and Engineering", 3, 5, "A", "Male", LocalDate.of(2003, 5, 14), "123 Tech Campus Road, Coimbatore");
        Student s2 = createStudent("21CSE002", "Diya", "Patel", "diya.patel@college.edu", "9876543211",
                "Computer Science and Engineering", 3, 5, "A", "Female", LocalDate.of(2003, 9, 21), "45 Cyber Street, Coimbatore");
        Student s3 = createStudent("21CSE003", "Karthik", "Rajan", "karthik.rajan@college.edu", "9876543214",
                "Computer Science and Engineering", 3, 5, "B", "Male", LocalDate.of(2003, 7, 2), "99 Anna Nagar, Coimbatore");
        Student s4 = createStudent("22CSE010", "Priya", "Venkatesh", "priya.venkatesh@college.edu", "9876543215",
                "Computer Science and Engineering", 2, 3, "A", "Female", LocalDate.of(2004, 1, 18), "34 RS Puram, Coimbatore");

        // --- Electronics and Communication Engineering (ECE) ---
        Student s5 = createStudent("21ECE015", "Rohan", "Verma", "rohan.verma@college.edu", "9876543212",
                "Electronics and Communication", 2, 3, "B", "Male", LocalDate.of(2004, 3, 10), "78 Circuit Nagar, Coimbatore");
        Student s6 = createStudent("21ECE016", "Ananya", "Krishnan", "ananya.krishnan@college.edu", "9876543216",
                "Electronics and Communication", 2, 3, "A", "Female", LocalDate.of(2004, 6, 25), "12 Saibaba Colony, Coimbatore");
        Student s7 = createStudent("22ECE005", "Vishwa", "Nathan", "vishwa.nathan@college.edu", "9876543217",
                "Electronics and Communication", 1, 1, "A", "Male", LocalDate.of(2005, 2, 14), "56 Peelamedu, Coimbatore");

        // --- Mechanical Engineering ---
        Student s8 = createStudent("21MECH030", "Pooja", "Sundaram", "pooja.sundaram@college.edu", "9876543213",
                "Mechanical Engineering", 4, 7, "A", "Female", LocalDate.of(2002, 11, 5), "12 Foundry Avenue, Coimbatore");
        Student s9 = createStudent("21MECH031", "Arun", "Balaji", "arun.balaji@college.edu", "9876543218",
                "Mechanical Engineering", 4, 7, "A", "Male", LocalDate.of(2002, 8, 30), "88 Gandhipuram, Coimbatore");
        Student s10 = createStudent("22MECH008", "Deepa", "Lakshmi", "deepa.lakshmi@college.edu", "9876543219",
                "Mechanical Engineering", 3, 5, "B", "Female", LocalDate.of(2003, 12, 12), "23 Singanallur, Coimbatore");

        // --- Civil Engineering ---
        Student s11 = createStudent("21CIVIL020", "Harish", "Kumar", "harish.kumar@college.edu", "9876543220",
                "Civil Engineering", 3, 5, "A", "Male", LocalDate.of(2003, 4, 8), "67 Race Course, Coimbatore");
        Student s12 = createStudent("22CIVIL012", "Sneha", "Ramesh", "sneha.ramesh@college.edu", "9876543221",
                "Civil Engineering", 2, 3, "A", "Female", LocalDate.of(2004, 10, 19), "90 Kuniyamuthur, Coimbatore");

        // --- Information Technology (IT) ---
        Student s13 = createStudent("21IT040", "Naveen", "Prasad", "naveen.prasad@college.edu", "9876543222",
                "Information Technology", 3, 5, "A", "Male", LocalDate.of(2003, 6, 15), "11 Ukkadam, Coimbatore");
        Student s14 = createStudent("21IT041", "Kavitha", "Moorthy", "kavitha.moorthy@college.edu", "9876543223",
                "Information Technology", 3, 5, "A", "Female", LocalDate.of(2003, 8, 7), "44 Vadavalli, Coimbatore");
        Student s15 = createStudent("22IT015", "Sanjay", "Guru", "sanjay.guru@college.edu", "9876543224",
                "Information Technology", 2, 3, "B", "Male", LocalDate.of(2004, 5, 22), "33 Thudiyalur, Coimbatore");

        s1 = studentRepository.save(s1);
        s2 = studentRepository.save(s2);
        s3 = studentRepository.save(s3);
        s4 = studentRepository.save(s4);
        s5 = studentRepository.save(s5);
        s6 = studentRepository.save(s6);
        s7 = studentRepository.save(s7);
        s8 = studentRepository.save(s8);
        s9 = studentRepository.save(s9);
        s10 = studentRepository.save(s10);
        s11 = studentRepository.save(s11);
        s12 = studentRepository.save(s12);
        s13 = studentRepository.save(s13);
        s14 = studentRepository.save(s14);
        s15 = studentRepository.save(s15);

        log.info("Seeded 15 students across 5 departments.");

        // =====================================================================
        // 2. SEED FACULTY (10 across 5 departments)
        // =====================================================================

        // --- CSE Faculty ---
        Faculty f1 = createFaculty("FAC001", "Dr. Ravi", "Kumar", "ravi.kumar@college.edu", "9800000001",
                "Computer Science and Engineering", "Professor", "Artificial Intelligence", "Ph.D Computer Science", 15, "Male", LocalDate.of(2010, 7, 1));
        Faculty f2 = createFaculty("FAC002", "Dr. Meena", "Nair", "meena.nair@college.edu", "9800000002",
                "Computer Science and Engineering", "Associate Professor", "Data Structures & Algorithms", "Ph.D Information Technology", 10, "Female", LocalDate.of(2015, 6, 15));

        // --- ECE Faculty ---
        Faculty f3 = createFaculty("FAC003", "Mr. Suresh", "Babu", "suresh.babu@college.edu", "9800000003",
                "Electronics and Communication", "Assistant Professor", "VLSI Design", "M.E Electronics", 5, "Male", LocalDate.of(2020, 8, 1));
        Faculty f4 = createFaculty("FAC004", "Dr. Divya", "Raghavan", "divya.raghavan@college.edu", "9800000010",
                "Electronics and Communication", "Associate Professor", "Embedded Systems", "Ph.D ECE", 12, "Female", LocalDate.of(2014, 3, 10));

        // --- Mechanical Engineering Faculty ---
        Faculty f5 = createFaculty("FAC005", "Ms. Lakshmi", "Priya", "lakshmi.priya@college.edu", "9800000004",
                "Mechanical Engineering", "Assistant Professor", "Thermal Engineering", "M.E Mechanical", 3, "Female", LocalDate.of(2022, 7, 20));
        Faculty f6 = createFaculty("FAC006", "Dr. Senthil", "Murugan", "senthil.murugan@college.edu", "9800000005",
                "Mechanical Engineering", "Professor", "Manufacturing Technology", "Ph.D Mechanical", 18, "Male", LocalDate.of(2008, 1, 15));

        // --- Civil Engineering Faculty ---
        Faculty f7 = createFaculty("FAC007", "Dr. Venkat", "Subramanian", "venkat.subramanian@college.edu", "9800000006",
                "Civil Engineering", "Professor", "Structural Engineering", "Ph.D Civil Engineering", 20, "Male", LocalDate.of(2006, 5, 1));
        Faculty f8 = createFaculty("FAC008", "Ms. Rekha", "Devi", "rekha.devi@college.edu", "9800000007",
                "Civil Engineering", "Assistant Professor", "Environmental Engineering", "M.E Civil", 4, "Female", LocalDate.of(2021, 9, 1));

        // --- IT Faculty ---
        Faculty f9 = createFaculty("FAC009", "Dr. Manoj", "Pandian", "manoj.pandian@college.edu", "9800000008",
                "Information Technology", "Associate Professor", "Cyber Security", "Ph.D Information Security", 8, "Male", LocalDate.of(2018, 2, 1));
        Faculty f10 = createFaculty("FAC010", "Ms. Janani", "Selvan", "janani.selvan@college.edu", "9800000009",
                "Information Technology", "Assistant Professor", "Cloud Computing", "M.Tech IT", 3, "Female", LocalDate.of(2023, 6, 1));

        f1 = facultyRepository.save(f1);
        f2 = facultyRepository.save(f2);
        f3 = facultyRepository.save(f3);
        f4 = facultyRepository.save(f4);
        f5 = facultyRepository.save(f5);
        f6 = facultyRepository.save(f6);
        f7 = facultyRepository.save(f7);
        f8 = facultyRepository.save(f8);
        f9 = facultyRepository.save(f9);
        f10 = facultyRepository.save(f10);

        log.info("Seeded 10 faculty across 5 departments.");

        // =====================================================================
        // 3. SEED ACTIVITIES (12 across 5 departments)
        // =====================================================================

        // --- CSE Activities ---
        Activity a1 = createActivity("ACT-CS501-LAB", "Data Structures & Algorithms Laboratory",
                "Hands-on practical session implementing trees, graphs, and dynamic programming.",
                "LAB", "Computer Science and Engineering", "2025-2026", 5, 2, "Computer Lab 3", f2,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 60);

        Activity a2 = createActivity("ACT-CS502-LEC", "Artificial Intelligence & Machine Learning",
                "Core theory lecture on heuristic search, neural networks, and modern LLM foundations.",
                "LECTURE", "Computer Science and Engineering", "2025-2026", 5, 4, "Hall 204", f1,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 75);

        Activity a3 = createActivity("ACT-CS301-TUT", "Python Programming Workshop",
                "Beginner-to-intermediate workshop on Python, covering OOP, Flask, and data processing.",
                "WORKSHOP", "Computer Science and Engineering", "2025-2026", 3, 1, "Smart Lab 1", f1,
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 10, 30), 40);

        // --- ECE Activities ---
        Activity a4 = createActivity("ACT-EC301-LAB", "VLSI Design & Digital Simulation",
                "Hardware description language synthesis and FPGA verification session.",
                "LAB", "Electronics and Communication", "2025-2026", 3, 2, "VLSI Centre", f3,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 50);

        Activity a5 = createActivity("ACT-EC302-SEM", "IoT & Embedded Systems Seminar",
                "Industry-led seminar on IoT architectures, sensor networks, and edge computing.",
                "SEMINAR", "Electronics and Communication", "2025-2026", 3, 1, "Seminar Hall A", f4,
                LocalDate.of(2026, 9, 15), LocalDate.of(2026, 10, 15), 80);

        // --- Mechanical Engineering Activities ---
        Activity a6 = createActivity("ACT-ME701-SEM", "Renewable Energy & Thermal Systems Workshop",
                "Industry guest speaker workshop on solar photovoltaic and electric mobility.",
                "WORKSHOP", "Mechanical Engineering", "2025-2026", 7, 1, "Auditorium B", f5,
                LocalDate.of(2026, 9, 1), LocalDate.of(2026, 11, 30), 120);

        Activity a7 = createActivity("ACT-ME702-LAB", "CAD/CAM & 3D Printing Laboratory",
                "Practical sessions on SolidWorks, AutoCAD, and additive manufacturing techniques.",
                "LAB", "Mechanical Engineering", "2025-2026", 7, 2, "CAD Lab", f6,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 35);

        // --- Civil Engineering Activities ---
        Activity a8 = createActivity("ACT-CE501-LAB", "Geotechnical & Soil Testing Lab",
                "Soil classification, compaction, and shear strength testing practicals.",
                "LAB", "Civil Engineering", "2025-2026", 5, 2, "Geotechnical Lab", f7,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 45);

        Activity a9 = createActivity("ACT-CE302-FLD", "Field Survey & Levelling Camp",
                "5-day field camp covering total station survey, GPS mapping, and contour drawing.",
                "FIELD_WORK", "Civil Engineering", "2025-2026", 3, 2, "Campus Survey Ground", f8,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 5), 30);

        // --- IT Activities ---
        Activity a10 = createActivity("ACT-IT501-LAB", "Cyber Security & Ethical Hacking Lab",
                "Practical exercises on penetration testing, vulnerability scanning, and network forensics.",
                "LAB", "Information Technology", "2025-2026", 5, 2, "Networking Lab", f9,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 40);

        Activity a11 = createActivity("ACT-IT502-LEC", "Cloud Computing & DevOps",
                "Lecture series covering AWS, Docker, Kubernetes, CI/CD pipelines, and microservices.",
                "LECTURE", "Information Technology", "2025-2026", 5, 3, "Hall 301", f10,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 12, 15), 60);

        Activity a12 = createActivity("ACT-OPEN-HACK", "Inter-Department Hackathon 2026",
                "24-hour coding marathon open to all departments — build innovative solutions for real problems.",
                "HACKATHON", "Computer Science and Engineering", "2025-2026", 5, 1, "Main Auditorium", f1,
                LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 21), 200);

        a1 = activityRepository.save(a1);
        a2 = activityRepository.save(a2);
        a3 = activityRepository.save(a3);
        a4 = activityRepository.save(a4);
        a5 = activityRepository.save(a5);
        a6 = activityRepository.save(a6);
        a7 = activityRepository.save(a7);
        a8 = activityRepository.save(a8);
        a9 = activityRepository.save(a9);
        a10 = activityRepository.save(a10);
        a11 = activityRepository.save(a11);
        a12 = activityRepository.save(a12);

        log.info("Seeded 12 activities across 5 departments.");

        // =====================================================================
        // 4. SEED ATTENDANCE RECORDS (60+ records with varied patterns)
        // =====================================================================

        // ---- CSE Students in DSA Lab (a1) — multiple dates ----
        // Sep 10
        attendanceRepository.save(createAttendance(s1, a1, f2, LocalDate.of(2026, 9, 10), "PRESENT", "SESSION_1", "Attended DSA Lab on Binary Trees"));
        attendanceRepository.save(createAttendance(s2, a1, f2, LocalDate.of(2026, 9, 10), "PRESENT", "SESSION_1", "Attended DSA Lab on Binary Trees"));
        attendanceRepository.save(createAttendance(s3, a1, f2, LocalDate.of(2026, 9, 10), "ABSENT", "SESSION_1", "Reported sick — flu"));
        attendanceRepository.save(createAttendance(s4, a1, f2, LocalDate.of(2026, 9, 10), "PRESENT", "SESSION_1", "First lab session for 2nd year"));
        // Sep 17
        attendanceRepository.save(createAttendance(s1, a1, f2, LocalDate.of(2026, 9, 17), "PRESENT", "SESSION_1", "Completed graph traversal assignment"));
        attendanceRepository.save(createAttendance(s2, a1, f2, LocalDate.of(2026, 9, 17), "PRESENT", "SESSION_1", "Completed graph traversal assignment"));
        attendanceRepository.save(createAttendance(s3, a1, f2, LocalDate.of(2026, 9, 17), "PRESENT", "SESSION_1", "Recovered, attended lab"));
        attendanceRepository.save(createAttendance(s4, a1, f2, LocalDate.of(2026, 9, 17), "ABSENT", "SESSION_1", "Family emergency leave"));
        // Sep 24
        attendanceRepository.save(createAttendance(s1, a1, f2, LocalDate.of(2026, 9, 24), "PRESENT", "SESSION_1", "Dynamic programming practical"));
        attendanceRepository.save(createAttendance(s2, a1, f2, LocalDate.of(2026, 9, 24), "ABSENT", "SESSION_1", "College fest volunteering duty"));
        attendanceRepository.save(createAttendance(s3, a1, f2, LocalDate.of(2026, 9, 24), "PRESENT", "SESSION_1", "Dynamic programming practical"));
        // Oct 1
        attendanceRepository.save(createAttendance(s1, a1, f2, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_1", "Heap and priority queue lab"));
        attendanceRepository.save(createAttendance(s2, a1, f2, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_1", "Heap and priority queue lab"));
        attendanceRepository.save(createAttendance(s3, a1, f2, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_1", "Heap and priority queue lab"));

        // ---- CSE Students in AI/ML Lecture (a2) ----
        // Sep 11
        attendanceRepository.save(createAttendance(s1, a2, f1, LocalDate.of(2026, 9, 11), "PRESENT", "SESSION_2", "Attended AI lecture on A* Search"));
        attendanceRepository.save(createAttendance(s2, a2, f1, LocalDate.of(2026, 9, 11), "ABSENT", "SESSION_2", "Medical leave submitted"));
        attendanceRepository.save(createAttendance(s3, a2, f1, LocalDate.of(2026, 9, 11), "PRESENT", "SESSION_2", "Attended AI lecture on A* Search"));
        // Sep 18
        attendanceRepository.save(createAttendance(s1, a2, f1, LocalDate.of(2026, 9, 18), "PRESENT", "SESSION_2", "Neural networks introduction"));
        attendanceRepository.save(createAttendance(s2, a2, f1, LocalDate.of(2026, 9, 18), "PRESENT", "SESSION_2", "Neural networks introduction"));
        attendanceRepository.save(createAttendance(s3, a2, f1, LocalDate.of(2026, 9, 18), "PRESENT", "SESSION_2", "Neural networks introduction"));
        // Sep 25
        attendanceRepository.save(createAttendance(s1, a2, f1, LocalDate.of(2026, 9, 25), "PRESENT", "SESSION_2", "CNN architectures deep dive"));
        attendanceRepository.save(createAttendance(s2, a2, f1, LocalDate.of(2026, 9, 25), "PRESENT", "SESSION_2", "CNN architectures deep dive"));
        attendanceRepository.save(createAttendance(s3, a2, f1, LocalDate.of(2026, 9, 25), "ABSENT", "SESSION_2", "Sports day participation"));
        // Oct 2
        attendanceRepository.save(createAttendance(s1, a2, f1, LocalDate.of(2026, 10, 2), "ON_DUTY", "SESSION_2", "Paper presentation at inter-college symposium"));
        attendanceRepository.save(createAttendance(s2, a2, f1, LocalDate.of(2026, 10, 2), "PRESENT", "SESSION_2", "Transformer models lecture"));
        attendanceRepository.save(createAttendance(s3, a2, f1, LocalDate.of(2026, 10, 2), "PRESENT", "SESSION_2", "Transformer models lecture"));

        // ---- CSE Students in Python Workshop (a3) ----
        attendanceRepository.save(createAttendance(s4, a3, f1, LocalDate.of(2026, 9, 5), "PRESENT", "SESSION_1", "Python basics and setup"));
        attendanceRepository.save(createAttendance(s4, a3, f1, LocalDate.of(2026, 9, 12), "PRESENT", "SESSION_1", "OOP in Python"));
        attendanceRepository.save(createAttendance(s4, a3, f1, LocalDate.of(2026, 9, 19), "ABSENT", "SESSION_1", "Missed due to mid-semester exam prep"));
        attendanceRepository.save(createAttendance(s4, a3, f1, LocalDate.of(2026, 9, 26), "PRESENT", "SESSION_1", "Flask web framework session"));

        // ---- ECE Students in VLSI Lab (a4) ----
        attendanceRepository.save(createAttendance(s5, a4, f3, LocalDate.of(2026, 9, 12), "PRESENT", "SESSION_1", "Completed VLSI simulation module 1"));
        attendanceRepository.save(createAttendance(s6, a4, f3, LocalDate.of(2026, 9, 12), "PRESENT", "SESSION_1", "Completed VLSI simulation module 1"));
        attendanceRepository.save(createAttendance(s7, a4, f3, LocalDate.of(2026, 9, 12), "ABSENT", "SESSION_1", "First year orientation clash"));
        // Sep 19
        attendanceRepository.save(createAttendance(s5, a4, f3, LocalDate.of(2026, 9, 19), "PRESENT", "SESSION_1", "Verilog HDL coding exercise"));
        attendanceRepository.save(createAttendance(s6, a4, f3, LocalDate.of(2026, 9, 19), "ABSENT", "SESSION_1", "Attended departmental seminar"));
        attendanceRepository.save(createAttendance(s7, a4, f3, LocalDate.of(2026, 9, 19), "PRESENT", "SESSION_1", "First VLSI lab session attended"));
        // Sep 26
        attendanceRepository.save(createAttendance(s5, a4, f3, LocalDate.of(2026, 9, 26), "PRESENT", "SESSION_1", "FPGA board programming"));
        attendanceRepository.save(createAttendance(s6, a4, f3, LocalDate.of(2026, 9, 26), "PRESENT", "SESSION_1", "FPGA board programming"));
        attendanceRepository.save(createAttendance(s7, a4, f3, LocalDate.of(2026, 9, 26), "PRESENT", "SESSION_1", "FPGA board programming"));

        // ---- ECE Students in IoT Seminar (a5) ----
        attendanceRepository.save(createAttendance(s5, a5, f4, LocalDate.of(2026, 9, 20), "PRESENT", "SESSION_1", "IoT architecture overview by industry expert"));
        attendanceRepository.save(createAttendance(s6, a5, f4, LocalDate.of(2026, 9, 20), "PRESENT", "SESSION_1", "IoT architecture overview by industry expert"));
        attendanceRepository.save(createAttendance(s7, a5, f4, LocalDate.of(2026, 9, 20), "PRESENT", "SESSION_1", "IoT architecture overview by industry expert"));

        // ---- Mechanical Students in Renewable Energy Workshop (a6) ----
        attendanceRepository.save(createAttendance(s8, a6, f5, LocalDate.of(2026, 9, 15), "ON_DUTY", "SESSION_1", "Representing college at Renewable Energy Summit"));
        attendanceRepository.save(createAttendance(s9, a6, f5, LocalDate.of(2026, 9, 15), "PRESENT", "SESSION_1", "Solar panel efficiency analysis session"));
        attendanceRepository.save(createAttendance(s10, a6, f5, LocalDate.of(2026, 9, 15), "PRESENT", "SESSION_1", "Solar panel efficiency analysis session"));
        // Sep 22
        attendanceRepository.save(createAttendance(s8, a6, f5, LocalDate.of(2026, 9, 22), "PRESENT", "SESSION_1", "Electric vehicle drivetrain workshop"));
        attendanceRepository.save(createAttendance(s9, a6, f5, LocalDate.of(2026, 9, 22), "PRESENT", "SESSION_1", "Electric vehicle drivetrain workshop"));
        attendanceRepository.save(createAttendance(s10, a6, f5, LocalDate.of(2026, 9, 22), "ABSENT", "SESSION_1", "Personal leave"));

        // ---- Mechanical Students in CAD/CAM Lab (a7) ----
        attendanceRepository.save(createAttendance(s8, a7, f6, LocalDate.of(2026, 9, 14), "PRESENT", "SESSION_2", "SolidWorks part modeling"));
        attendanceRepository.save(createAttendance(s9, a7, f6, LocalDate.of(2026, 9, 14), "PRESENT", "SESSION_2", "SolidWorks part modeling"));
        attendanceRepository.save(createAttendance(s8, a7, f6, LocalDate.of(2026, 9, 21), "PRESENT", "SESSION_2", "Assembly modeling and simulation"));
        attendanceRepository.save(createAttendance(s9, a7, f6, LocalDate.of(2026, 9, 21), "ABSENT", "SESSION_2", "Sick leave — fever"));
        attendanceRepository.save(createAttendance(s8, a7, f6, LocalDate.of(2026, 9, 28), "PRESENT", "SESSION_2", "3D printing practical"));
        attendanceRepository.save(createAttendance(s9, a7, f6, LocalDate.of(2026, 9, 28), "PRESENT", "SESSION_2", "3D printing practical"));

        // ---- Civil Students in Geotechnical Lab (a8) ----
        attendanceRepository.save(createAttendance(s11, a8, f7, LocalDate.of(2026, 9, 13), "PRESENT", "SESSION_1", "Soil classification experiment"));
        attendanceRepository.save(createAttendance(s12, a8, f7, LocalDate.of(2026, 9, 13), "PRESENT", "SESSION_1", "Soil classification experiment"));
        attendanceRepository.save(createAttendance(s11, a8, f7, LocalDate.of(2026, 9, 20), "PRESENT", "SESSION_1", "Compaction test — Proctor method"));
        attendanceRepository.save(createAttendance(s12, a8, f7, LocalDate.of(2026, 9, 20), "ABSENT", "SESSION_1", "Bus missed — late arrival"));
        attendanceRepository.save(createAttendance(s11, a8, f7, LocalDate.of(2026, 9, 27), "ABSENT", "SESSION_1", "College cultural fest duty"));
        attendanceRepository.save(createAttendance(s12, a8, f7, LocalDate.of(2026, 9, 27), "PRESENT", "SESSION_1", "Shear strength test"));

        // ---- Civil Students in Field Survey (a9) ----
        attendanceRepository.save(createAttendance(s11, a9, f8, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_1", "Day 1: Total station survey basics"));
        attendanceRepository.save(createAttendance(s12, a9, f8, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_1", "Day 1: Total station survey basics"));
        attendanceRepository.save(createAttendance(s11, a9, f8, LocalDate.of(2026, 10, 2), "PRESENT", "SESSION_1", "Day 2: GPS mapping and coordinates"));
        attendanceRepository.save(createAttendance(s12, a9, f8, LocalDate.of(2026, 10, 2), "PRESENT", "SESSION_1", "Day 2: GPS mapping and coordinates"));

        // ---- IT Students in Cyber Security Lab (a10) ----
        attendanceRepository.save(createAttendance(s13, a10, f9, LocalDate.of(2026, 9, 16), "PRESENT", "SESSION_1", "Nmap and network scanning lab"));
        attendanceRepository.save(createAttendance(s14, a10, f9, LocalDate.of(2026, 9, 16), "PRESENT", "SESSION_1", "Nmap and network scanning lab"));
        attendanceRepository.save(createAttendance(s15, a10, f9, LocalDate.of(2026, 9, 16), "ABSENT", "SESSION_1", "2nd year timetable clash"));
        // Sep 23
        attendanceRepository.save(createAttendance(s13, a10, f9, LocalDate.of(2026, 9, 23), "PRESENT", "SESSION_1", "Wireshark packet analysis"));
        attendanceRepository.save(createAttendance(s14, a10, f9, LocalDate.of(2026, 9, 23), "ABSENT", "SESSION_1", "Medical appointment"));
        attendanceRepository.save(createAttendance(s15, a10, f9, LocalDate.of(2026, 9, 23), "PRESENT", "SESSION_1", "Wireshark packet analysis"));
        // Sep 30
        attendanceRepository.save(createAttendance(s13, a10, f9, LocalDate.of(2026, 9, 30), "PRESENT", "SESSION_1", "SQL injection prevention techniques"));
        attendanceRepository.save(createAttendance(s14, a10, f9, LocalDate.of(2026, 9, 30), "PRESENT", "SESSION_1", "SQL injection prevention techniques"));
        attendanceRepository.save(createAttendance(s15, a10, f9, LocalDate.of(2026, 9, 30), "PRESENT", "SESSION_1", "SQL injection prevention techniques"));

        // ---- IT Students in Cloud Computing Lecture (a11) ----
        attendanceRepository.save(createAttendance(s13, a11, f10, LocalDate.of(2026, 9, 17), "PRESENT", "SESSION_2", "AWS fundamentals overview"));
        attendanceRepository.save(createAttendance(s14, a11, f10, LocalDate.of(2026, 9, 17), "PRESENT", "SESSION_2", "AWS fundamentals overview"));
        attendanceRepository.save(createAttendance(s13, a11, f10, LocalDate.of(2026, 9, 24), "ABSENT", "SESSION_2", "Participated in coding contest"));
        attendanceRepository.save(createAttendance(s14, a11, f10, LocalDate.of(2026, 9, 24), "PRESENT", "SESSION_2", "Docker containerization lecture"));
        attendanceRepository.save(createAttendance(s13, a11, f10, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_2", "Kubernetes orchestration demo"));
        attendanceRepository.save(createAttendance(s14, a11, f10, LocalDate.of(2026, 10, 1), "PRESENT", "SESSION_2", "Kubernetes orchestration demo"));

        log.info("Seeded 60+ attendance records with varied PRESENT/ABSENT/ON_DUTY patterns.");
        log.info("=== Data initialization complete: 15 students, 10 faculty, 12 activities, 60+ attendance records ===");
    }

    // =========================================================================
    // Helper Factory Methods
    // =========================================================================

    private Student createStudent(String roll, String first, String last, String email, String phone,
                                  String dept, int year, int sem, String sec, String gender, LocalDate dob, String addr) {
        Student s = new Student();
        s.setRollNumber(roll);
        s.setFirstName(first);
        s.setLastName(last);
        s.setEmail(email);
        s.setPhoneNumber(phone);
        s.setDepartment(dept);
        s.setYearOfStudy(year);
        s.setSemester(sem);
        s.setSection(sec);
        s.setGender(gender);
        s.setDateOfBirth(dob);
        s.setAddress(addr);
        s.setStatus("ACTIVE");
        return s;
    }

    private Faculty createFaculty(String empId, String first, String last, String email, String phone,
                                  String dept, String designation, String spec, String qual, int exp, String gender, LocalDate doj) {
        Faculty f = new Faculty();
        f.setEmployeeId(empId);
        f.setFirstName(first);
        f.setLastName(last);
        f.setEmail(email);
        f.setPhoneNumber(phone);
        f.setDepartment(dept);
        f.setDesignation(designation);
        f.setSpecialization(spec);
        f.setQualification(qual);
        f.setExperienceYears(exp);
        f.setGender(gender);
        f.setDateOfJoining(doj);
        f.setStatus("ACTIVE");
        return f;
    }

    private Activity createActivity(String code, String title, String desc, String type, String dept,
                                    String acaYear, int sem, int credits, String venue, Faculty faculty,
                                    LocalDate startDate, LocalDate endDate, int maxEnrollment) {
        Activity a = new Activity();
        a.setActivityCode(code);
        a.setTitle(title);
        a.setDescription(desc);
        a.setActivityType(type);
        a.setDepartment(dept);
        a.setAcademicYear(acaYear);
        a.setSemester(sem);
        a.setCredits(credits);
        a.setVenue(venue);
        a.setFaculty(faculty);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setMaxEnrollment(maxEnrollment);
        a.setStatus("ACTIVE");
        return a;
    }

    private Attendance createAttendance(Student student, Activity activity, Faculty faculty,
                                        LocalDate date, String status, String sessionSlot, String remarks) {
        Attendance att = new Attendance();
        att.setStudent(student);
        att.setActivity(activity);
        att.setMarkedByFaculty(faculty);
        att.setAttendanceDate(date);
        att.setStatus(status);
        att.setSessionSlot(sessionSlot);
        att.setRemarks(remarks);
        return att;
    }
}
