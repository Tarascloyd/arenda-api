package com.taras.arenda.controller;

import com.taras.arenda.jpa.domain.City;
import com.taras.arenda.jpa.domain.Hotel;
import com.taras.arenda.jpa.repository.CityRepository;
import com.taras.arenda.jpa.repository.HotelRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/city")
public class CityController {

    private CityRepository cityRepo;
    private HotelRepository hotelRepo;

    public CityController(CityRepository cityRepo, HotelRepository hotelRepo) {
        this.cityRepo = cityRepo;
        this.hotelRepo = hotelRepo;
    }

    @GetMapping("/list")
    public String listDishes(Model theModel) {
        List<City> cities = cityRepo.findAll();
        theModel.addAttribute("cities", cities);
        return "city/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam("name") String theName, Model theModel) {

        if (theName.trim().isEmpty()) {
            return "redirect:/city/list";
        }
        else {
            List<City> cities = cityRepo.findByNameContainsAllIgnoreCase(theName);
            theModel.addAttribute("cities", cities);
            return "city/list";
        }

    }

    @GetMapping("/{id}/hotel")
    public String getHotelByCity(@PathVariable Long id, Model theModel) {
        City city = cityRepo.getById(id);
        List<Hotel> hotels = hotelRepo.findByCity(city);
        theModel.addAttribute("hotels", hotels);
        theModel.addAttribute("city", city);
        return "city/hotels";
    }
}
