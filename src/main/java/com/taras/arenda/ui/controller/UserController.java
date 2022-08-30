package com.taras.arenda.ui.controller;

import com.taras.arenda.Service.UserService;
import com.taras.arenda.dto.UserDto;
import com.taras.arenda.ui.model.CreateUserRequestModel;
import com.taras.arenda.ui.model.CreateUserResponseModel;
import com.taras.arenda.ui.model.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public CreateUserResponseModel register(@Valid @RequestBody final CreateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        return modelMapper.map(createdUser, CreateUserResponseModel.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<UserResponseModel> getUsers(@PageableDefault(size = 10) Pageable page) {
        ModelMapper modelMapper = new ModelMapper();
        return userService.getUsers(page)
                .map(userDto -> modelMapper.map(userDto, UserResponseModel.class));
    }
}
