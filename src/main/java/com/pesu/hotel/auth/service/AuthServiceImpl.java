package com.pesu.hotel.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pesu.hotel.auth.dto.LoginRequest;
import com.pesu.hotel.auth.dto.LoginResponse;
import com.pesu.hotel.auth.dto.RegisterRequest;
import com.pesu.hotel.entity.Guest;
import com.pesu.hotel.entity.IdProofType;
import com.pesu.hotel.entity.UserRole;
import com.pesu.hotel.repository.GuestRepository;
import com.pesu.hotel.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	private final GuestRepository guestRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(GuestRepository guestRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.guestRepository = guestRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public LoginResponse register(RegisterRequest request) {
		if (request.getEmail() == null || request.getEmail().isBlank()) {
			return new LoginResponse(false, "Email is required.");
		}
		if (request.getPassword() == null || request.getPassword().length() < 6) {
			return new LoginResponse(false, "Password must be at least 6 characters.");
		}
		if (request.getIdProofType() == null || request.getIdProofType().isBlank()) {
			return new LoginResponse(false, "Identification proof type is required.");
		}
		if (request.getIdProofNo() == null || request.getIdProofNo().isBlank()) {
			return new LoginResponse(false, "Identification proof number is required.");
		}

		String email = request.getEmail().trim().toLowerCase();
		if (userRepository.existsByEmail(email)) {
			return new LoginResponse(false, "Email already registered.");
		}

		Guest guest = new Guest();
		guest.setFullName(request.getFullName());
		guest.setEmail(email);
		guest.setPhone(request.getPhone());
		guest.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		guest.setRole(UserRole.GUEST);
		guest.setAddress(request.getAddress());
		guest.setNationality(request.getNationality());
		guest.setIdProofType(IdProofType.valueOf(request.getIdProofType()));
		guest.setIdProofNo(request.getIdProofNo());
		guestRepository.save(guest);
		return new LoginResponse(true, "Registration successful. Please login.");
	}

	@Override
	public LoginResponse login(LoginRequest request) {
		if (request.getEmail() == null || request.getPassword() == null) {
			return new LoginResponse(false, "Invalid credentials.");
		}

		return userRepository.findByEmail(request.getEmail().trim().toLowerCase())
				.filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
				.map(user -> new LoginResponse(true, "Welcome, " + user.getFullName() + "!"))
				.orElseGet(() -> new LoginResponse(false, "Invalid email or password."));
	}
}
