package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotel(Hotel hotel);
}
