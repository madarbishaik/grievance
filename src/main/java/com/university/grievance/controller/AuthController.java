// AuthController.java - Add registration method
package com.university.grievance.controller;

import com.university.grievance.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerStudent(@RequestParam String fullName,
                                  @RequestParam String email,
                                  @RequestParam String studentId,
                                  @RequestParam(required = false) String phone,
                                  RedirectAttributes redirectAttributes) {

        try {
            // Validate if username/email already exists
            if (userService.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Email already registered!");
                return "redirect:/auth/register";
            }

            if (userService.existsByStudentId(studentId)) {
                redirectAttributes.addFlashAttribute("error", "Student ID already exists!");
                return "redirect:/auth/register";
            }

            // Register the student
            userService.registerStudent(fullName, email, phone, studentId);

            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Your login credentials have been sent to your email.");

            return "redirect:/auth/login";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }
}