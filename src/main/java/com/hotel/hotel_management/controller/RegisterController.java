package com.hotel.hotel_management.controller;

import com.hotel.hotel_management.model.User;
import com.hotel.hotel_management.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String form(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, RedirectAttributes ra) {

        if (userService.usernameExists(user.getUsername())) {
            ra.addFlashAttribute("error", "Username already taken.");
            return "redirect:/register";
        }

        if (userService.emailExists(user.getEmail())) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/register";
        }

        // ✅ Encode password here (NOT in service)
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        userService.register(user, encodedPassword);

        ra.addFlashAttribute("success", "Account created! Please sign in.");
        return "redirect:/signin";
    }
}