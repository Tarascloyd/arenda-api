package com.taras.arenda;

import com.taras.arenda.dto.UserDto;
import com.taras.arenda.ui.model.CreateUserRequestModel;
import org.modelmapper.ModelMapper;

public class TestUtil {

    public static UserDto createValidUserDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(createValidUser(), UserDto.class);
    }

    public static CreateUserRequestModel createValidUser() {
        CreateUserRequestModel user = new CreateUserRequestModel();
        user.setFirstName("Mark");
        user.setLastName("Mailer");
        user.setEmail("aa@gmail.com");
        user.setPassword("Password99");
        return user;
    }
}
