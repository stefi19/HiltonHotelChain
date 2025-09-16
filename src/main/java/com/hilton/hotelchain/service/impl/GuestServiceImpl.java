package com.hilton.hotelchain.service.impl;

import com.hilton.hotelchain.dto.GuestDTO;
import com.hilton.hotelchain.model.Guest;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.repository.GuestRepository;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.service.GuestService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {
    
    private final GuestRepository guestRepository;
    private final HotelRepository hotelRepository;

    @Override
    public GuestDTO addGuest(GuestDTO guestDTO) {
        if (guestRepository.existsByEmail(guestDTO.getEmail())) {
            throw new IllegalStateException("Guest with email " + guestDTO.getEmail() + " already exists");
        }

        Hotel hotel = hotelRepository.findById(guestDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + guestDTO.getHotelId()));

        Guest guest = new Guest();
        guest.setName(guestDTO.getName());
        guest.setEmail(guestDTO.getEmail());
        guest.setPhone(guestDTO.getPhone());
        guest.setHotel(hotel);

        Guest savedGuest = guestRepository.save(guest);
        return convertToDTO(savedGuest);
    }

    @Override
    public GuestDTO getGuest(Integer guestId) {
        return guestRepository.findById(guestId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found with id: " + guestId));
    }

    @Override
    public List<GuestDTO> getGuestsByHotel(Integer hotelId) {
        return guestRepository.findByHotelHotelId(hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private GuestDTO convertToDTO(Guest guest) {
        GuestDTO dto = new GuestDTO();
        dto.setGuestId(guest.getGuestId());
        dto.setName(guest.getName());
        dto.setEmail(guest.getEmail());
        dto.setPhone(guest.getPhone());
        dto.setHotelId(guest.getHotel().getHotelId());
        return dto;
    }
}