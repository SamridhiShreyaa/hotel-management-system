package com.pesu.hotel.admin.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pesu.hotel.admin.dto.ReportResponse;
import com.pesu.hotel.admin.dto.RoomManageRequest;

@Service
public class AdminServiceImpl implements AdminService {

	@Override
	public List<RoomManageRequest> getRooms() {
		RoomManageRequest room1 = new RoomManageRequest();
		room1.setHotelName("The Grand Palace");
		room1.setRoomNumber("101");
		room1.setRoomType("SINGLE");
		room1.setStatus("AVAILABLE");

		RoomManageRequest room2 = new RoomManageRequest();
		room2.setHotelName("City Comfort Inn");
		room2.setRoomNumber("201");
		room2.setRoomType("DELUXE");
		room2.setStatus("RESERVED");

		return List.of(room1, room2);
	}

	@Override
	public List<String> getReservations() {
		return List.of(
				"R-1001 | Ravi Kumar | CONFIRMED",
				"R-1002 | Ananya Sharma | CHECKED_IN");
	}

	@Override
	public List<String> getGuests() {
		return List.of(
				"Ravi Kumar | ravi@example.com",
				"Ananya Sharma | ananya@example.com");
	}

	@Override
	public List<ReportResponse> getReports() {
		return List.of(
				new ReportResponse("Occupancy Summary", 1, 1, new BigDecimal("4500.00")),
				new ReportResponse("Revenue Summary", 2, 0, new BigDecimal("12800.00")));
	}
}
