package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findByHotel(Hotel hotel);
}