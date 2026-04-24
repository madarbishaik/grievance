// DashboardController.java
package com.university.grievance.controller;

import com.university.grievance.entity.User;
import com.university.grievance.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        User user = userService.findByUsername(auth.getName());

        switch (user.getRole()) {
            case STUDENT:
                return "redirect:/student/dashboard";
            case STAFF:
                return "redirect:/staff/dashboard";
            case HOD:
                return "redirect:/hod/dashboard";
            case ADMIN:
                return "redirect:/admin/dashboard";
            default:
                return "redirect:/auth/login";
        }
    }

    @ModelAttribute
    public void addUserToModel(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName());
            model.addAttribute("user", user);
        }
    }
}