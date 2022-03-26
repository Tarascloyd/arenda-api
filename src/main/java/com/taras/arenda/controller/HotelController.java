package com.taras.arenda.controller;

import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.Room;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    private RoomRepository roomRepo;
    private HotelRepository hotelRepo;

    public HotelController(RoomRepository roomRepo, HotelRepository hotelRepo) {
        this.roomRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/{id}")
    public String getRoomsByHotel(@PathVariable Long id, Model theModel) {
        Hotel hotel = hotelRepo.getById(id);
        List<Room> rooms = roomRepo.findByHotel(hotel);
        theModel.addAttribute("rooms", rooms);
        theModel.addAttribute("hotel", hotel);
        return "hotel/rooms";
    }
}
