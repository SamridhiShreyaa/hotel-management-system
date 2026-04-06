package com.pesu.hotel.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.admin.service.AdminService;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

	private final AdminService adminService;

	public AdminReportController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping
	public String reports(Model model) {
		model.addAttribute("reports", adminService.getReports());
		return "admin/reports";
	}
}
