package com.pesu.hotel.reservation.service;

import java.util.List;

import com.pesu.hotel.reservation.dto.ModifyReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationResponse;

public interface ReservationService {
	ReservationResponse createReservation(ReservationRequest request);

	ReservationResponse confirmReservation(Long reservationId);

	ReservationResponse modifyReservation(Long reservationId, ModifyReservationRequest request);

	ReservationResponse cancelReservation(Long reservationId);

	List<ReservationResponse> getReservationHistory(Long guestId);

	ReservationResponse getReservationById(Long reservationId);
}
