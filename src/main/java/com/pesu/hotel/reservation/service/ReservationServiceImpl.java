package com.pesu.hotel.reservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.pesu.hotel.reservation.dto.ModifyReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationRequest;
import com.pesu.hotel.reservation.dto.ReservationResponse;
import com.pesu.hotel.reservation.pattern.ReservationBuilder;

@Service
public class ReservationServiceImpl implements ReservationService {

	private static final BigDecimal DEFAULT_PRICE_PER_NIGHT = new BigDecimal("4500.00");

	private final AtomicLong sequence = new AtomicLong(1000);
	private final Map<Long, ReservationResponse> reservations = new ConcurrentHashMap<>();

	@Override
	public ReservationResponse createReservation(ReservationRequest request) {
		if (!isDateRangeValid(request.getCheckInDate(), request.getCheckOutDate())) {
			return ReservationBuilder.builder()
					.status("PENDING")
					.message("Booking dates must be today or earlier, and check-out must be after check-in.")
					.build();
		}

		long reservationId = sequence.incrementAndGet();
		int guests = request.getNumGuests() == null ? 1 : request.getNumGuests();

		ReservationResponse response = ReservationBuilder.builder()
				.reservationId(reservationId)
				.guestId(request.getGuestId())
				.roomId(request.getRoomId())
				.checkInDate(request.getCheckInDate())
				.checkOutDate(request.getCheckOutDate())
				.numGuests(guests)
				.specialRequests(request.getSpecialRequests())
				.totalAmount(calculateTotal(request.getCheckInDate(), request.getCheckOutDate()))
				.status("PENDING")
				.message("Reservation created. Proceed to payment.")
				.build();

		reservations.put(reservationId, response);
		return response;
	}

	@Override
	public ReservationResponse confirmReservation(Long reservationId) {
		ReservationResponse existing = reservations.get(reservationId);
		if (existing == null) {
			return ReservationBuilder.builder()
					.reservationId(reservationId)
					.status("PENDING")
					.message("Reservation not found.")
					.build();
		}

		ReservationResponse confirmed = ReservationBuilder.builder()
				.reservationId(existing.getReservationId())
				.guestId(existing.getGuestId())
				.roomId(existing.getRoomId())
				.checkInDate(existing.getCheckInDate())
				.checkOutDate(existing.getCheckOutDate())
				.numGuests(existing.getNumGuests())
				.specialRequests(existing.getSpecialRequests())
				.totalAmount(existing.getTotalAmount())
				.status("CONFIRMED")
				.message("Reservation confirmed after payment.")
				.build();

		reservations.put(reservationId, confirmed);
		return confirmed;
	}

	@Override
	public ReservationResponse modifyReservation(Long reservationId, ModifyReservationRequest request) {
		ReservationResponse existing = reservations.get(reservationId);
		if (existing == null) {
			return ReservationBuilder.builder()
					.reservationId(reservationId)
					.status("PENDING")
					.message("Reservation not found.")
					.build();
		}

		LocalDate updatedCheckIn = request.getCheckInDate() == null ? existing.getCheckInDate() : request.getCheckInDate();
		LocalDate updatedCheckOut = request.getCheckOutDate() == null ? existing.getCheckOutDate() : request.getCheckOutDate();
		if (!isDateRangeValid(updatedCheckIn, updatedCheckOut)) {
			return ReservationBuilder.builder()
					.reservationId(reservationId)
					.status(existing.getStatus())
					.message("Check-out date must be after check-in date.")
					.build();
		}

		ReservationResponse updated = ReservationBuilder.builder()
				.reservationId(existing.getReservationId())
				.guestId(existing.getGuestId())
				.roomId(existing.getRoomId())
				.checkInDate(updatedCheckIn)
				.checkOutDate(updatedCheckOut)
				.numGuests(request.getNumGuests() == null ? existing.getNumGuests() : request.getNumGuests())
				.specialRequests(request.getSpecialRequests() == null ? existing.getSpecialRequests() : request.getSpecialRequests())
				.totalAmount(calculateTotal(updatedCheckIn, updatedCheckOut))
				.status("CONFIRMED")
				.message("Reservation modified successfully.")
				.build();

		reservations.put(reservationId, updated);
		return updated;
	}

	@Override
	public ReservationResponse cancelReservation(Long reservationId) {
		ReservationResponse existing = reservations.get(reservationId);
		if (existing == null) {
			return ReservationBuilder.builder()
					.reservationId(reservationId)
					.status("PENDING")
					.message("Reservation not found.")
					.build();
		}

		ReservationResponse cancelled = ReservationBuilder.builder()
				.reservationId(existing.getReservationId())
				.guestId(existing.getGuestId())
				.roomId(existing.getRoomId())
				.checkInDate(existing.getCheckInDate())
				.checkOutDate(existing.getCheckOutDate())
				.numGuests(existing.getNumGuests())
				.specialRequests(existing.getSpecialRequests())
				.totalAmount(existing.getTotalAmount())
				.status("CANCELLED")
				.message("Reservation cancelled successfully.")
				.build();

		reservations.put(reservationId, cancelled);
		return cancelled;
	}

	@Override
	public List<ReservationResponse> getReservationHistory(Long guestId) {
		if (guestId == null) {
			return reservations.values().stream().toList();
		}
		return reservations.values().stream()
				.filter(reservation -> guestId.equals(reservation.getGuestId()))
				.toList();
	}

	@Override
	public ReservationResponse getReservationById(Long reservationId) {
		return reservations.get(reservationId);
	}

	private boolean isDateRangeValid(LocalDate checkInDate, LocalDate checkOutDate) {
		LocalDate today = LocalDate.now();
		return checkInDate != null && checkOutDate != null
				&& !checkInDate.isAfter(today)
				&& !checkOutDate.isAfter(today)
				&& checkOutDate.isAfter(checkInDate);
	}

	private BigDecimal calculateTotal(LocalDate checkInDate, LocalDate checkOutDate) {
		long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
		return DEFAULT_PRICE_PER_NIGHT.multiply(BigDecimal.valueOf(nights));
	}
}
