package com.university.grievance.services;// UserService.java

import com.university.grievance.Repository.UserRepository;
import com.university.grievance.entity.User;
import com.university.grievance.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;



    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getStaffByDepartment(String department) {
        return userRepository.findByDepartmentAndRole(department, Role.STAFF);
    }

    public User getHODByDepartment(String department) {
        return userRepository.findByDepartmentAndRoleAndActiveTrue(department, Role.HOD).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean existsByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId).isPresent();
    }

    // NEW: Student Registration Method
    public User registerStudent(String fullName, String email, String phone, String studentId) {
        // Generate username from email
        String username = email.split("@")[0] + "_" + System.currentTimeMillis() % 10000;

        // Generate random password
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Create new student user
        User student = new User();
        student.setUsername(username);
        student.setPassword(encodedPassword);
        student.setEmail(email);
        student.setFullName(fullName);
        student.setRole(Role.STUDENT);
        student.setStudentId(studentId);
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());

        User savedStudent = userRepository.save(student);

        // Send email with credentials
        emailService.sendRegistrationEmail(savedStudent, rawPassword);

        return savedStudent;
    }

    private String generateRandomPassword() {
        // Generate a random 8-character password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public void saveInitialUsers() {
        if (userRepository.count() == 0) {
            // Create HODs for all departments
            String[] departments = {"Admissions", "Academics", "Finance", "SRS", "Transport",
                    "Research", "Hostel", "Library", "Examination", "IT Services"};

            // Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@university.edu");
            admin.setFullName("System Admin");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);

            // Create HODs
            for (String dept : departments) {
                User hod = new User();
                hod.setUsername("hod_" + dept.toLowerCase().replace(" ", ""));
                hod.setPassword(passwordEncoder.encode("hod123"));
                hod.setEmail("hod." + dept.toLowerCase().replace(" ", "") + "@university.edu");
                hod.setFullName("Dr. " + dept + " HOD");
                hod.setRole(Role.HOD);
                hod.setDepartment(dept);
                hod.setActive(true);
                userRepository.save(hod);

                // Create 2 staff per department
                for (int i = 1; i <= 2; i++) {
                    User staff = new User();
                    staff.setUsername("staff_" + dept.toLowerCase().replace(" ", "") + i);
                    staff.setPassword(passwordEncoder.encode("staff123"));
                    staff.setEmail("staff" + i + "." + dept.toLowerCase().replace(" ", "") + "@university.edu");
                    staff.setFullName(dept + " Staff " + i);
                    staff.setRole(Role.STAFF);
                    staff.setDepartment(dept);
                    staff.setActive(true);
                    userRepository.save(staff);
                }
            }

            // Create sample students
            for (int i = 1; i <= 5; i++) {
                User student = new User();
                student.setUsername("student" + i);
                student.setPassword(passwordEncoder.encode("student123"));
                student.setEmail("student" + i + "@university.edu");
                student.setFullName("Student " + i);
                student.setRole(Role.STUDENT);
                student.setStudentId("STU00" + i);
                student.setActive(true);
                userRepository.save(student);
            }
        }
    }
}
