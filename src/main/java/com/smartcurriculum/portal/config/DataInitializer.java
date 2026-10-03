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

        log.info("Initializing baseline sample data for Smart Curriculum Portal...");

        // 1. Seed Students
        Student s1 = createStudent("21CSE001", "Aarav", "Sharma", "aarav.sharma@college.edu", "9876543210",
                "Computer Science and Engineering", 3, 5, "A", "Male", LocalDate.of(2003, 5, 14), "123 Tech Campus Road, Coimbatore");
        Student s2 = createStudent("21CSE002", "Diya", "Patel", "diya.patel@college.edu", "9876543211",
                "Computer Science and Engineering", 3, 5, "A", "Female", LocalDate.of(2003, 9, 21), "45 Cyber Street, Coimbatore");
        Student s3 = createStudent("21ECE015", "Rohan", "Verma", "rohan.verma@college.edu", "9876543212",
                "Electronics and Communication", 2, 3, "B", "Male", LocalDate.of(2004, 3, 10), "78 Circuit Nagar, Coimbatore");
        Student s4 = createStudent("21MECH030", "Pooja", "Sundaram", "pooja.sundaram@college.edu", "9876543213",
                "Mechanical Engineering", 4, 7, "A", "Female", LocalDate.of(2002, 11, 5), "12 Foundry Avenue, Coimbatore");

        s1 = studentRepository.save(s1);
        s2 = studentRepository.save(s2);
        s3 = studentRepository.save(s3);
        s4 = studentRepository.save(s4);

        // 2. Seed Faculty
        Faculty f1 = createFaculty("FAC001", "Dr. Ravi", "Kumar", "ravi.kumar@college.edu", "9800000001",
                "Computer Science and Engineering", "Professor", "Artificial Intelligence", "Ph.D Computer Science", 15, "Male", LocalDate.of(2010, 7, 1));
        Faculty f2 = createFaculty("FAC002", "Dr. Meena", "Nair", "meena.nair@college.edu", "9800000002",
                "Computer Science and Engineering", "Associate Professor", "Data Structures & Algorithms", "Ph.D Information Technology", 10, "Female", LocalDate.of(2015, 6, 15));
        Faculty f3 = createFaculty("FAC003", "Mr. Suresh", "Babu", "suresh.babu@college.edu", "9800000003",
                "Electronics and Communication", "Assistant Professor", "VLSI Design", "M.E Electronics", 5, "Male", LocalDate.of(2020, 8, 1));
        Faculty f4 = createFaculty("FAC004", "Ms. Lakshmi", "Priya", "lakshmi.priya@college.edu", "9800000004",
                "Mechanical Engineering", "Assistant Professor", "Thermal Engineering", "M.E Mechanical", 3, "Female", LocalDate.of(2022, 7, 20));

        f1 = facultyRepository.save(f1);
        f2 = facultyRepository.save(f2);
        f3 = facultyRepository.save(f3);
        f4 = facultyRepository.save(f4);

        // 3. Seed Activities
        Activity a1 = createActivity("ACT-CS501-LAB", "Data Structures & Algorithms Laboratory",
                "Hands-on practical session implementing trees, graphs, and dynamic programming.",
                "LAB", "Computer Science and Engineering", "2025-2026", 5, 2, "Computer Lab 3", f2, 60);
        Activity a2 = createActivity("ACT-CS502-LEC", "Artificial Intelligence & Machine Learning",
                "Core theory lecture on heuristic search, neural networks, and modern LLM foundations.",
                "LECTURE", "Computer Science and Engineering", "2025-2026", 5, 4, "Hall 204", f1, 75);
        Activity a3 = createActivity("ACT-EC301-LAB", "VLSI Design & Digital Simulation",
                "Hardware description language synthesis and FPGA verification session.",
                "LAB", "Electronics and Communication", "2025-2026", 3, 2, "VLSI Centre", f3, 50);
        Activity a4 = createActivity("ACT-ME701-SEM", "Renewable Energy & Thermal Systems Workshop",
                "Industry guest speaker workshop on solar photovoltaic and electric mobility.",
                "WORKSHOP", "Mechanical Engineering", "2025-2026", 7, 1, "Auditorium B", f4, 120);

        a1 = activityRepository.save(a1);
        a2 = activityRepository.save(a2);
        a3 = activityRepository.save(a3);
        a4 = activityRepository.save(a4);

        // 4. Seed Attendance Records
        attendanceRepository.save(createAttendance(s1, a1, f2, LocalDate.of(2026, 9, 10), "PRESENT", "SESSION_1", "Attended DSA Lab on Binary Trees"));
        attendanceRepository.save(createAttendance(s2, a1, f2, LocalDate.of(2026, 9, 10), "PRESENT", "SESSION_1", "Attended DSA Lab on Binary Trees"));
        attendanceRepository.save(createAttendance(s1, a2, f1, LocalDate.of(2026, 9, 11), "PRESENT", "SESSION_2", "Attended AI lecture on A* Search"));
        attendanceRepository.save(createAttendance(s2, a2, f1, LocalDate.of(2026, 9, 11), "ABSENT", "SESSION_2", "Medical leave submitted"));
        attendanceRepository.save(createAttendance(s3, a3, f3, LocalDate.of(2026, 9, 12), "PRESENT", "SESSION_1", "Completed VLSI simulation module 1"));
        attendanceRepository.save(createAttendance(s4, a4, f4, LocalDate.of(2026, 9, 15), "ON_DUTY", "SESSION_1", "Representing college at Renewable Energy Summit"));

        log.info("Successfully seeded 4 students, 4 faculty, 4 activities, and 6 attendance logs.");
    }

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
                                    String acaYear, int sem, int credits, String venue, Faculty faculty, int maxEnrollment) {
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
