package com.taras.arenda.Service;

import com.taras.arenda.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {


    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword("ffff");

        return userDetails;
    }

}
