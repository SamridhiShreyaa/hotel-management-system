package com.pesu.hotel.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.pesu.hotel.payment.dto.PaymentRequest;
import com.pesu.hotel.payment.dto.PaymentResponse;
import com.pesu.hotel.payment.service.PaymentServiceImpl;

class PaymentServiceTest {

	@Test
	void processPaymentShouldSucceedForValidInput() {
		PaymentServiceImpl service = new PaymentServiceImpl();
		PaymentRequest request = new PaymentRequest();
		request.setReservationId(1001L);
		request.setAmount(new BigDecimal("4500.00"));
		request.setPaymentMethod("UPI");

		PaymentResponse response = service.processPayment(request);

		assertTrue(response.isSuccess());
		assertEquals("SUCCESS", response.getPaymentStatus());
		assertNotNull(response.getTransactionReference());
		assertEquals(1, service.getPaymentLogs().size());
	}

	@Test
	void processPaymentShouldFailForUnsupportedMethod() {
		PaymentServiceImpl service = new PaymentServiceImpl();
		PaymentRequest request = new PaymentRequest();
		request.setReservationId(1002L);
		request.setAmount(new BigDecimal("2500.00"));
		request.setPaymentMethod("CRYPTO");

		PaymentResponse response = service.processPayment(request);

		assertFalse(response.isSuccess());
		assertEquals("FAILED", response.getPaymentStatus());
		assertEquals(0, service.getPaymentLogs().size());
	}

	@Test
	void processPaymentShouldFailForInvalidAmount() {
		PaymentServiceImpl service = new PaymentServiceImpl();
		PaymentRequest request = new PaymentRequest();
		request.setReservationId(1003L);
		request.setAmount(BigDecimal.ZERO);
		request.setPaymentMethod("CREDIT_CARD");

		PaymentResponse response = service.processPayment(request);

		assertFalse(response.isSuccess());
		assertEquals("FAILED", response.getPaymentStatus());
	}
}
