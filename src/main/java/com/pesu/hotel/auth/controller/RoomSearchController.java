package com.pesu.hotel.auth.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
	public String showSearch(Authentication authentication, Model model) {
		if (isAdmin(authentication)) {
			return "redirect:/admin/dashboard";
		}

		model.addAttribute("today", LocalDate.now());
		RoomSearchRequest request = new RoomSearchRequest();
		request.setGuests(1);
		model.addAttribute("roomSearchRequest", request);
		model.addAttribute("results", List.of());
		return "auth/room-search";
	}

	@PostMapping("/room-search")
	public String search(Authentication authentication, @ModelAttribute RoomSearchRequest roomSearchRequest, Model model) {
		if (isAdmin(authentication)) {
			return "redirect:/admin/dashboard";
		}

		if (!isValidSearch(roomSearchRequest)) {
			model.addAttribute("error", "Please provide valid dates for today or earlier, with check-out after check-in, and at least 1 guest.");
			model.addAttribute("results", Collections.emptyList());
			model.addAttribute("roomSearchRequest", roomSearchRequest);
			model.addAttribute("today", LocalDate.now());
			return "auth/room-search";
		}

		model.addAttribute("roomSearchRequest", roomSearchRequest);
		model.addAttribute("results", roomSearchService.searchAvailableRooms(roomSearchRequest));
		model.addAttribute("today", LocalDate.now());
		return "auth/room-search";
	}

	private boolean isValidSearch(RoomSearchRequest request) {
		if (request.getCheckInDate() == null || request.getCheckOutDate() == null) {
			return false;
		}
		LocalDate today = LocalDate.now();
		if (request.getCheckInDate().isAfter(today) || request.getCheckOutDate().isAfter(today)) {
			return false;
		}
		if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
			return false;
		}
		return request.getGuests() != null && request.getGuests() > 0;
	}

	private boolean isAdmin(Authentication authentication) {
		if (authentication == null) {
			return false;
		}
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch("ROLE_ADMIN"::equals);
	}
}
