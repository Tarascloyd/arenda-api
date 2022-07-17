package com.taras.arenda.ui.controller;

import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@RequestMapping("api/v1/roomtype")
public class RoomTypeController {

    private final RoomTypeRepository roomTypeRepo;
    private HotelRepository hotelRepo;

    public RoomTypeController(RoomTypeRepository roomRepo, HotelRepository hotelRepo) {
        this.roomTypeRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/{id}")
    public RoomType getRoomType(@PathVariable Long id) {

        return roomTypeRepo.getById(id);
    }

    @GetMapping("/top5")
    public Iterable<RoomType> getTop5RoomType() {

        return roomTypeRepo.findTop5ByOrderByPriceAsc();
    }

    @PostMapping({"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public RoomType createRoomType(@RequestBody final RoomType roomType) {

        return roomTypeRepo.save(roomType);
    }
}
