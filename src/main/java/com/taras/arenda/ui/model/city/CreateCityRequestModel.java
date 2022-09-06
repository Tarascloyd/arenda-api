package com.taras.arenda.ui.model.city;


import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class CreateCityRequestModel {

    @NotNull(message="Name cannot be null")
    @Size(min=2, message= "Name must not be less than two characters")
    private String name;

    @Size(min=16, message= "About message must not be less than 16 characters")
    private String about;
}
