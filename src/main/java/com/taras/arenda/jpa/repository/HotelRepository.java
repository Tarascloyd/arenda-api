package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.domain.City;
import com.taras.arenda.jpa.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCity(City city);

    List<Hotel> findByNameContainsAllIgnoreCase(String name);
}
