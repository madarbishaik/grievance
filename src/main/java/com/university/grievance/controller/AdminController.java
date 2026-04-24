// AdminController.java
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final GrievanceService grievanceService;
    private final UserService userService;
    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("user", user);
        }
    }
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User admin = userService.findByUsername(auth.getName());
        List<Grievance> allGrievances = grievanceService.getAllGrievances();
        List<User> allUsers = userService.getAllUsers();

        long pending = allGrievances.stream().filter(g -> g.getStatus() == Status.PENDING).count();
        long inProgress = allGrievances.stream().filter(g -> g.getStatus() == Status.IN_PROGRESS).count();
        long resolved = allGrievances.stream().filter(g -> g.getStatus() == Status.RESOLVED).count();

        Map<String, Long> deptWise = allGrievances.stream()
                .collect(Collectors.groupingBy(Grievance::getDepartment, Collectors.counting()));

        model.addAttribute("user", admin);
        model.addAttribute("totalGrievances", allGrievances.size());
        model.addAttribute("totalUsers", allUsers.size());
        model.addAttribute("pending", pending);
        model.addAttribute("inProgress", inProgress);
        model.addAttribute("resolved", resolved);
        model.addAttribute("deptWise", deptWise);
        model.addAttribute("recentGrievances", allGrievances.stream().limit(10).toList());

        return "admin/dashboard";
    }

    @GetMapping("/grievances")
    public String grievances(Model model) {
        model.addAttribute("grievances", grievanceService.getAllGrievances());
        return "admin/grievances";
    }
}