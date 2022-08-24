package com.taras.arenda.ui.model;

import com.taras.arenda.ui.validation.UniqueEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequestModel {
    @NotNull(message="First name cannot be null")
    @Size(min=2, max=255, message= "First name must not be less than two characters")
    private String firstName;

    @NotNull(message="Last name cannot be null")
    @Size(min=2, max=255, message= "Last name must not be less than two characters")
    private String lastName;

    @NotNull(message="Password cannot be null")
    @Size(min=8, max=255, message="Password must be equal or grater than 8 characters and less than 16 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message= "Password must have at least one uppercase, one lowercase letter and one digit")
    private String password;

    @NotNull(message="Email cannot be null")
    @Email
    @UniqueEmail
    private String email;
}
