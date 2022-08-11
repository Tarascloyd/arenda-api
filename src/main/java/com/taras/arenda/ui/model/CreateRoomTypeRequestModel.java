package com.taras.arenda.ui.model;


import com.taras.arenda.jpa.entity.Hotel;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class CreateRoomTypeRequestModel {

    @NotNull(message="Hotel cannot be null")
    private Long hotelId;

    @NotNull(message="Name cannot be null")
    @Size(min=2, message= "Name must not be less than two characters")
    private String name;

    @Min(value = 1, message= "Number of People must not be less than 1")
    @Max(value = 8, message= "Number of People must not be more than 8")
    private int numberPeople;

    @Min(value = 1, message= "Number of Beds must not be less than 1")
    @Max(value = 8, message= "Number of Beds must not be more than 8")
    private int numberBeds;

    @Min(value = 1, message= "Number of Rooms must not be less than 1")
    @Max(value = 5, message= "Number of Rooms must not be more than 5")
    private int numberRooms;
}
