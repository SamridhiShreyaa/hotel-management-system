package com.pesu.hotel.auth.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pesu.hotel.auth.dto.RoomSearchRequest;
import com.pesu.hotel.auth.dto.RoomSearchResult;

@Service
public class RoomSearchService {

	public List<RoomSearchResult> searchAvailableRooms(RoomSearchRequest request) {
		List<RoomSearchResult> allRooms = List.of(
				new RoomSearchResult(101L, "The Grand Palace", "101", "SINGLE", new BigDecimal("2500.00"), 1),
				new RoomSearchResult(102L, "The Grand Palace", "102", "DOUBLE", new BigDecimal("4500.00"), 2),
				new RoomSearchResult(201L, "City Comfort Inn", "201", "DELUXE", new BigDecimal("3200.00"), 2));

		int requiredGuests = request.getGuests() == null ? 1 : request.getGuests();
		String requestedCity = request.getCity() == null ? "" : request.getCity().trim().toLowerCase();

		List<RoomSearchResult> filtered = new ArrayList<>();
		for (RoomSearchResult room : allRooms) {
			boolean matchesGuests = room.getMaxOccupancy() >= requiredGuests;
			boolean matchesCity = requestedCity.isBlank() || room.getHotelName().toLowerCase().contains(requestedCity);
			if (matchesGuests && matchesCity) {
				filtered.add(room);
			}
		}
		return filtered;
	}
}
