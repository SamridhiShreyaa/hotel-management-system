package com.pesu.hotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pesu.hotel.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
	Optional<Guest> findByUserId(Long userId);
}
