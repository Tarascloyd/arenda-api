package com.taras.arenda.ui.controller;

import com.taras.arenda.Service.HotelService;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("api/v1/hotel")
@AllArgsConstructor
public class HotelController {

    private static final String NOT_IMPLEMENTED_MESSAGE = "Not implemented yet in version v1";
    private final RoomTypeRepository roomTypeRepo;
    private final HotelService hotelService;

    @GetMapping({"/", ""})
    public Iterable<Hotel> getAllHotelsOrSearch(
            @RequestParam(name = "name", required = false) String name) {
        if (name == null || name.trim().isEmpty()) {
            return hotelService.findAll();
        }
        return hotelService.findByName(name);
    }

    @GetMapping("/{id}/roomtype")
    public Iterable<RoomType> getRoomTypesByHotel(@PathVariable Long id) {
        Hotel hotel = hotelService.findById(id);
        return roomTypeRepo.findAllByHotel(hotel);
    }

    //TODO endpoint
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @PostMapping({"/", ""})
    public String createHotel() {
        return NOT_IMPLEMENTED_MESSAGE;
    }

    //TODO endpoint
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @PatchMapping({"/", ""})
    public String updateHotel() {
        return NOT_IMPLEMENTED_MESSAGE;
    }

    //TODO endpoint
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @DeleteMapping({"/", ""})
    public String deleteHotel() {
        return NOT_IMPLEMENTED_MESSAGE;
    }
}
