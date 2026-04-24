// HODController.java - Complete version
package com.university.grievance.controller;

import com.university.grievance.entity.Grievance;
import com.university.grievance.entity.User;
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
@RequestMapping("/hod")
@RequiredArgsConstructor
public class HODController {

    private final GrievanceService grievanceService;
    private final UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.findByUsername(auth.getName());
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User hod = getCurrentUser(auth);
        List<Grievance> grievances = grievanceService.getDepartmentGrievances(hod.getDepartment());
        List<User> staff = userService.getStaffByDepartment(hod.getDepartment());

        long pending = grievances.stream().filter(g -> g.getStatus() == Status.PENDING).count();
        long inProgress = grievances.stream().filter(g -> g.getStatus() == Status.IN_PROGRESS).count();
        long resolved = grievances.stream().filter(g -> g.getStatus() == Status.RESOLVED).count();

        model.addAttribute("user", hod);
        model.addAttribute("grievances", grievances);
        model.addAttribute("staff", staff);
        model.addAttribute("pending", pending);
        model.addAttribute("inProgress", inProgress);
        model.addAttribute("resolved", resolved);
        model.addAttribute("currentPage", "hod-dashboard");

        return "hod/dashboard";
    }

    @GetMapping("/grievance/{id}")
    public String viewGrievance(@PathVariable Long id,
                                Authentication auth,
                                Model model) {
        User hod = getCurrentUser(auth);
        Grievance grievance = grievanceService.findById(id);

        // Security check - HOD can only view grievances from their department
        if (!grievance.getDepartment().equals(hod.getDepartment())) {
            return "redirect:/hod/dashboard";
        }

        List<User> staff = userService.getStaffByDepartment(grievance.getDepartment());
        List<com.university.grievance.entity.Comment> comments = grievanceService.getComments(id);

        model.addAttribute("grievance", grievance);
        model.addAttribute("staff", staff);
        model.addAttribute("comments", comments);
        model.addAttribute("user", hod);
        model.addAttribute("currentPage", "hod-view");

        return "hod/view";
    }

    @RequestMapping("/grievance/{id}/assign")
    public String assignToStaff(@PathVariable Long id,
                                @RequestParam Long staffId,
                                Authentication auth,
                                RedirectAttributes redirect) {
        try {
            User hod = getCurrentUser(auth);
            Grievance grievance = grievanceService.findById(id);

            // Verify HOD can only assign from their department
            if (!grievance.getDepartment().equals(hod.getDepartment())) {
                redirect.addFlashAttribute("error", "You cannot assign grievances from other departments");
                return "redirect:/hod/dashboard";
            }

            User staff = userService.findById(staffId);

            // Verify staff belongs to same department
            if (!staff.getDepartment().equals(grievance.getDepartment())) {
                redirect.addFlashAttribute("error", "Staff must belong to the same department");
                return "redirect:/hod/grievance/" + id;
            }

            grievanceService.assignToStaff(id, staff);
            redirect.addFlashAttribute("success", "Grievance assigned to " + staff.getFullName());

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Assignment failed: " + e.getMessage());
        }

        return "redirect:/hod/dashboard";
    }

    @PostMapping("/grievance/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Status status,
                               Authentication auth,
                               RedirectAttributes redirect) {
        try {
            User hod = getCurrentUser(auth);
            Grievance grievance = grievanceService.findById(id);

            // Verify HOD can only update from their department
            if (!grievance.getDepartment().equals(hod.getDepartment())) {
                redirect.addFlashAttribute("error", "You cannot update grievances from other departments");
                return "redirect:/hod/dashboard";
            }

            grievanceService.updateStatus(id, status, hod);
            redirect.addFlashAttribute("success", "Status updated to " + status);

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Update failed: " + e.getMessage());
        }

        return "redirect:/hod/dashboard";
    }

    @PostMapping("/grievance/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication auth,
                             RedirectAttributes redirect) {
        try {
            User hod = getCurrentUser(auth);
            Grievance grievance = grievanceService.findById(id);

            // Verify HOD can only comment on their department's grievances
            if (!grievance.getDepartment().equals(hod.getDepartment())) {
                redirect.addFlashAttribute("error", "You cannot comment on grievances from other departments");
                return "redirect:/hod/dashboard";
            }

            grievanceService.addComment(id, content, hod);
            redirect.addFlashAttribute("success", "Comment added successfully");

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Failed to add comment: " + e.getMessage());
        }

        return "redirect:/hod/grievance/" + id;
    }
    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("user", user);
        }
    }
}