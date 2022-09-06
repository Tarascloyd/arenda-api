package com.taras.arenda.Service;


import com.taras.arenda.dto.CityDto;
import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CityService {

    private final CityRepository cityRepo;
    private final HotelRepository hotelRepo;

    public Iterable<City> findByName(String name) {

        return cityRepo.findByNameContainsAllIgnoreCase(name);
    }

    public City findById(Long id) {

        return cityRepo.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Iterable<City> findAll() {

        return cityRepo.findAll();
    }

    public Iterable<Hotel> getHotels(Long id) {

        City city = findById(id);
        return hotelRepo.findAllByCity(city);
    }

    public CityDto createCity(CityDto cityDto) {
        cityDto.setCityId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        City city = modelMapper.map(cityDto, City.class);

        City savedCity = cityRepo.save(city);

        return modelMapper.map(savedCity, CityDto.class);
    }
}
