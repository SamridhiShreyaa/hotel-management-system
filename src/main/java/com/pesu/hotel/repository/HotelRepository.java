package com.pesu.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pesu.hotel.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
	List<Hotel> findByCity(String city);
}
