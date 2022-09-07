package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository  extends JpaRepository<City, Long> {
    Page<City> findByNameContainsAllIgnoreCase(String theName, Pageable page);
}
