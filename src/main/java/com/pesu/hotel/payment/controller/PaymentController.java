package com.pesu.hotel.payment.controller;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pesu.hotel.payment.dto.PaymentRequest;
import com.pesu.hotel.payment.dto.PaymentResponse;
import com.pesu.hotel.payment.service.PaymentService;

@Controller
@RequestMapping("/payment")
public class PaymentController {

	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@GetMapping("/checkout")
	public String showCheckout(@org.springframework.web.bind.annotation.RequestParam(required = false) Long reservationId,
							@org.springframework.web.bind.annotation.RequestParam(required = false) BigDecimal amount,
							Model model) {
			PaymentRequest paymentRequest = new PaymentRequest();
			paymentRequest.setReservationId(reservationId);
			paymentRequest.setAmount(amount);
			model.addAttribute("paymentRequest", paymentRequest);
		model.addAttribute("methods", List.of("CREDIT_CARD", "DEBIT_CARD", "UPI", "NET_BANKING", "CASH"));
		return "payment/checkout";
	}

	@PostMapping("/checkout")
	public String process(@ModelAttribute PaymentRequest paymentRequest, Model model) {
		PaymentResponse response = paymentService.processPayment(paymentRequest);
		model.addAttribute("paymentResponse", response);
		model.addAttribute("reservationId", paymentRequest.getReservationId());
		if (!response.isSuccess()) {
			return "payment/failed";
		}
		return "payment/success";
	}

	@GetMapping("/logs")
	public String paymentLogs(Model model) {
		model.addAttribute("logs", paymentService.getPaymentLogs());
		return "payment/success";
	}
}
