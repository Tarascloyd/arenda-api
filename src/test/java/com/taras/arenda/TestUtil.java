package com.taras.arenda;

import com.taras.arenda.dto.UserDto;
import com.taras.arenda.ui.model.CreateUserRequestModel;
import com.taras.arenda.ui.model.LoginRequestModel;
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

    public static LoginRequestModel createLoginRequestModel() {
        LoginRequestModel user = new LoginRequestModel();
        user.setEmail("aa@gmail.com");
        user.setPassword("Password99");
        return user;
    }

    public static UserDto createValidUserDto(String email) {
        CreateUserRequestModel user = createValidUser();
        user.setEmail(email);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDto.class);
    }
}
