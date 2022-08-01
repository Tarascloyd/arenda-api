package com.taras.arenda.Service;

import com.taras.arenda.exceptions.ResourceNotFoundException;
import com.taras.arenda.jpa.entity.RoomType;
import com.taras.arenda.jpa.repository.RoomTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepo;


    public RoomType getById(Long id) {

        return roomTypeRepo.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Iterable<RoomType> findTop5ByLowerPrice() {

        return roomTypeRepo.findTop5ByOrderByPriceAsc();
    }

    public RoomType createRoomType(RoomType roomType) {

        return roomTypeRepo.save(roomType);
    }
}
