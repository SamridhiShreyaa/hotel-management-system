package com.pesu.hotel.payment.service;

import java.util.List;

import com.pesu.hotel.payment.dto.PaymentRequest;
import com.pesu.hotel.payment.dto.PaymentResponse;

public interface PaymentService {
	PaymentResponse processPayment(PaymentRequest request);

	List<PaymentResponse> getPaymentLogs();
}
