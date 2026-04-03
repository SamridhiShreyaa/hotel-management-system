package com.pesu.hotel.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.auth.dto.LoginRequest;
import com.pesu.hotel.auth.dto.LoginResponse;
import com.pesu.hotel.auth.dto.RegisterRequest;
import com.pesu.hotel.auth.service.AuthService;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("registerRequest", new RegisterRequest());
		return "auth/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute RegisterRequest registerRequest, Model model) {
		LoginResponse response = authService.register(registerRequest);
		model.addAttribute("message", response.getMessage());
		model.addAttribute("registerRequest", new RegisterRequest());
		return "auth/register";
	}

	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "auth/login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest loginRequest, Model model) {
		LoginResponse response = authService.login(loginRequest);
		model.addAttribute("message", response.getMessage());
		if (response.isAuthenticated()) {
			model.addAttribute("loginRequest", new LoginRequest());
			return "redirect:/auth/room-search";
		}
		model.addAttribute("loginRequest", new LoginRequest());
		return "auth/login";
	}
}
