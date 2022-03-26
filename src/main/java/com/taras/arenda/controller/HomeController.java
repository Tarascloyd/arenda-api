package com.taras.arenda.controller;

import com.taras.arenda.jpa.domain.City;
import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.Room;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class HomeController {

    private CityRepository cityRepo;
    private HotelRepository hotelRepo;
    private RoomRepository roomRepo;

    public HomeController(CityRepository cityRepo, HotelRepository hotelRepo, RoomRepository roomRepo) {
        this.cityRepo = cityRepo;
        this.hotelRepo = hotelRepo;
        this.roomRepo = roomRepo;
    }

    @GetMapping("/")
    public String home() {
        bootstrap();
        return "redirect:/city/list";
    }

    private void bootstrap() {
        List<City> cities = cityRepo.findAll();
        if (cities.size() == 0) {
            City city1 = City.builder()
                    .name("Новосибирск").about("Столица сибири").build();
            City city2 = City.builder()
                    .name("Краснодар").about("Жаркий город").build();
            City city3 = City.builder()
                    .name("Москва").about("Столица России").build();
            City city4 = City.builder()
                    .name("Горноалтайск").about("Алтай!").build();
            cities.add(city1);
            cities.add(city2);
            cities.add(city3);
            cities.add(city4);
            cityRepo.saveAll(cities);

            Hotel hotel1 = Hotel.builder()
                    .name("Аврора").about("Лучший отель в центре")
                    .city(city1).build();
            Hotel hotel2 = Hotel.builder()
                    .name("Глок").about("Маленький гостевой дом")
                    .city(city1).build();
            Hotel hotel3 = Hotel.builder()
                    .name("Глушь").about("Старая разбитая гостиница")
                    .city(city1).build();
            Hotel hotel4 = Hotel.builder()
                    .name("Блума").about("Лучший отель в центре")
                    .city(city2).build();
            Hotel hotel5 = Hotel.builder()
                    .name("Плесень").about("Маленький гостевой дом")
                    .city(city3).build();
            Hotel hotel6 = Hotel.builder()
                    .name("Ветер").about("Старая разбитая гостиница")
                    .city(city4).build();
            List<Hotel> hotels = new ArrayList<>();
            hotels.add(hotel1);
            hotels.add(hotel2);
            hotels.add(hotel3);
            hotels.add(hotel4);
            hotels.add(hotel5);
            hotels.add(hotel6);
            hotelRepo.saveAll(hotels);

            Random rnd = new Random();
            List<Room> rooms = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                int numberBeds = rnd.nextInt(3) + 1;
                Room room = Room.builder()
                        .name(String.valueOf(i + 1)).numberPeople(numberBeds * 2)
                        .numberBeds(numberBeds).numberRooms(rnd.nextInt(numberBeds / 2 + 1) + 1)
                        .hotel(hotels.get(rnd.nextInt(6)))
                        .price((rnd.nextInt(501) + 800) * numberBeds).build();
                rooms.add(room);
            }
            roomRepo.saveAll(rooms);

            for (Hotel hotel : hotels) {
                List<Room> hRooms = roomRepo.findByHotel(hotel);
                if (hRooms.size() != 0) {
                    int minPrice = hRooms.stream().map(Room :: getPrice).sorted().findFirst().orElse(0);
                    hotel.setPrice(minPrice);
                }
            }
            hotelRepo.saveAll(hotels);
        }

    }
}
