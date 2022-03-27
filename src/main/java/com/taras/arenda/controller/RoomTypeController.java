package com.taras.arenda.controller;

import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.domain.RoomType;
import com.taras.arenda.jpa.repository.HotelRepository;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
@RequestMapping("/roomtype")
@SessionAttributes("hotel")
public class RoomTypeController {

    private RoomTypeRepository roomTypeRepo;
    private HotelRepository hotelRepo;

    public RoomTypeController(RoomTypeRepository roomRepo, HotelRepository hotelRepo) {
        this.roomTypeRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/{id}")
    public String getRoomType(@PathVariable Long id, Model theModel) {
        RoomType roomType = roomTypeRepo.getById(id);
        theModel.addAttribute("roomType", roomType);
        return "roomType/get";
    }

    @GetMapping("/add")
    public String add(Model model, @SessionAttribute("hotel") Hotel hotel) {
        RoomType roomType = RoomType.builder().hotel(hotel).build();
        roomTypeRepo.save(roomType);
        model.addAttribute("roomType", roomType);
        return "roomType/add";
    }

    @PostMapping
    public String saveRoomType(
            @ModelAttribute("roomType") @Valid RoomType theRoomType,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "roomType/add";
        } else {
            roomTypeRepo.save(theRoomType);
            return "redirect:/hotel/" + theRoomType.getHotel().getId();
        }
    }
}
