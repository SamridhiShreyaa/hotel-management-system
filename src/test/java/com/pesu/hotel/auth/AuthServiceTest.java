package com.pesu.hotel.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.pesu.hotel.auth.dto.LoginRequest;
import com.pesu.hotel.auth.dto.LoginResponse;
import com.pesu.hotel.auth.dto.RegisterRequest;
import com.pesu.hotel.auth.dto.RoomSearchRequest;
import com.pesu.hotel.auth.dto.RoomSearchResult;
import com.pesu.hotel.auth.service.AuthServiceImpl;
import com.pesu.hotel.auth.service.RoomSearchService;
import com.pesu.hotel.entity.Guest;
import com.pesu.hotel.entity.User;
import com.pesu.hotel.repository.GuestRepository;
import com.pesu.hotel.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthServiceTest {

	@Test
	void registerAndLoginShouldSucceedWithValidCredentials() {
		GuestRepository guestRepository = mock(GuestRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		AuthServiceImpl authService = new AuthServiceImpl(guestRepository, userRepository, passwordEncoder);

		RegisterRequest registerRequest = new RegisterRequest();
		registerRequest.setFullName("Ravi Kumar");
		registerRequest.setEmail("ravi@example.com");
		registerRequest.setPhone("9999999999");
		registerRequest.setPassword("guest123");

		when(userRepository.existsByEmail("ravi@example.com")).thenReturn(false);
		when(guestRepository.save(any(Guest.class))).thenAnswer(invocation -> invocation.getArgument(0));

		LoginResponse registerResponse = authService.register(registerRequest);
		assertTrue(registerResponse.isAuthenticated());
		assertEquals("Registration successful. Please login.", registerResponse.getMessage());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail("ravi@example.com");
		loginRequest.setPassword("guest123");
		User storedUser = new User();
		storedUser.setFullName("Ravi Kumar");
		storedUser.setEmail("ravi@example.com");
		storedUser.setPasswordHash(passwordEncoder.encode("guest123"));
		when(userRepository.findByEmail("ravi@example.com")).thenReturn(Optional.of(storedUser));

		LoginResponse loginResponse = authService.login(loginRequest);
		assertTrue(loginResponse.isAuthenticated());
	}

	@Test
	void duplicateRegistrationShouldFail() {
		GuestRepository guestRepository = mock(GuestRepository.class);
		UserRepository userRepository = mock(UserRepository.class);
		AuthServiceImpl authService = new AuthServiceImpl(guestRepository, userRepository, new BCryptPasswordEncoder());

		RegisterRequest first = new RegisterRequest();
		first.setFullName("Test User");
		first.setEmail("test@example.com");
		first.setPassword("password1");

		RegisterRequest duplicate = new RegisterRequest();
		duplicate.setFullName("Test User 2");
		duplicate.setEmail("test@example.com");
		duplicate.setPassword("password2");

		when(userRepository.existsByEmail("test@example.com")).thenReturn(false, true);

		LoginResponse firstResponse = authService.register(first);
		assertTrue(firstResponse.isAuthenticated());
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
