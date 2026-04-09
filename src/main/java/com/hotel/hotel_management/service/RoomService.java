package com.hotel.hotel_management.service;

import com.hotel.hotel_management.model.Room;
import com.hotel.hotel_management.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll()          { return roomRepository.findAll(); }
    public List<Room> findAvailable()    { return roomRepository.findByStatus("AVAILABLE"); }
    public Optional<Room> findById(Long id) { return roomRepository.findById(id); }
    public Room save(Room room)          { return roomRepository.save(room); }
    public void deleteById(Long id)      { roomRepository.deleteById(id); }
    public long countAll()               { return roomRepository.count(); }
    public long countAvailable()         { return roomRepository.findByStatus("AVAILABLE").size(); }

    public List<Room> findAvailableByType(String type) {
        return roomRepository.findByStatusAndRoomType("AVAILABLE", type);
    }

    public List<Room> findAvailableRooms() {
    return roomRepository.findAll()
            .stream()
            .filter(r -> "AVAILABLE".equals(r.getStatus()))
            .toList();
}
}