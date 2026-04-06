package com.pesu.hotel.reservation.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pesu.hotel.reservation.dto.ModifyReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationResponse;
import com.pesu.hotel.reservation.service.ReservationService;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping
	public String listReservations(@RequestParam(required = false) Long guestId, Model model) {
		model.addAttribute("reservations", reservationService.getReservationHistory(guestId));
		model.addAttribute("guestId", guestId == null ? "" : guestId);
		return "reservation/list";
	}

	@GetMapping("/create")
	public String showCreate(@RequestParam(required = false) Long guestId,
							@RequestParam(required = false) Long roomId,
							@RequestParam(required = false) String checkInDate,
							@RequestParam(required = false) String checkOutDate,
							@RequestParam(required = false) Integer numGuests,
							Model model) {
		ReservationRequest reservationRequest = new ReservationRequest();
		reservationRequest.setGuestId(guestId);
		reservationRequest.setRoomId(roomId);
		try {
			if (checkInDate != null && !checkInDate.isBlank()) {
				reservationRequest.setCheckInDate(java.time.LocalDate.parse(checkInDate));
			}
			if (checkOutDate != null && !checkOutDate.isBlank()) {
				reservationRequest.setCheckOutDate(java.time.LocalDate.parse(checkOutDate));
			}
		} catch (java.time.format.DateTimeParseException ex) {
			model.addAttribute("error", "Invalid date format. Please select valid dates.");
		}
		if (reservationRequest.getCheckInDate() == null) {
			reservationRequest.setCheckInDate(LocalDate.now().minusDays(1));
		}
		if (reservationRequest.getCheckOutDate() == null) {
			reservationRequest.setCheckOutDate(LocalDate.now());
		}
		reservationRequest.setNumGuests(numGuests);
		model.addAttribute("reservationRequest", reservationRequest);
		model.addAttribute("today", LocalDate.now());
		return "reservation/create";
	}

	@PostMapping("/create")
	public String createReservation(@ModelAttribute ReservationRequest reservationRequest, Model model) {
		if (!isValidReservationDates(reservationRequest.getCheckInDate(), reservationRequest.getCheckOutDate())) {
			model.addAttribute("error", "Booking dates must be today or earlier, and check-out must be after check-in.");
			model.addAttribute("reservationRequest", reservationRequest);
			model.addAttribute("today", LocalDate.now());
			return "reservation/create";
		}

		ReservationResponse response = reservationService.createReservation(reservationRequest);
		if (response.getReservationId() == null) {
			String encodedError = URLEncoder.encode(response.getMessage(), StandardCharsets.UTF_8);
			return "redirect:/reservations/create?error=" + encodedError;
		}
		return "redirect:/payment/checkout?reservationId=" + response.getReservationId() + "&amount=" + response.getTotalAmount();
	}

	private boolean isValidReservationDates(LocalDate checkInDate, LocalDate checkOutDate) {
		if (checkInDate == null || checkOutDate == null) {
			return false;
		}
		LocalDate today = LocalDate.now();
		if (checkInDate.isAfter(today) || checkOutDate.isAfter(today)) {
			return false;
		}
		return checkOutDate.isAfter(checkInDate);
	}

	@GetMapping("/confirm/{reservationId}")
	public String confirmPage(@PathVariable Long reservationId, Model model) {
		model.addAttribute("reservation", reservationService.getReservationById(reservationId));
		return "reservation/confirm";
	}

	@GetMapping("/modify/{reservationId}")
	public String showModify(@PathVariable Long reservationId, Model model) {
		model.addAttribute("reservationId", reservationId);
		model.addAttribute("modifyRequest", new ModifyReservationRequest());
		return "reservation/modify";
	}

	@PostMapping("/modify/{reservationId}")
	public String modifyReservation(@PathVariable Long reservationId,
									@ModelAttribute ModifyReservationRequest modifyRequest) {
		reservationService.modifyReservation(reservationId, modifyRequest);
		return "redirect:/reservations";
	}

	@PostMapping("/cancel/{reservationId}")
	public String cancelReservation(@PathVariable Long reservationId) {
		reservationService.cancelReservation(reservationId);
		return "redirect:/reservations";
	}
}
