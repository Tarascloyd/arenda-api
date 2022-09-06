package com.taras.arenda.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CityDto {

    private Long id;
    private String cityId;
    private String name;
    private String about;
    private int price;
    private LocalDateTime createDate;
}
