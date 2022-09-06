package com.taras.arenda.ui.controller;

import com.taras.arenda.Service.UserService;
import com.taras.arenda.dto.UserDto;
import com.taras.arenda.ui.model.user.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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
    public Page<PublicUserResponseModel> getUsers(@PageableDefault(size = 10) Pageable page) {
        ModelMapper modelMapper = new ModelMapper();
        return userService.getUsers(page)
                .map(userDto -> modelMapper.map(userDto, PublicUserResponseModel.class));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public PublicUserResponseModel getUser(@PathVariable String userId) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = userService.getUser(userId);
        return modelMapper.map(userDto, PublicUserResponseModel.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/{email}")
    public UserResponseModel getUserByEmail(@PathVariable String email, Principal pricncipal) {
        userService.isCurrentUser(email, pricncipal.getName());
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = userService.getUserByEmail(email);
        return modelMapper.map(userDto, UserResponseModel.class);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}")
    @PreAuthorize("#userId == principal")
    public UserResponseModel updateUser(@PathVariable String userId,
                           @Valid @RequestBody(required = false) final UpdateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = userService.updateUser(userId, modelMapper.map(userDetails, UserDto.class));
        return modelMapper.map(userDto, UserResponseModel.class);
    }
}
