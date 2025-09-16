package com.hilton.hotelchain.service.impl;

import com.hilton.hotelchain.dto.HotelDTO;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.service.HotelService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    
    private final HotelRepository hotelRepository;

    @Override
    public HotelDTO addHotel(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        
        Hotel savedHotel = hotelRepository.save(hotel);
        return convertToDTO(savedHotel);
    }

    @Override
    public HotelDTO getHotel(Integer hotelId) {
        return hotelRepository.findById(hotelId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + hotelId));
    }

    @Override
    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO dto = new HotelDTO();
        dto.setHotelId(hotel.getHotelId());
        dto.setName(hotel.getName());
        dto.setLocation(hotel.getLocation());
        return dto;
    }
}