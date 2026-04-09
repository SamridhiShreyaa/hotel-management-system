package com.hotel.hotel_management.repository;

import com.hotel.hotel_management.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(String status);
    List<Room> findByStatusAndRoomType(String status, String roomType);
    boolean existsByRoomNumber(String roomNumber);
}