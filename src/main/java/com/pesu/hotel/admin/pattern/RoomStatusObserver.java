package com.pesu.hotel.admin.pattern;

import com.pesu.hotel.entity.Reservation;
import com.pesu.hotel.entity.ReservationStatus;
import com.pesu.hotel.entity.RoomStatus;

/**
 * Behavioral Pattern - Observer
 * Keeps room status aligned with reservation status changes.
 */
public final class RoomStatusObserver {

	private RoomStatusObserver() {
	}

	public static RoomStatus mapReservationStatus(Reservation reservation) {
		if (reservation == null || reservation.getStatus() == null) {
			return RoomStatus.AVAILABLE;
		}

		ReservationStatus status = reservation.getStatus();
		return switch (status) {
			case PENDING, CONFIRMED -> RoomStatus.RESERVED;
			case CHECKED_IN -> RoomStatus.CHECKED_IN;
			case COMPLETED, CANCELLED -> RoomStatus.AVAILABLE;
		};
	}
}
