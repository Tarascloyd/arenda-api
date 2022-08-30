package com.taras.arenda.Service;

import com.taras.arenda.dto.UserDto;
import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.User;
import com.taras.arenda.jpa.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;

    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDetails, User.class);

        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(username);
                });

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getEncryptedPassword(), true, true,
                true, true, new ArrayList<>());
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException(email);
                });

        return new ModelMapper().map(user, UserDto.class);
    }


    public Page<?> getUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        return userRepo.findAll(pageable);
    }
}
