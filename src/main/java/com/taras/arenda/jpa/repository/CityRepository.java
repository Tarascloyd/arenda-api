package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository  extends JpaRepository<City, Long> {
    List<City> findByNameContainsAllIgnoreCase(String theName);
}
