// GrievanceService.java
package com.university.grievance.services;

import com.university.grievance.Repository.CommentRepository;
import com.university.grievance.Repository.GrievanceRepository;
import com.university.grievance.entity.Comment;
import com.university.grievance.entity.Grievance;
import com.university.grievance.entity.User;
import com.university.grievance.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final CommentRepository commentRepository;
    private final EmailService emailService;
    private final UserService userService;

    public Grievance save(Grievance grievance) {
        return grievanceRepository.save(grievance);
    }

    public Grievance findById(Long id) {
        return grievanceRepository.findById(id).orElse(null);
    }

    public List<Grievance> getStudentGrievances(Long studentId) {
        return grievanceRepository.findByStudentId(studentId);
    }

    public List<Grievance> getStaffGrievances(Long staffId) {
        return grievanceRepository.findByAssignedToId(staffId);
    }

    public List<Grievance> getDepartmentGrievances(String department) {
        return grievanceRepository.findByDepartment(department);
    }

    public List<Grievance> getPendingGrievances(String department) {
        return grievanceRepository.findByDepartmentAndStatus(department, Status.PENDING);
    }

    public long getDepartmentStats(String department, Status status) {
        return grievanceRepository.countByDepartmentAndStatus(department, status);
    }

    public long getTotalDepartmentGrievances(String department) {
        return grievanceRepository.countByDepartment(department);
    }

    public List<Grievance> getAllGrievances() {
        return grievanceRepository.findAll();
    }

    @Transactional
    public Grievance fileGrievance(Grievance grievance, User student) {
        grievance.setStudent(student);
        grievance.setStatus(Status.PENDING);
        Grievance saved = grievanceRepository.save(grievance);

        // Notify HOD
        User hod = userService.getHODByDepartment(grievance.getDepartment());
        if (hod != null) {
            emailService.notifyHOD(saved, hod);
        }

        // Notify all staff
        List<User> staff = userService.getStaffByDepartment(grievance.getDepartment());
        for (User s : staff) {
            emailService.notifyStaff(saved, s);
        }

        return saved;
    }





    public List<Comment> getComments(Long grievanceId) {
        return commentRepository.findByGrievanceIdOrderByCreatedAtDesc(grievanceId);
    }





    /**
     * HOD assigns grievance to a specific staff member
     */
    @Transactional
    public Grievance assignToStaff(Long grievanceId, User staff) {
        Grievance grievance = findById(grievanceId);

        if (grievance == null) {
            throw new RuntimeException("Grievance not found");
        }

        // Store previous assignee for notification
        User previousAssignee = grievance.getAssignedTo();

        // Assign to staff
        grievance.setAssignedTo(staff);
        grievance.setAssignedAt(LocalDateTime.now());

        // Update status from PENDING to IN_PROGRESS if it was pending
        if (grievance.getStatus() == Status.PENDING) {
            grievance.setStatus(Status.IN_PROGRESS);
        }

        grievance.setUpdatedAt(LocalDateTime.now());

        Grievance saved = grievanceRepository.save(grievance);

        // Add comment about assignment
        Comment comment = new Comment();
        comment.setGrievance(grievance);
        comment.setUser(staff);
        comment.setContent("Grievance assigned to " + staff.getFullName());
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Send email notification to staff
        emailService.notifyStaff(saved, staff);

        // Send email notification to student
        String message = "Your grievance has been assigned to " + staff.getFullName() + " from " +
                grievance.getDepartment() + " department.";
        emailService.notifyStudent(saved, message);

        return saved;
    }

    /**
     * Staff can assign grievance to themselves
     */
    @Transactional
    public Grievance assignToSelf(Long grievanceId, User staff) {
        Grievance grievance = findById(grievanceId);

        if (grievance == null) {
            throw new RuntimeException("Grievance not found");
        }

        // Check if staff belongs to the correct department
        if (!staff.getDepartment().equals(grievance.getDepartment())) {
            throw new RuntimeException("You can only assign grievances from your department");
        }

        // Check if already assigned
        if (grievance.getAssignedTo() != null) {
            throw new RuntimeException("Grievance already assigned to " +
                    grievance.getAssignedTo().getFullName());
        }

        // Assign to self
        grievance.setAssignedTo(staff);
        grievance.setAssignedAt(LocalDateTime.now());
        grievance.setStatus(Status.IN_PROGRESS);
        grievance.setUpdatedAt(LocalDateTime.now());

        Grievance saved = grievanceRepository.save(grievance);

        // Add comment
        Comment comment = new Comment();
        comment.setGrievance(grievance);
        comment.setUser(staff);
        comment.setContent("Staff " + staff.getFullName() + " has taken ownership of this grievance");
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Notify student
        String message = "Your grievance is now being handled by " + staff.getFullName();
        emailService.notifyStudent(saved, message);

        return saved;
    }

    // ========== STATUS UPDATE METHODS ==========

    @Transactional
    public Grievance updateStatus(Long grievanceId, Status status, User user) {
        Grievance grievance = findById(grievanceId);
        Status oldStatus = grievance.getStatus();

        grievance.setStatus(status);

        if (status == Status.RESOLVED) {
            grievance.setResolvedAt(LocalDateTime.now());
        }

        grievance.setUpdatedAt(LocalDateTime.now());
        Grievance saved = grievanceRepository.save(grievance);

        // Add comment
        Comment comment = new Comment();
        comment.setGrievance(grievance);
        comment.setUser(user);
        comment.setContent("Status changed from " + oldStatus + " to " + status);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Notify student
        String message = "Your grievance status has been updated to: " + status;
        emailService.notifyStudent(saved, message);

        return saved;
    }

    @Transactional
    public Comment addComment(Long grievanceId, String content, User user) {
        Grievance grievance = findById(grievanceId);

        Comment comment = new Comment();
        comment.setGrievance(grievance);
        comment.setUser(user);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);

        // Notify student if comment is from staff
        if (user.getRole().name().equals("STAFF") || user.getRole().name().equals("HOD")) {
            String message = "New comment added by " + user.getFullName() + ": " + content;
            emailService.notifyStudent(grievance, message);
        }

        return saved;
    }



    @Transactional
    public void submitFeedback(Long grievanceId, String feedback, int rating) {
        Grievance grievance = findById(grievanceId);
        grievance.setFeedback(feedback);
        grievance.setRating(rating);
        grievance.setStatus(Status.CLOSED);
        grievance.setUpdatedAt(LocalDateTime.now());
        grievanceRepository.save(grievance);

        // Add comment
        Comment comment = new Comment();
        comment.setGrievance(grievance);
        comment.setUser(grievance.getStudent());
        comment.setContent("Student submitted feedback with rating: " + rating + "/5");
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        // Notify assigned staff
        if (grievance.getAssignedTo() != null) {
            String message = "Student has submitted feedback for grievance " + grievance.getGrievanceNumber();
            emailService.sendEmail(grievance.getAssignedTo().getEmail(), "Feedback Received", message);
        }
    }

    // ========== HELPER METHODS ==========

    public long countByDepartment(String department) {
        return grievanceRepository.countByDepartment(department);
    }

    public long countByDepartmentAndStatus(String department, Status status) {
        return grievanceRepository.countByDepartmentAndStatus(department, status);
    }
}