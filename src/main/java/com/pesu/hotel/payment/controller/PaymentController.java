package com.pesu.hotel.payment.controller;

import java.util.List;

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
	public String showCheckout(Model model) {
		model.addAttribute("paymentRequest", new PaymentRequest());
		model.addAttribute("methods", List.of("CREDIT_CARD", "DEBIT_CARD", "UPI", "NET_BANKING", "CASH"));
		return "payment/checkout";
	}

	@PostMapping("/checkout")
	public String process(@ModelAttribute PaymentRequest paymentRequest, Model model) {
		PaymentResponse response = paymentService.processPayment(paymentRequest);
		model.addAttribute("paymentResponse", response);
		return "payment/success";
	}

	@GetMapping("/logs")
	public String paymentLogs(Model model) {
		model.addAttribute("logs", paymentService.getPaymentLogs());
		return "payment/success";
	}
}
