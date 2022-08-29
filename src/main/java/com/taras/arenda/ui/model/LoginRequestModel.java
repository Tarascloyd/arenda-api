package com.taras.arenda.ui.model;

import com.taras.arenda.ui.validation.UniqueEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class LoginRequestModel {

    private String email;
    private String password;
}
