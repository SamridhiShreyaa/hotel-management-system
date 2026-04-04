package com.pesu.hotel.reservation.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class ModifyReservationRequest {
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkInDate;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate checkOutDate;
	private Integer numGuests;
	private String specialRequests;

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

	public Integer getNumGuests() {
		return numGuests;
	}

	public void setNumGuests(Integer numGuests) {
		this.numGuests = numGuests;
	}

	public String getSpecialRequests() {
		return specialRequests;
	}

	public void setSpecialRequests(String specialRequests) {
		this.specialRequests = specialRequests;
	}
}
