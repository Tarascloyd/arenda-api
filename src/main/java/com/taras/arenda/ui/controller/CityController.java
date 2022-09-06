package com.taras.arenda.ui.controller;

import com.taras.arenda.Service.CityService;
import com.taras.arenda.dto.CityDto;
import com.taras.arenda.dto.UserDto;
import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import com.taras.arenda.ui.model.city.CreateCityRequestModel;
import com.taras.arenda.ui.model.city.CreateCityResponseModel;
import com.taras.arenda.ui.model.user.CreateUserRequestModel;
import com.taras.arenda.ui.model.user.CreateUserResponseModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping("api/v1/cities")
@AllArgsConstructor
public class CityController {

    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented yet in version v1";
    private final CityRepository cityRepo;
    private final HotelRepository hotelRepo;
    private final RoomTypeRepository roomTypeRepo;

    private final CityService cityService;

    @GetMapping({"/", ""})
    public Iterable<City> getAllCitiesOrSearch(
            @RequestParam(name = "name", required = false) String name) {
        if (name == null || name.trim().isEmpty()) {
            return cityService.findAll();
        }
        return cityService.findByName(name);
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Long id) {

        return cityService.findById(id);
    }

    @GetMapping("/{id}/hotel")
    public Iterable<Hotel> getHotelsByCity(@PathVariable Long id) {

        return cityService.getHotels(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public CreateCityResponseModel createCity(@Valid @RequestBody final CreateCityRequestModel cityRequest) {
        ModelMapper modelMapper = new ModelMapper();

        CityDto cityDto = modelMapper.map(cityRequest, CityDto.class);

        CityDto createdCity = cityService.createCity(cityDto);

        return modelMapper.map(createdCity, CreateCityResponseModel.class);
    }

    //TODO endpoint
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @PatchMapping({"/", ""})
    public String updateCity() {
        return NOT_IMPLEMENTED_MESSAGE;
    }

    //TODO endpoint
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @DeleteMapping({"/", ""})
    public String deleteCity() {
        return NOT_IMPLEMENTED_MESSAGE;
    }

//    @PostConstruct
//    private void bootstrap() {
//        List<City> cities = cityRepo.findAll();
//        if (cities.size() == 0) {
//            City city1 = City.builder()
//                    .name("Новосибирск").about("Столица сибири").build();
//            City city2 = City.builder()
//                    .name("Краснодар").about("Жаркий город").build();
//            City city3 = City.builder()
//                    .name("Москва").about("Столица России").build();
//            City city4 = City.builder()
//                    .name("Горноалтайск").about("Алтай!").build();
//            cities.add(city1);
//            cities.add(city2);
//            cities.add(city3);
//            cities.add(city4);
//            cityRepo.saveAll(cities);
//
//            Hotel hotel1 = Hotel.builder()
//                    .name("Аврора").about("Лучший отель в центре")
//                    .city(city1).build();
//            Hotel hotel2 = Hotel.builder()
//                    .name("Глок").about("Маленький гостевой дом")
//                    .city(city1).build();
//            Hotel hotel3 = Hotel.builder()
//                    .name("Глушь").about("Старая разбитая гостиница")
//                    .city(city1).build();
//            Hotel hotel4 = Hotel.builder()
//                    .name("Блума").about("Лучший отель в центре")
//                    .city(city2).build();
//            Hotel hotel5 = Hotel.builder()
//                    .name("Плесень").about("Маленький гостевой дом")
//                    .city(city3).build();
//            Hotel hotel6 = Hotel.builder()
//                    .name("Ветер").about("Старая разбитая гостиница")
//                    .city(city4).build();
//            List<Hotel> hotels = new ArrayList<>();
//            hotels.add(hotel1);
//            hotels.add(hotel2);
//            hotels.add(hotel3);
//            hotels.add(hotel4);
//            hotels.add(hotel5);
//            hotels.add(hotel6);
//            hotelRepo.saveAll(hotels);
//
//            Random rnd = new Random();
//            List<RoomType> roomTypes = new ArrayList<>();
//            for (int i = 0; i < 30; i++) {
//                int numberBeds = rnd.nextInt(3) + 1;
//                RoomType roomType = RoomType.builder()
//                        .name(String.valueOf(i + 1)).numberPeople(numberBeds * 2)
//                        .numberBeds(numberBeds).numberRooms(rnd.nextInt(numberBeds / 2 + 1) + 1)
//                        .hotel(hotels.get(rnd.nextInt(6)))
//                        .count(rnd.nextInt(5) + 1)
//                        .price((rnd.nextInt(501) + 800) * numberBeds).build();
//                roomTypes.add(roomType);
//            }
//            roomTypeRepo.saveAll(roomTypes);
//
//            for (Hotel hotel : hotels) {
//                List<RoomType> hRoomTypes = roomTypeRepo.findAllByHotel(hotel);
//                if (hRoomTypes.size() != 0) {
//                    int minPrice = hRoomTypes.stream().map(RoomType::getPrice).sorted().findFirst().orElse(0);
//                    hotel.setPrice(minPrice);
//                }
//            }
//            hotelRepo.saveAll(hotels);
//
//            for (City city : cities) {
//                List<Hotel> hhotels = hotelRepo.findAllByCity(city);
//                if (hhotels.size() != 0) {
//                    int minPrice = hhotels.stream().map(Hotel::getPrice).sorted().findFirst().orElse(0);
//                    city.setPrice(minPrice);
//                }
//            }
//            cityRepo.saveAll(cities);
//        }
//
//    }
}
