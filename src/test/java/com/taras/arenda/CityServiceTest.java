package com.taras.arenda;


import com.taras.arenda.Service.CityService;
import com.taras.arenda.dto.CityDto;
import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepo;

    @InjectMocks
    private CityService cityService;

    private List<City> allCities;
    private ModelMapper mapper = new ModelMapper();

    @BeforeEach
    public void setUp() {
        this.allCities = IntStream.rangeClosed(1,3).mapToObj(i -> "City" + i)
                .map(TestUtil::createValidCityDto)
                .peek(c -> c.setCityId(UUID.randomUUID().toString()))
                .map(c -> mapper.map(c, City.class)).collect(Collectors.toList());
    }

    @Test
    public void findAll_whenThereAreCities_ReturnsAllCities() {
        Page<City> pagedResponse = new PageImpl(this.allCities);
        Pageable pageable = PageRequest.of(0, 10);
        when(cityRepo.findAll(pageable)).thenReturn(pagedResponse);

        Page<CityDto> returnedCities = cityService.findAllByName(null, pageable);

        assertThat(returnedCities.getTotalElements()).isEqualTo(allCities.size());
        verify(cityRepo, times(1)).findAll(pageable);
    }

    @Test
    public void findAll_whenThereAreCities_ReturnsPageCityWithName() {
        Page<City> pagedResponse = new PageImpl(this.allCities);
        Pageable pageable = PageRequest.of(0, 10);
        when(cityRepo.findAll(pageable)).thenReturn(pagedResponse);

        Page<CityDto> returnedCities = cityService.findAllByName(null, pageable);

        assertThat(returnedCities.getContent().get(0).getName()).isEqualTo(allCities.get(0).getName());
        verify(cityRepo, times(1)).findAll(pageable);
    }

    @Test
    public void findAll_whenThereAreCities_ReturnsPageCityWithAbout() {
        Page<City> pagedResponse = new PageImpl(this.allCities);
        Pageable pageable = PageRequest.of(0, 10);
        when(cityRepo.findAll(pageable)).thenReturn(pagedResponse);

        Page<CityDto> returnedCities = cityService.findAllByName(null, pageable);

        assertThat(returnedCities.getContent().get(0).getAbout()).isEqualTo(allCities.get(0).getAbout());
        verify(cityRepo, times(1)).findAll(pageable);
    }

    @Test
    public void findAll_whenThereAreCities_ReturnsPageCityWithCityId() {
        Page<City> pagedResponse = new PageImpl(this.allCities);
        Pageable pageable = PageRequest.of(0, 10);
        when(cityRepo.findAll(pageable)).thenReturn(pagedResponse);

        Page<CityDto> returnedCities = cityService.findAllByName(null, pageable);

        assertThat(returnedCities.getContent().get(0).getCityId()).isEqualTo(allCities.get(0).getCityId());
        verify(cityRepo, times(1)).findAll(pageable);
    }

    @Test
    public void findAll_whenThereAreNoCities_ReturnsZeroElements() {
        this.allCities = new ArrayList<>();
        Page<City> pagedResponse = new PageImpl(this.allCities);
        Pageable pageable = PageRequest.of(0, 10);
        when(cityRepo.findAll(pageable)).thenReturn(pagedResponse);

        Page<CityDto> returnedCities = cityService.findAllByName(null, pageable);

        assertThat(returnedCities.getTotalElements()).isEqualTo(0);
        verify(cityRepo, times(1)).findAll(pageable);
    }

    @Test
    public void findAll_whenThereAreCitiesAndNameIsNotNullAndEqualFor1_Returns1City() {
        Pageable pageable = PageRequest.of(0, 10);
        String name = "City1";
        Page<City> pagedResponse = new PageImpl(
                this.allCities.stream().filter(c -> name.equals(c.getName())).collect(Collectors.toList()));
        when(cityRepo.findByNameContainsAllIgnoreCase(name, pageable))
                .thenReturn(pagedResponse);
        Page<CityDto> returnedCities = cityService.findAllByName(name, pageable);

        assertThat(returnedCities.getTotalElements()).isEqualTo(1);
        verify(cityRepo, times(0)).findAll(pageable);
        verify(cityRepo, times(1)).findByNameContainsAllIgnoreCase(name, pageable);
    }

    @Test
    public void findAll_whenThereAreCitiesAndNameIsNotNullAndNotEqualForAny_ReturnsZeroCities() {
        Pageable pageable = PageRequest.of(0, 10);
        String name = "RandomCity";
        Page<City> pagedResponse = new PageImpl(
                this.allCities.stream().filter(c -> name.equals(c.getName())).collect(Collectors.toList()));
        when(cityRepo.findByNameContainsAllIgnoreCase(name, pageable))
                .thenReturn(pagedResponse);
        Page<CityDto> returnedCities = cityService.findAllByName(name, pageable);

        assertThat(returnedCities.getTotalElements()).isEqualTo(0);
        verify(cityRepo, times(0)).findAll(pageable);
        verify(cityRepo, times(1)).findByNameContainsAllIgnoreCase(name, pageable);
    }

    @Test
    public void getCity_whenCityExist_ReturnsCity() {
        City city = this.allCities.get(0);
        String cityId = city.getCityId();
        when(cityRepo.findByCityId(cityId)).thenReturn(Optional.of(city));

        CityDto returnedCity = cityService.getCity(cityId);

        assertThat(returnedCity).isNotNull();
        verify(cityRepo, times(1)).findByCityId(cityId);
    }

    @Test
    public void getCity_whenCityExist_ReturnsCityWithCityIdEqual() {
        City city = this.allCities.get(0);
        String cityId = city.getCityId();
        when(cityRepo.findByCityId(cityId)).thenReturn(Optional.of(city));

        CityDto returnedCity = cityService.getCity(cityId);

        assertThat(returnedCity.getCityId()).isEqualTo(cityId);
        verify(cityRepo, times(1)).findByCityId(cityId);
    }

    @Test
    public void getCity_whenCityExist_ReturnsCityWithNameEqual() {
        City city = this.allCities.get(0);
        String cityId = city.getCityId();
        when(cityRepo.findByCityId(cityId)).thenReturn(Optional.of(city));

        CityDto returnedCity = cityService.getCity(cityId);

        assertThat(returnedCity.getName()).isEqualTo(city.getName());
        verify(cityRepo, times(1)).findByCityId(cityId);
    }

    @Test
    public void getCity_whenCityExist_ReturnsCityWithAboutEqual() {
        City city = this.allCities.get(0);
        String cityId = city.getCityId();
        when(cityRepo.findByCityId(cityId)).thenReturn(Optional.of(city));

        CityDto returnedCity = cityService.getCity(cityId);

        assertThat(returnedCity.getAbout()).isEqualTo(city.getAbout());
        verify(cityRepo, times(1)).findByCityId(cityId);
    }

    @Test
    public void getCity_whenCityDoesNotExist_ThrowResourceNotFound() {
        String cityId = "random_id";
        when(cityRepo.findByCityId(cityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCity(cityId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
