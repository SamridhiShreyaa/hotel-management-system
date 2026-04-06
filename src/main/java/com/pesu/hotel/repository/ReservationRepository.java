package com.pesu.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pesu.hotel.entity.Guest;
import com.pesu.hotel.entity.Reservation;
import com.pesu.hotel.entity.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByGuest(Guest guest);

	List<Reservation> findByGuestAndStatus(Guest guest, ReservationStatus status);

	List<Reservation> findByStatus(ReservationStatus status);
}
