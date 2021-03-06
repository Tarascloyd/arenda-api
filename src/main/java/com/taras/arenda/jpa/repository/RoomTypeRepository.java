package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findAllByHotel(Hotel hotel);
    List<RoomType> findTop5ByOrderByPriceAsc();
}
