package com.taras.arenda.ui.validation;

import com.taras.arenda.jpa.entity.User;
import com.taras.arenda.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> inDb = userRepo.findByEmail(value);
        return inDb.isEmpty();
    }
}
