// StudentController.java
package com.university.grievance.controller;

import com.university.grievance.entity.Grievance;
import com.university.grievance.entity.User;
import com.university.grievance.enums.Priority;
import com.university.grievance.enums.Status;
import com.university.grievance.services.GrievanceService;
import com.university.grievance.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final GrievanceService grievanceService;
    private final UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.findByUsername(auth.getName());
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User student = getCurrentUser(auth);
        List<Grievance> grievances = grievanceService.getStudentGrievances(student.getId());

        long pending = grievances.stream().filter(g -> g.getStatus() == Status.PENDING).count();
        long inProgress = grievances.stream().filter(g -> g.getStatus() == Status.IN_PROGRESS).count();
        long resolved = grievances.stream().filter(g -> g.getStatus() == Status.RESOLVED).count();

        model.addAttribute("user", student);
        model.addAttribute("grievances", grievances);
        model.addAttribute("pending", pending);
        model.addAttribute("inProgress", inProgress);
        model.addAttribute("resolved", resolved);

        return "student/dashboard";
    }

    @GetMapping("/file")
    public String fileForm(Model model) {
        model.addAttribute("grievance", new Grievance());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("departments", new String[]{
                "Admissions", "Academics", "Finance", "SRS", "Transport",
                "Research", "Hostel", "Library", "Examination", "IT Services"
        });
        return "student/file";
    }
    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("user", user);
        }
    }


    @PostMapping("/file")
    public String fileGrievance(@ModelAttribute Grievance grievance,
                                Authentication auth,
                                RedirectAttributes redirect) {
        User student = getCurrentUser(auth);
        grievanceService.fileGrievance(grievance, student);
        redirect.addFlashAttribute("success", "Grievance filed successfully!");
        return "redirect:/student/dashboard";
    }

    @GetMapping("/grievance/{id}")
    public String viewGrievance(@PathVariable Long id, Model model) {
        Grievance grievance = grievanceService.findById(id);
        model.addAttribute("grievance", grievance);
        model.addAttribute("comments", grievanceService.getComments(id));
        return "student/view";
    }

    @PostMapping("/grievance/{id}/feedback")
    public String submitFeedback(@PathVariable Long id,
                                 @RequestParam String feedback,
                                 @RequestParam int rating,
                                 RedirectAttributes redirect) {
        grievanceService.submitFeedback(id, feedback, rating);
        redirect.addFlashAttribute("success", "Thank you for your feedback!");
        return "redirect:/student/dashboard";
    }
    // StudentController.java - Add this method for adding comments

    @PostMapping("/grievance/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication auth,
                             RedirectAttributes redirect) {
        try {
            User student = getCurrentUser(auth);
            Grievance grievance = grievanceService.findById(id);

            // Verify student owns this grievance
            if (!grievance.getStudent().getId().equals(student.getId())) {
                redirect.addFlashAttribute("error", "You can only comment on your own grievances");
                return "redirect:/student/dashboard";
            }

            grievanceService.addComment(id, content, student);
            redirect.addFlashAttribute("success", "Comment added successfully");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Failed to add comment: " + e.getMessage());
        }

        return "redirect:/student/grievance/" + id;
    }


}