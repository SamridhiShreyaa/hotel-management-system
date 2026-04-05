package com.pesu.hotel.payment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.pesu.hotel.payment.dto.PaymentRequest;
import com.pesu.hotel.payment.dto.PaymentResponse;
import com.pesu.hotel.payment.pattern.PaymentDecorator;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Set<String> SUPPORTED_METHODS = Set.of(
			"CREDIT_CARD", "DEBIT_CARD", "UPI", "NET_BANKING", "CASH");

	private final PaymentGateway paymentGateway;
	private final List<PaymentResponse> paymentLogs = new CopyOnWriteArrayList<>();

	public PaymentServiceImpl() {
		this.paymentGateway = new PaymentDecorator(new BasicPaymentGateway());
	}

	@Override
	public PaymentResponse processPayment(PaymentRequest request) {
		if (request == null || request.getReservationId() == null) {
			return new PaymentResponse(false, "FAILED", null, "Reservation id is required.");
		}
		if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
			return new PaymentResponse(false, "FAILED", null, "Amount must be greater than zero.");
		}
		if (request.getPaymentMethod() == null || !SUPPORTED_METHODS.contains(request.getPaymentMethod())) {
			return new PaymentResponse(false, "FAILED", null, "Unsupported payment method.");
		}

		PaymentResponse response = paymentGateway.process(request);
		paymentLogs.add(response);
		return response;
	}

	@Override
	public List<PaymentResponse> getPaymentLogs() {
		return new ArrayList<>(paymentLogs);
	}

	private static class BasicPaymentGateway implements PaymentGateway {
		@Override
		public PaymentResponse process(PaymentRequest request) {
			String txRef = "TXN-" + request.getReservationId() + "-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			return new PaymentResponse(
					true,
					"SUCCESS",
					txRef,
					"Payment processed successfully via " + request.getPaymentMethod());
		}
	}
}
