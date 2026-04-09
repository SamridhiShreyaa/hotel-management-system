package com.hotel.hotel_management.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String roomType;

    @Column(nullable = false)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private String status;

    private Integer maxGuests;
    private String description;
    private String hotelName;

    // ✅ GETTERS

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public String getDescription() {
        return description;
    }

    public String getHotelName() {
        return hotelName;
    }

    // ✅ SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
}