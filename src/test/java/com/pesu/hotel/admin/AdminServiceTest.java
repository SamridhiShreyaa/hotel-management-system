package com.pesu.hotel.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pesu.hotel.admin.service.AdminServiceImpl;

class AdminServiceTest {

	@Test
	void dashboardDataShouldContainRoomsReservationsGuestsAndReports() {
		AdminServiceImpl service = new AdminServiceImpl();

		assertEquals(2, service.getRooms().size());
		assertEquals(2, service.getReservations().size());
		assertEquals(2, service.getGuests().size());
		assertEquals(2, service.getReports().size());
	}

	@Test
	void roomStatusShouldIncludeAvailableAndReservedSamples() {
		AdminServiceImpl service = new AdminServiceImpl();

		boolean hasAvailable = service.getRooms().stream().anyMatch(room -> "AVAILABLE".equals(room.getStatus()));
		boolean hasReserved = service.getRooms().stream().anyMatch(room -> "RESERVED".equals(room.getStatus()));

		assertTrue(hasAvailable);
		assertTrue(hasReserved);
	}

	@Test
	void reportValuesShouldBePositive() {
		AdminServiceImpl service = new AdminServiceImpl();

		assertTrue(service.getReports().stream().allMatch(report -> report.getRevenue().doubleValue() > 0));
		assertFalse(service.getReports().isEmpty());
	}
}
