package com.pesu.hotel.auth.service;

import com.pesu.hotel.auth.dto.LoginRequest;
import com.pesu.hotel.auth.dto.LoginResponse;
import com.pesu.hotel.auth.dto.RegisterRequest;

public interface AuthService {
	LoginResponse register(RegisterRequest request);

	LoginResponse login(LoginRequest request);
}
