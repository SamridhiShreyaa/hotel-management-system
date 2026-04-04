package com.pesu.hotel.reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.pesu.hotel.reservation.dto.ModifyReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationResponse;
import com.pesu.hotel.reservation.service.ReservationServiceImpl;

class ReservationServiceTest {

	@Test
	void createReservationShouldReturnConfirmedReservation() {
		ReservationServiceImpl reservationService = new ReservationServiceImpl();
		ReservationRequest request = new ReservationRequest();
		request.setGuestId(2L);
		request.setRoomId(101L);
		request.setCheckInDate(LocalDate.now().plusDays(2));
		request.setCheckOutDate(LocalDate.now().plusDays(4));
		request.setNumGuests(2);

		ReservationResponse response = reservationService.createReservation(request);

		assertNotNull(response.getReservationId());
		assertEquals("CONFIRMED", response.getStatus());
	}

	@Test
	void modifyReservationShouldUpdateDatesAndGuests() {
		ReservationServiceImpl reservationService = new ReservationServiceImpl();
		ReservationRequest request = new ReservationRequest();
		request.setGuestId(3L);
		request.setRoomId(201L);
		request.setCheckInDate(LocalDate.now().plusDays(1));
		request.setCheckOutDate(LocalDate.now().plusDays(3));
		request.setNumGuests(1);

		ReservationResponse created = reservationService.createReservation(request);

		ModifyReservationRequest modifyRequest = new ModifyReservationRequest();
		modifyRequest.setCheckInDate(LocalDate.now().plusDays(2));
		modifyRequest.setCheckOutDate(LocalDate.now().plusDays(5));
		modifyRequest.setNumGuests(2);

		ReservationResponse updated = reservationService.modifyReservation(created.getReservationId(), modifyRequest);

		assertEquals(2, updated.getNumGuests());
		assertEquals("CONFIRMED", updated.getStatus());
	}

	@Test
	void cancelReservationShouldSetCancelledStatus() {
		ReservationServiceImpl reservationService = new ReservationServiceImpl();
		ReservationRequest request = new ReservationRequest();
		request.setGuestId(4L);
		request.setRoomId(301L);
		request.setCheckInDate(LocalDate.now().plusDays(3));
		request.setCheckOutDate(LocalDate.now().plusDays(5));

		ReservationResponse created = reservationService.createReservation(request);
		ReservationResponse cancelled = reservationService.cancelReservation(created.getReservationId());

		assertEquals("CANCELLED", cancelled.getStatus());
	}

	@Test
	void historyShouldFilterByGuestId() {
		ReservationServiceImpl reservationService = new ReservationServiceImpl();

		ReservationRequest guestOneRequest = new ReservationRequest();
		guestOneRequest.setGuestId(10L);
		guestOneRequest.setRoomId(1L);
		guestOneRequest.setCheckInDate(LocalDate.now().plusDays(1));
		guestOneRequest.setCheckOutDate(LocalDate.now().plusDays(2));
		reservationService.createReservation(guestOneRequest);

		ReservationRequest guestTwoRequest = new ReservationRequest();
		guestTwoRequest.setGuestId(20L);
		guestTwoRequest.setRoomId(2L);
		guestTwoRequest.setCheckInDate(LocalDate.now().plusDays(2));
		guestTwoRequest.setCheckOutDate(LocalDate.now().plusDays(4));
		reservationService.createReservation(guestTwoRequest);

		List<ReservationResponse> guestOneHistory = reservationService.getReservationHistory(10L);

		assertFalse(guestOneHistory.isEmpty());
		assertEquals(1, guestOneHistory.size());
		assertEquals(10L, guestOneHistory.get(0).getGuestId());
	}
}
