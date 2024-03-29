package com.taras.arenda.Service;

import com.taras.arenda.dto.CityDto;
import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepo;
    private final CityService cityService;

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

    public Iterable<Hotel> getHotelsByCity(String cityId) {
        CityDto cityDto = cityService.getCity(cityId);
        ModelMapper modelMapper = new ModelMapper();
        return hotelRepo.findAllByCity(modelMapper.map(cityDto, City.class));
    }
}
