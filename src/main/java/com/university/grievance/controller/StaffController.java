// StaffController.java
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
@RequestMapping("/staff")
@RequiredArgsConstructor
public class StaffController {

    private final GrievanceService grievanceService;
    private final UserService userService;

    private User getCurrentUser(Authentication auth) {
        return userService.findByUsername(auth.getName());
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User staff = getCurrentUser(auth);
        List<Grievance> myGrievances = grievanceService.getStaffGrievances(staff.getId());
        List<Grievance> pendingGrievances = grievanceService.getPendingGrievances(staff.getDepartment());

        model.addAttribute("user", staff);
        model.addAttribute("myGrievances", myGrievances);
        model.addAttribute("pendingGrievances", pendingGrievances);

        return "staff/dashboard";
    }

    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/grievance/{id}")
    public String viewGrievance(@PathVariable Long id, Model model) {
        Grievance grievance = grievanceService.findById(id);
        model.addAttribute("grievance", grievance);
        model.addAttribute("comments", grievanceService.getComments(id));
        return "staff/view";
    }

    @PostMapping("/grievance/{id}/assign")
    public String assignToMe(@PathVariable Long id,
                             Authentication auth,
                             RedirectAttributes redirect) {
        User staff = getCurrentUser(auth);
        grievanceService.assignToStaff(id, staff);
        redirect.addFlashAttribute("success", "Grievance assigned to you");
        return "redirect:/staff/grievance/" + id;
    }

    @PostMapping("/grievance/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Status status,
                               Authentication auth,
                               RedirectAttributes redirect) {
        User staff = getCurrentUser(auth);
        grievanceService.updateStatus(id, status, staff);
        redirect.addFlashAttribute("success", "Status updated to " + status);
        return "redirect:/staff/grievance/" + id;
    }

    @PostMapping("/grievance/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Authentication auth) {
        User staff = getCurrentUser(auth);
        grievanceService.addComment(id, content, staff);
        return "redirect:/staff/grievance/" + id;
    }
}