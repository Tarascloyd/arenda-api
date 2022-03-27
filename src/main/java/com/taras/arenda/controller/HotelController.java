package com.taras.arenda.controller;

import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.RoomType;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hotel")
@SessionAttributes("hotel")
public class HotelController {

    private RoomTypeRepository roomTypeRepo;
    private HotelRepository hotelRepo;

    public HotelController(RoomTypeRepository roomRepo, HotelRepository hotelRepo) {
        this.roomTypeRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/{id}")
    public String getRoomTypesByHotel(@PathVariable Long id, Model theModel) {
        Hotel hotel = hotelRepo.getById(id);
        List<RoomType> roomTypes = roomTypeRepo.findByHotel(hotel);
        theModel.addAttribute("roomTypes", roomTypes);
        theModel.addAttribute("hotel", hotel);
        return "hotel/roomTypes";
    }
}
