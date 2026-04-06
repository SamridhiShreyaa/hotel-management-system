package com.pesu.hotel.auth.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pesu.hotel.auth.dto.LoginRequest;
import com.pesu.hotel.auth.dto.LoginResponse;
import com.pesu.hotel.auth.dto.RegisterRequest;

@Service
public class AuthServiceImpl implements AuthService {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final Map<String, LocalUserRecord> usersByEmail = new ConcurrentHashMap<>();

	@Override
	public LoginResponse register(RegisterRequest request) {
		if (request.getEmail() == null || request.getEmail().isBlank()) {
			return new LoginResponse(false, "Email is required.");
		}
		if (request.getPassword() == null || request.getPassword().length() < 6) {
			return new LoginResponse(false, "Password must be at least 6 characters.");
		}

		String email = request.getEmail().trim().toLowerCase();
		if (usersByEmail.containsKey(email)) {
			return new LoginResponse(false, "Email already registered.");
		}

		LocalUserRecord record = new LocalUserRecord(
				request.getFullName(),
				email,
				request.getPhone(),
				passwordEncoder.encode(request.getPassword()));
		usersByEmail.put(email, record);
		return new LoginResponse(true, "Registration successful. Please login.");
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		if (request.getEmail() == null || request.getPassword() == null) {
			return new LoginResponse(false, "Invalid credentials.");
		}

		LocalUserRecord record = usersByEmail.get(request.getEmail().trim().toLowerCase());
		if (record == null || !passwordEncoder.matches(request.getPassword(), record.passwordHash())) {
			return new LoginResponse(false, "Invalid email or password.");
		}

		return new LoginResponse(true, "Welcome, " + record.fullName() + "!");
	}

	private record LocalUserRecord(String fullName, String email, String phone, String passwordHash) {
	}
}
