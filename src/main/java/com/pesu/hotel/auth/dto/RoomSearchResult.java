package com.pesu.hotel.auth.dto;

import java.math.BigDecimal;

public class RoomSearchResult {
    private String hotelName;
    private String roomNumber;
    private String roomType;
    private BigDecimal pricePerNight;
    private int maxOccupancy;

    public RoomSearchResult(String hotelName, String roomNumber, String roomType, BigDecimal pricePerNight, int maxOccupancy) {
        this.hotelName = hotelName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.maxOccupancy = maxOccupancy;
    }

    public String getHotelName() {
        return hotelName;
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

    public int getMaxOccupancy() {
        return maxOccupancy;
    }
}
