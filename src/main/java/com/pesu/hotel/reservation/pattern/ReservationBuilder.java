package com.pesu.hotel.reservation.pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pesu.hotel.reservation.dto.ReservationResponse;

public final class ReservationBuilder {

	private ReservationBuilder() {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private final ReservationResponse response = new ReservationResponse();

		public Builder reservationId(Long reservationId) {
			response.setReservationId(reservationId);
			return this;
		}

		public Builder guestId(Long guestId) {
			response.setGuestId(guestId);
			return this;
		}

		public Builder roomId(Long roomId) {
			response.setRoomId(roomId);
			return this;
		}

		public Builder checkInDate(LocalDate checkInDate) {
			response.setCheckInDate(checkInDate);
			return this;
		}

		public Builder checkOutDate(LocalDate checkOutDate) {
			response.setCheckOutDate(checkOutDate);
			return this;
		}

		public Builder numGuests(Integer numGuests) {
			response.setNumGuests(numGuests);
			return this;
		}

		public Builder totalAmount(BigDecimal totalAmount) {
			response.setTotalAmount(totalAmount);
			return this;
		}

		public Builder status(String status) {
			response.setStatus(status);
			return this;
		}

		public Builder specialRequests(String specialRequests) {
			response.setSpecialRequests(specialRequests);
			return this;
		}

		public Builder message(String message) {
			response.setMessage(message);
			return this;
		}

		public ReservationResponse build() {
			return response;
		}
	}
}
