package com.hotel.hotel_management.service;

import com.hotel.hotel_management.model.User;
import com.hotel.hotel_management.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    // ✅ FIXED: encoder passed as parameter
    public User register(User user, String encodedPassword) {
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllGuests() {
        return userRepository.findAll().stream()
            .filter(u -> "ROLE_USER".equals(u.getRole()))
            .toList();
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public long countGuests() {
        return userRepository.findAll().stream()
            .filter(u -> "ROLE_USER".equals(u.getRole()))
            .count();
    }
}