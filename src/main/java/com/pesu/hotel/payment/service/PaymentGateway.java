package com.pesu.hotel.payment.service;

import com.pesu.hotel.payment.dto.PaymentRequest;
import com.pesu.hotel.payment.dto.PaymentResponse;

public interface PaymentGateway {
	PaymentResponse process(PaymentRequest request);
}
