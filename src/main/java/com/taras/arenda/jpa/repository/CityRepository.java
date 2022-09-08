package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CityRepository  extends JpaRepository<City, Long> {
    Page<City> findByNameContainsAllIgnoreCase(String theName, Pageable page);

    Optional<City> findByCityId(String cityId);

    @Modifying
    @Query("update City c set c.deleted = true where c.cityId = ?1")
    void deleteByCityId(String cityId);
}
