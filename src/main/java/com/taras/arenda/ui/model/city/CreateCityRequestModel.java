package com.taras.arenda.ui.model.city;


import com.taras.arenda.ui.validation.UniqueCityName;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class CreateCityRequestModel {

    @UniqueCityName
    @NotNull(message="Name cannot be null")
    @Size(min=2, max=255, message= "Name must not be less than two characters and more than 255")
    private String name;

    @Size(min=16, max=5000, message= "About message must not be less than 16 characters and more than 5000")
    private String about;
}
