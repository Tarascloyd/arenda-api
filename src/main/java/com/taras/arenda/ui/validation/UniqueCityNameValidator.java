package com.taras.arenda.ui.validation;

import com.taras.arenda.jpa.entity.City;
import com.taras.arenda.jpa.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueCityNameValidator implements ConstraintValidator<UniqueCityName, String> {

    @Autowired
    CityRepository cityRepo;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<City> inDb = cityRepo.findByName(value);
        return inDb.isEmpty();
    }
}
