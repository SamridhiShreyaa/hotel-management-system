package com.pesu.hotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pesu.hotel.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByUserId(Long userId);
}
