package com.pesu.hotel.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	@GetMapping("/login")
	public String showLogin(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "auth/login";
	}

	@GetMapping("/login-page")
	public String legacyLoginRedirect() {
		return "redirect:/auth/login";
	}

	@GetMapping("/register-success")
	public String showRegisterSuccess(Model model) {
		model.addAttribute("message", "Registration successful. Please login.");
		model.addAttribute("loginRequest", new LoginRequest());
		return "auth/login";
	}

	@GetMapping("/register-failure")
	public String showRegisterFailure(Model model) {
		model.addAttribute("message", "Registration failed. Please try again.");
		model.addAttribute("registerRequest", new RegisterRequest());
		return "auth/register";
	}

	@org.springframework.web.bind.annotation.PostMapping("/register")
	public String register(@ModelAttribute RegisterRequest registerRequest, Model model) {
		LoginResponse response = authService.register(registerRequest);
		if (response.isAuthenticated()) {
			return "redirect:/auth/login?registered=true";
		}
		model.addAttribute("message", response.getMessage());
		model.addAttribute("registerRequest", registerRequest);
		return "auth/register";
	}

	@GetMapping("/login-success")
	public String loginSuccess() {
		return "redirect:/auth/room-search";
	}
}
