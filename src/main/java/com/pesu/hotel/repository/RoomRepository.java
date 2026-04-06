package com.pesu.hotel.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pesu.hotel.entity.Room;
import com.pesu.hotel.entity.RoomStatus;
import com.pesu.hotel.entity.RoomType;

public interface RoomRepository extends JpaRepository<Room, Long> {
	List<Room> findByHotelHotelIdAndStatus(Long hotelId, RoomStatus status);

	List<Room> findByRoomTypeAndStatus(RoomType type, RoomStatus status);

	@Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId AND r.status = com.pesu.hotel.entity.RoomStatus.AVAILABLE " +
			"AND r.roomId NOT IN (SELECT res.room.roomId FROM Reservation res " +
			"WHERE res.status <> com.pesu.hotel.entity.ReservationStatus.CANCELLED " +
			"AND res.checkInDate < :checkOut AND res.checkOutDate > :checkIn)")
	List<Room> findAvailableRooms(@Param("hotelId") Long hotelId,
								  @Param("checkIn") LocalDate checkIn,
								  @Param("checkOut") LocalDate checkOut);

	@Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId AND r.pricePerNight <= :maxPrice")
	List<Room> findByHotelAndMaxPrice(@Param("hotelId") Long hotelId, @Param("maxPrice") BigDecimal maxPrice);
}
