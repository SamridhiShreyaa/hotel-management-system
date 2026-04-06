package com.pesu.hotel.auth.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class RoomSearchRequest {
	private String city;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkInDate;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkOutDate;
	private Integer guests;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public LocalDate getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDate checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDate getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDate checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Integer getGuests() {
		return guests;
	}

	public void setGuests(Integer guests) {
		this.guests = guests;
	}
}
