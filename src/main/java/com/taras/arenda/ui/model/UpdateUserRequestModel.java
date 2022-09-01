package com.taras.arenda.ui.model;

import com.taras.arenda.ui.validation.UniqueEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUserRequestModel {
    @NotNull(message="First name cannot be null")
    @Size(min=2, max=255, message= "First name must not be less than two characters")
    private String firstName;

    @NotNull(message="Last name cannot be null")
    @Size(min=2, max=255, message= "Last name must not be less than two characters")
    private String lastName;
}
