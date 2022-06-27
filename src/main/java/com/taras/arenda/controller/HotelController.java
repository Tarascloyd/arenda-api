package com.taras.arenda.controller;

import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.RoomType;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/hotel")
public class HotelController {

    private RoomTypeRepository roomTypeRepo;
    private HotelRepository hotelRepo;

    public HotelController(RoomTypeRepository roomRepo, HotelRepository hotelRepo) {
        this.roomTypeRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/{id}/roomtype")
    public @ResponseBody Iterable<RoomType> getRoomTypesByHotel(@PathVariable Long id) {
        Hotel hotel = hotelRepo.getById(id);
        return roomTypeRepo.findByHotel(hotel);
    }
}
