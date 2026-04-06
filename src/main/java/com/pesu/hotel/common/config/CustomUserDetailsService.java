package com.pesu.hotel.common.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pesu.hotel.entity.User;
import com.pesu.hotel.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username.trim().toLowerCase())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), List.of(authority));
	}
}