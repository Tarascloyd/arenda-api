package com.taras.arenda.Service;

import com.taras.arenda.dto.RoomTypeDto;
import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.Hotel;
import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepo;
    private final HotelService hotelService;


    public RoomType getById(Long id) {

        return roomTypeRepo.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Iterable<RoomType> findTop5ByLowerPrice() {

        return roomTypeRepo.findTop5ByOrderByPriceAsc();
    }

    public RoomTypeDto createRoomType(RoomTypeDto roomType) {
        Hotel hotel = hotelService.findById(roomType.getHotelId());
        roomType.setHotel(hotel);

        ModelMapper modelMapper = new ModelMapper();

        RoomType roomTypeEntity = modelMapper.map(roomType, RoomType.class);

        RoomType savedEntity = roomTypeRepo.save(roomTypeEntity);

        return modelMapper.map(savedEntity, RoomTypeDto.class);
    }
}
