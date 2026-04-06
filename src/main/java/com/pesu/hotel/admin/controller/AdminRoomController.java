package com.pesu.hotel.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.admin.service.AdminService;

@Controller
@RequestMapping("/admin/rooms")
public class AdminRoomController {

	private final AdminService adminService;

	public AdminRoomController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping
	public String rooms(Model model) {
		model.addAttribute("rooms", adminService.getRooms());
		return "admin/rooms";
	}
}
