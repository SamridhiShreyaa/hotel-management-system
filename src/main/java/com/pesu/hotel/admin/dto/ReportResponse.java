package com.pesu.hotel.admin.dto;

import java.math.BigDecimal;

public class ReportResponse {
	private String title;
	private int occupiedRooms;
	private int availableRooms;
	private BigDecimal revenue;

	public ReportResponse(String title, int occupiedRooms, int availableRooms, BigDecimal revenue) {
		this.title = title;
		this.occupiedRooms = occupiedRooms;
		this.availableRooms = availableRooms;
		this.revenue = revenue;
	}

	public String getTitle() {
		return title;
	}

	public int getOccupiedRooms() {
		return occupiedRooms;
	}

	public int getAvailableRooms() {
		return availableRooms;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}
}
