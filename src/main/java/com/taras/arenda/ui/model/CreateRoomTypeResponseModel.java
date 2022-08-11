package com.taras.arenda.ui.model;

import com.taras.arenda.jpa.entity.Hotel;
import lombok.Data;


@Data
public class CreateRoomTypeResponseModel {
    private Long id;
    private Hotel hotel;
    private String name;
}
