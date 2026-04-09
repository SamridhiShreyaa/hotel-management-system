package com.hotel.hotel_management.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) return "redirect:/dashboard";
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String signin(Authentication auth,
                         @RequestParam(required = false) String error,
                         @RequestParam(required = false) String logout,
                         Model model) {
        if (auth != null && auth.isAuthenticated()) return "redirect:/dashboard";
        if (error  != null) model.addAttribute("error",  "Invalid username or password.");
        if (logout != null) model.addAttribute("logout", "You have been signed out.");
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth == null) return "redirect:/signin";
        boolean isAdmin = auth.getAuthorities()
            .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return isAdmin ? "redirect:/admin/dashboard" : "redirect:/user/dashboard";
    }
}