package com.taras.arenda.ui.controller;

import com.taras.arenda.Service.RoomTypeService;
import com.taras.arenda.jpa.entity.RoomType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@RequestMapping("api/v1/roomtype")
@AllArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/{id}")
    public RoomType getRoomType(@PathVariable Long id) {

        return roomTypeService.getById(id);
    }

    @GetMapping("/top5")
    public Iterable<RoomType> getTop5RoomType() {

        return roomTypeService.findTop5ByLowerPrice();
    }

    @PostMapping({"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public RoomType createRoomType(@RequestBody final RoomType roomType) {

        return roomTypeService.createRoomType(roomType);
    }
}
