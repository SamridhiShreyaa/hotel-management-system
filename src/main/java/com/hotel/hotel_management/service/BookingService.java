package com.hotel.hotel_management.service;

import com.hotel.hotel_management.model.*;
import com.hotel.hotel_management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    // ✅ CREATE BOOKING
    @Transactional
    public Booking book(User user, Room room, LocalDate checkIn, LocalDate checkOut) {

        // 🔥 VALIDATION (IMPORTANT FIX)
        LocalDate today = LocalDate.now();
        if (checkIn.isBefore(today)) {
            throw new RuntimeException("Check-in date cannot be before today");
        }

        if (!checkIn.isBefore(checkOut)) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }

        if (!"AVAILABLE".equals(room.getStatus())) {
            throw new RuntimeException("Room is not available");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal total = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setBookingDate(LocalDate.now());
        booking.setTotalAmount(total);
        booking.setStatus("BOOKED");

        // ✅ Update room status
        room.setStatus("BOOKED");
        roomRepository.save(room);

        return bookingRepository.save(booking);
    }

    // ✅ CANCEL BOOKING
    @Transactional
    public void cancel(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 🔥 Prevent invalid cancel states
        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Booking already cancelled");
        }

        if (!"BOOKED".equals(booking.getStatus())) {
            throw new RuntimeException("Only unpaid bookings can be cancelled");
        }

        booking.setStatus("CANCELLED");

        // ✅ Free room
        Room room = booking.getRoom();
        room.setStatus("AVAILABLE");

        roomRepository.save(room);
        bookingRepository.save(booking);
    }

    @Transactional
    public void markPaid(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot pay a cancelled booking");
        }

        booking.setStatus("PAID");
        bookingRepository.save(booking);
    }

    // ✅ USER BOOKINGS
    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    public boolean hasBookings(Room room) {
        return bookingRepository.existsByRoom(room);
    }

    // ✅ ALL BOOKINGS
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    // ✅ STATS
    public long countAll() {
        return bookingRepository.count();
    }

    public long countBooked() {
        return bookingRepository.countByStatus("BOOKED");
    }
}