package com.university.grievance.services;// EmailService.java - Add registration email method

import com.university.grievance.entity.Grievance;
import com.university.grievance.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    // NEW: Registration Email with Username and Password
    public void sendRegistrationEmail(User student, String rawPassword) {
        String subject = "Welcome to University Grievance Portal - Your Login Credentials";
        String text = String.format(
                "Dear %s,\n\n" +
                        "Thank you for registering with the University Grievance Portal.\n\n" +
                        "Your account has been successfully created. Please find your login credentials below:\n\n" +
                        "══════════════════════════════════════════\n" +
                        "Portal URL: http://localhost:8080/auth/login\n" +
                        "Username: %s\n" +
                        "Password: %s\n" +
                        "Student ID: %s\n" +
                        "══════════════════════════════════════════\n\n" +
                        "For security reasons, please change your password after first login.\n\n" +
                        "If you have any issues, please contact the IT support team.\n\n" +
                        "Best regards,\n" +
                        "University Grievance Portal Team",
                student.getFullName(),
                student.getUsername(),
                rawPassword,
                student.getStudentId()
        );
        sendEmail(student.getEmail(), subject, text);
    }

    public void notifyStaff(Grievance grievance, User staff) {
        String subject = "New Grievance: " + grievance.getGrievanceNumber();
        String text = String.format(
                "Dear %s,\n\n" +
                        "A new grievance has been filed in %s department.\n\n" +
                        "Grievance #: %s\n" +
                        "Title: %s\n" +
                        "Student: %s\n" +
                        "Priority: %s\n\n" +
                        "Please login to the portal to view and take action.\n\n" +
                        "Login URL: http://localhost:8080/auth/login\n\n" +
                        "Regards,\nGrievance Management System",
                staff.getFullName(),
                grievance.getDepartment(),
                grievance.getGrievanceNumber(),
                grievance.getTitle(),
                grievance.getStudent().getFullName(),
                grievance.getPriority()
        );
        sendEmail(staff.getEmail(), subject, text);
    }

    public void notifyHOD(Grievance grievance, User hod) {
        String subject = "New Grievance in Your Department";
        String text = String.format(
                "Dear %s,\n\n" +
                        "A new grievance has been filed in your department.\n\n" +
                        "Grievance #: %s\n" +
                        "Title: %s\n" +
                        "Student: %s\n" +
                        "Priority: %s\n\n" +
                        "Please login to assign it to a staff member.\n\n" +
                        "Login URL: http://localhost:8080/auth/login\n\n" +
                        "Regards,\nGrievance Management System",
                hod.getFullName(),
                grievance.getGrievanceNumber(),
                grievance.getTitle(),
                grievance.getStudent().getFullName(),
                grievance.getPriority()
        );
        sendEmail(hod.getEmail(), subject, text);
    }

    public void notifyStudent(Grievance grievance, String message) {
        String subject = "Grievance Update: " + grievance.getGrievanceNumber();
        String text = String.format(
                "Dear %s,\n\n%s\n\n" +
                        "Grievance #: %s\n" +
                        "Title: %s\n" +
                        "Status: %s\n\n" +
                        "Login to view details: http://localhost:8080/student/grievance/%d\n\n" +
                        "Regards,\nGrievance Management System",
                grievance.getStudent().getFullName(),
                message,
                grievance.getGrievanceNumber(),
                grievance.getTitle(),
                grievance.getStatus(),
                grievance.getId()
        );
        sendEmail(grievance.getStudent().getEmail(), subject, text);
    }
}