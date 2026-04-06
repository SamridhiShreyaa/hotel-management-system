package com.pesu.hotel.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.pesu.hotel.auth.dto.LoginRequest;
import com.pesu.hotel.auth.dto.LoginResponse;
import com.pesu.hotel.auth.dto.RegisterRequest;
import com.pesu.hotel.auth.dto.RoomSearchRequest;
import com.pesu.hotel.auth.dto.RoomSearchResult;
import com.pesu.hotel.auth.service.AuthServiceImpl;
import com.pesu.hotel.auth.service.RoomSearchService;

class AuthServiceTest {

	@Test
	void registerAndLoginShouldSucceedWithValidCredentials() {
		AuthServiceImpl authService = new AuthServiceImpl();

		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setFullName("Ravi Kumar");
		registerRequest.setEmail("ravi@example.com");
		registerRequest.setPhone("9999999999");
		registerRequest.setPassword("guest123");

		LoginResponse registerResponse = authService.register(registerRequest);
		assertTrue(registerResponse.isAuthenticated());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("ravi@example.com");
		loginRequest.setPassword("guest123");

		LoginResponse loginResponse = authService.login(loginRequest);
		assertTrue(loginResponse.isAuthenticated());
	}

	@Test
	void duplicateRegistrationShouldFail() {
		AuthServiceImpl authService = new AuthServiceImpl();

		RegisterRequest first = new RegisterRequest();
		first.setFullName("Test User");
		first.setEmail("test@example.com");
		first.setPassword("password1");

		RegisterRequest duplicate = new RegisterRequest();
		duplicate.setFullName("Test User 2");
		duplicate.setEmail("test@example.com");
		duplicate.setPassword("password2");

		authService.register(first);
		LoginResponse duplicateResponse = authService.register(duplicate);

		assertFalse(duplicateResponse.isAuthenticated());
		assertEquals("Email already registered.", duplicateResponse.getMessage());
	}

	@Test
	void roomSearchShouldFilterByGuestCount() {
		RoomSearchService roomSearchService = new RoomSearchService();
		RoomSearchRequest request = new RoomSearchRequest();
		request.setGuests(2);

		List<RoomSearchResult> results = roomSearchService.searchAvailableRooms(request);

		assertFalse(results.isEmpty());
		assertTrue(results.stream().allMatch(room -> room.getMaxOccupancy() >= 2));
	}
}
