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
import com.pesu.hotel.reservation.service.ReservationService;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Set<String> SUPPORTED_METHODS = Set.of(
			"CREDIT_CARD", "DEBIT_CARD", "UPI", "NET_BANKING", "CASH");

	private final PaymentGateway paymentGateway;
	private final ReservationService reservationService;
	private final List<PaymentResponse> paymentLogs = new CopyOnWriteArrayList<>();

	public PaymentServiceImpl() {
		this(null);
	}

	public PaymentServiceImpl(ReservationService reservationService) {
		this.reservationService = reservationService;
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
		if (request.getTransactionReference() == null || request.getTransactionReference().isBlank()) {
			return new PaymentResponse(false, "FAILED", null, "Transaction reference is required.");
		}

		PaymentResponse response = paymentGateway.process(request);
		paymentLogs.add(response);
		if (reservationService != null) {
			if (response.isSuccess()) {
				reservationService.confirmReservation(request.getReservationId());
			} else {
				reservationService.cancelReservation(request.getReservationId());
			}
		}
		return response;
	}

	@Override
	public List<PaymentResponse> getPaymentLogs() {
		return new ArrayList<>(paymentLogs);
	}

	private static class BasicPaymentGateway implements PaymentGateway {
		@Override
		public PaymentResponse process(PaymentRequest request) {
			boolean shouldFail = "CASH".equals(request.getPaymentMethod())
					&& request.getAmount().compareTo(new BigDecimal("5000")) > 0;

			String providedRef = request.getTransactionReference().trim();
			String txRef = "TXN-" + request.getReservationId() + "-"
					+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

			if (providedRef.length() < 4 || shouldFail) {
				return new PaymentResponse(
						false,
						"FAILED",
						txRef,
						"Payment failed. Please retry with a valid reference or another payment method.");
			}

			return new PaymentResponse(
					true,
					"SUCCESS",
					txRef,
					"Payment processed successfully via " + request.getPaymentMethod());
		}
	}
}
