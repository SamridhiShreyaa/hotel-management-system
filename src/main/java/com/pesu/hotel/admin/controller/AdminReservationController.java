package com.pesu.hotel.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.admin.service.AdminService;

@Controller
@RequestMapping("/admin/reservations")
public class AdminReservationController {

	private final AdminService adminService;

	public AdminReservationController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping
	public String reservations(Model model) {
		model.addAttribute("reservations", adminService.getReservations());
		return "admin/reservations";
	}
}
