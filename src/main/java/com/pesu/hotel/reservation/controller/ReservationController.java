package com.pesu.hotel.reservation.controller;

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
	public String showCreate(Model model) {
		model.addAttribute("reservationRequest", new ReservationRequest());
		return "reservation/create";
	}

	@PostMapping("/create")
	public String createReservation(@ModelAttribute ReservationRequest reservationRequest) {
		ReservationResponse response = reservationService.createReservation(reservationRequest);
		if (response.getReservationId() == null) {
			return "redirect:/reservations/create?error=" + response.getMessage();
		}
		return "redirect:/reservations/confirm/" + response.getReservationId();
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
