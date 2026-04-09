package com.hotel.hotel_management.controller;

import java.util.List;
import com.hotel.hotel_management.model.*;
import com.hotel.hotel_management.service.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ✅ ADD THIS

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final PaymentService paymentService;
    private final RoomService roomService;

    public UserController(UserService userService,
                          BookingService bookingService,
                          PaymentService paymentService,
                          RoomService roomService) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
        this.roomService = roomService;
    }

    // ✅ DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        User user = userService.findByUsername(auth.getName()).orElseThrow();

        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.findByUser(user));
        model.addAttribute("payments", paymentService.findByUser(user));

        return "user/dashboard";
    }

    // ✅ ROOMS PAGE (🔥 THIS WAS MISSING)
    @GetMapping("/rooms")
    public String rooms(@RequestParam(required = false) String type, Model model) {

        List<Room> rooms;

        if (type != null && !type.isEmpty()) {
            rooms = roomService.findAvailableByType(type);
        } else {
            rooms = roomService.findAvailableRooms();
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("selectedType", type);

        return "user/rooms";
    }

    // ✅ MY BOOKINGS (🔥 THIS WAS MISSING)
    @GetMapping("/my-bookings")
    public String myBookings(Model model, Authentication auth) {

        User user = userService.findByUsername(auth.getName()).orElseThrow();

        model.addAttribute("bookings", bookingService.findByUser(user));

        return "user/my-bookings";
    }

    // ✅ BOOK PAGE
    @GetMapping("/book/{id}")
    public String bookPage(@PathVariable Long id, Model model) {

        Room room = roomService.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        model.addAttribute("room", room);

        return "user/booking-form";
    }

    // ✅ BOOK SUBMIT
    @PostMapping("/book")
    public String bookRoom(@RequestParam Long roomId,
                           @RequestParam String checkIn,
                           @RequestParam String checkOut,
                           Authentication auth,
                           RedirectAttributes ra) {

        User user = userService.findByUsername(auth.getName()).orElseThrow();
        Room room = roomService.findById(roomId).orElseThrow();

        var booking = bookingService.book(
                user,
                room,
                java.time.LocalDate.parse(checkIn),
                java.time.LocalDate.parse(checkOut)
        );

        ra.addFlashAttribute("success", "Booking created. Please complete payment.");
        return "redirect:/user/pay/" + booking.getId();
    }

    @GetMapping("/pay/{id}")
    public String paymentPage(@PathVariable Long id, Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        model.addAttribute("booking", booking);
        return "user/payment";
    }

    @PostMapping("/pay")
    public String processPayment(@RequestParam Long bookingId,
                                 @RequestParam String method,
                                 Authentication auth,
                                 RedirectAttributes ra) {

        User user = userService.findByUsername(auth.getName()).orElseThrow();
        Booking booking = bookingService.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        paymentService.recordPayment(booking, user, method);
        bookingService.markPaid(bookingId);
        ra.addFlashAttribute("success", "Payment completed successfully.");
        return "redirect:/user/dashboard";
    }
}