package com.taras.arenda.Service;


import com.taras.arenda.dto.CityDto;
import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CityService {

    private final CityRepository cityRepo;

    public Page<CityDto> findByName(String name, Pageable page) {
        ModelMapper modelMapper = new ModelMapper();
        Page<City> cities;
        if (name == null || name.trim().isEmpty()) {
            cities = cityRepo.findAll(page);
        } else {
            cities = cityRepo.findByNameContainsAllIgnoreCase(name, page);
        }
        return cities.map(city -> modelMapper.map(city, CityDto.class));
    }

    public CityDto getCity(String cityId) {
        City city = cityRepo.findByCityId(cityId)
                .orElseThrow(ResourceNotFoundException::new);
        return new ModelMapper().map(city, CityDto.class);
    }

    public CityDto createCity(CityDto cityDto) {
        cityDto.setCityId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        City city = modelMapper.map(cityDto, City.class);

        City savedCity = cityRepo.save(city);

        return modelMapper.map(savedCity, CityDto.class);
    }

    @Transactional
    public void deleteCity(String cityId) {
        cityRepo.deleteByCityId(cityId);
    }
}
