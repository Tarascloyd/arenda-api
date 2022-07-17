package com.taras.arenda.ui.controller;

import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/hotel")
public class HotelController {

    private final RoomTypeRepository roomTypeRepo;
    private final HotelRepository hotelRepo;

    public HotelController(RoomTypeRepository roomRepo, HotelRepository hotelRepo) {
        this.roomTypeRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping({"/", ""})
    public Iterable<Hotel> getAllHotelsOrSearch(
            @RequestParam("name") String name) {

        if (name.trim().isEmpty()) {
            return hotelRepo.findAll();
        }
        return hotelRepo.findByNameContainsAllIgnoreCase(name);

    }

    @GetMapping("/{id}/roomtype")
    public Iterable<RoomType> getRoomTypesByHotel(@PathVariable Long id) {
        Hotel hotel = hotelRepo.getById(id);
        return roomTypeRepo.findByHotel(hotel);
    }

    @PostMapping({"/", ""})
    public void createHotel() {

    }

    @PatchMapping({"/", ""})
    public void updateHotel() {

    }

    @DeleteMapping({"/", ""})
    public void deleteHotel() {

    }
}
