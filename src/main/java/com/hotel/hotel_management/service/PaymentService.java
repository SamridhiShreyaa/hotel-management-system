package com.hotel.hotel_management.service;

import com.hotel.hotel_management.model.*;
import com.hotel.hotel_management.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment recordPayment(Booking booking, User user, String method) {

        Payment payment = new Payment();

        payment.setBooking(booking);
        payment.setUser(user);
        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentMethod(method);
        payment.setPaymentDate(LocalDate.now());

        // ✅ Now this will work
        payment.setLogMessage(
                "Payment of ₹" + booking.getTotalAmount() + " received via " + method
        );

        return paymentRepository.save(payment);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    public List<Payment> findByUser(User user) {
        return paymentRepository.findByUserOrderByPaymentDateDesc(user);
    }

    public BigDecimal totalRevenue() {
        return paymentRepository.sumAllPayments();
    }
}