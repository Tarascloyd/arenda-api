package com.taras.arenda.ui.model.city;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateCityResponseModel {

    private String cityId;
    private String name;
    private String about;
}
