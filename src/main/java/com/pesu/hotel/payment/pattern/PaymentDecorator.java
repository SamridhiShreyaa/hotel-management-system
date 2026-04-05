package com.pesu.hotel.payment.pattern;

import java.time.LocalDateTime;

import com.pesu.hotel.payment.dto.PaymentRequest;
import com.pesu.hotel.payment.dto.PaymentResponse;
import com.pesu.hotel.payment.service.PaymentGateway;

/**
 * Structural Pattern - Decorator
 * Adds audit metadata behavior while preserving PaymentGateway contract.
 */
public class PaymentDecorator implements PaymentGateway {

	private final PaymentGateway delegate;

	public PaymentDecorator(PaymentGateway delegate) {
		this.delegate = delegate;
	}

	@Override
	public PaymentResponse process(PaymentRequest request) {
		PaymentResponse response = delegate.process(request);
		String baseMessage = response.getMessage() == null ? "" : response.getMessage();
		response.setMessage(baseMessage + " | audited at " + LocalDateTime.now());
		return response;
	}
}
