package com.taras.arenda.ui.model.city;


import com.taras.arenda.ui.validation.UniqueCityName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class UpdateCityRequestModel {

    @Size(min=16, max=5000, message= "About message must not be less than 16 characters and more than 5000")
    private String about;
}
