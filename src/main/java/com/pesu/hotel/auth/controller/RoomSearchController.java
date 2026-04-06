package com.pesu.hotel.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.auth.dto.RoomSearchRequest;
import com.pesu.hotel.auth.service.RoomSearchService;

@Controller
@RequestMapping("/auth")
public class RoomSearchController {

	private final RoomSearchService roomSearchService;

	public RoomSearchController(RoomSearchService roomSearchService) {
		this.roomSearchService = roomSearchService;
	}

	@GetMapping("/room-search")
	public String showSearch(Model model) {
		model.addAttribute("roomSearchRequest", new RoomSearchRequest());
		model.addAttribute("results", roomSearchService.searchAvailableRooms(new RoomSearchRequest()));
		return "auth/room-search";
	}

	@PostMapping("/room-search")
	public String search(@ModelAttribute RoomSearchRequest roomSearchRequest, Model model) {
		model.addAttribute("roomSearchRequest", roomSearchRequest);
		model.addAttribute("results", roomSearchService.searchAvailableRooms(roomSearchRequest));
		return "auth/room-search";
	}
}
