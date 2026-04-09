package com.hotel.hotel_management.controller;

import com.hotel.hotel_management.model.Room;
import com.hotel.hotel_management.service.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoomService roomService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public AdminController(UserService u, RoomService r,
                           BookingService b, PaymentService p) {
        this.userService = u;
        this.roomService = r;
        this.bookingService = b;
        this.paymentService = p;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalRooms", roomService.countAll());
        model.addAttribute("availableRooms", roomService.countAvailable());
        model.addAttribute("totalBookings", bookingService.countAll());
        model.addAttribute("activeBookings", bookingService.countBooked());
        model.addAttribute("totalGuests", userService.countGuests());
        model.addAttribute("totalRevenue", paymentService.totalRevenue());
        model.addAttribute("recentBookings", bookingService.findAll().stream().limit(5).toList());
        model.addAttribute("paymentLogs", paymentService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {
        model.addAttribute("rooms", roomService.findAll());
        return "admin/rooms";
    }

    @PostMapping("/rooms/add")
    public String addRoom(@RequestParam String roomNumber,
                          @RequestParam String roomType,
                          @RequestParam BigDecimal price,
                          @RequestParam(defaultValue = "2") Integer maxGuests,
                          @RequestParam(required = false) String description,
                          RedirectAttributes ra) {

        
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        room.setPricePerNight(price);
        room.setMaxGuests(maxGuests);
        room.setDescription(description);
        room.setHotelName("Grand Hotel");
        room.setStatus("AVAILABLE");

        roomService.save(room);

        ra.addFlashAttribute("success", "Room " + roomNumber + " added.");
        return "redirect:/admin/rooms";
    }

    @PostMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes ra) {
        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (bookingService.hasBookings(room)) {
            ra.addFlashAttribute("error", "Room cannot be deleted because it has existing bookings.");
            return "redirect:/admin/rooms";
        }

        try {
            roomService.deleteById(id);
            ra.addFlashAttribute("success", "Room deleted.");
        } catch (DataIntegrityViolationException ex) {
            ra.addFlashAttribute("error", "Room could not be deleted due to related data.");
        }

        return "redirect:/admin/rooms";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        return "admin/bookings";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes ra) {
        bookingService.cancel(id);
        ra.addFlashAttribute("success", "Booking cancelled.");
        return "redirect:/admin/bookings";
    }

    @GetMapping("/guests")
    public String guests(Model model) {
        model.addAttribute("guests", userService.findAllGuests());
        return "admin/guests";
    }
}