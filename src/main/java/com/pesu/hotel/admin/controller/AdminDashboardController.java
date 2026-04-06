package com.pesu.hotel.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.admin.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

	private final AdminService adminService;

	public AdminDashboardController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping({"", "/dashboard"})
	public String dashboard(Model model) {
		model.addAttribute("rooms", adminService.getRooms());
		model.addAttribute("reservations", adminService.getReservations());
		model.addAttribute("guests", adminService.getGuests());
		model.addAttribute("reports", adminService.getReports());
		return "admin/dashboard";
	}
}
