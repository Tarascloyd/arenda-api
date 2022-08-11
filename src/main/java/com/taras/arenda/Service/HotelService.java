package com.taras.arenda.Service;

import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepo;


    public Iterable<Hotel> findAll() {

        return hotelRepo.findAll();
    }

    public Iterable<Hotel> findByName(String name) {

        return hotelRepo.findByNameContainsAllIgnoreCase(name);
    }

    public Hotel findById(Long id) {
        return hotelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel"));
    }
}
