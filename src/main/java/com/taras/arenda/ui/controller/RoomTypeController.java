package com.taras.arenda.ui.controller;

import com.taras.arenda.Service.RoomTypeService;
import com.taras.arenda.dto.RoomTypeDto;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.ui.model.roomtype.CreateRoomTypeRequestModel;
import com.taras.arenda.ui.model.roomtype.CreateRoomTypeResponseModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    @GetMapping("/hotel/{id}")
    public Iterable<RoomType> getRoomTypesByHotel(@PathVariable Long id) {
        return roomTypeService.findRoomTypesByHotel(id);
    }

    @PostMapping({"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRoomTypeResponseModel createRoomType(@Valid @RequestBody final CreateRoomTypeRequestModel roomType) {

        ModelMapper modelMapper = new ModelMapper();

        RoomTypeDto roomTypeDto = modelMapper.map(roomType, RoomTypeDto.class);

        RoomTypeDto createdRoomType = roomTypeService.createRoomType(roomTypeDto);

        return modelMapper.map(createdRoomType, CreateRoomTypeResponseModel.class);
    }
}
