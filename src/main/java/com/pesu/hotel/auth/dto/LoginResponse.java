package com.pesu.hotel.auth.dto;

public class LoginResponse {
	private boolean authenticated;
	private String message;

	public LoginResponse() {
	}

	public LoginResponse(boolean authenticated, String message) {
		this.authenticated = authenticated;
		this.message = message;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
