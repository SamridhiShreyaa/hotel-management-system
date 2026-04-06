package com.pesu.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pesu.hotel.entity.Payment;
import com.pesu.hotel.entity.PaymentStatus;
import com.pesu.hotel.entity.Reservation;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findByReservation(Reservation reservation);

	List<Payment> findByPaymentStatus(PaymentStatus status);
}
