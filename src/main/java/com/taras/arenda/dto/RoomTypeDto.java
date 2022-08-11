package com.taras.arenda.dto;


import com.taras.arenda.jpa.entity.Hotel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RoomTypeDto implements Serializable {
    private Long id;
    private Long hotelId;
    private Hotel hotel;
    private String name;
    private int numberPeople;
    private int numberBeds;
    private int numberRooms;
    private int price;
    private int count;
    private LocalDateTime createDate;
}
