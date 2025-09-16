package com.hilton.hotelchain.service.impl;

import com.hilton.hotelchain.dto.RoomDTO;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.model.Room;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.repository.RoomRepository;
import com.hilton.hotelchain.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Override
    public RoomDTO addRoom(RoomDTO roomDTO) {
        Hotel hotel = hotelRepository.findById(roomDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + roomDTO.getHotelId()));

        Room room = new Room();
        room.setType(roomDTO.getType());
        room.setAvailable(true);
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    @Override
    public RoomDTO getRoom(Integer roomNumber) {
        return roomRepository.findById(roomNumber)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with number: " + roomNumber));
    }

    @Override
    public List<RoomDTO> getRoomsByHotel(Integer hotelId) {
        return roomRepository.findByHotelHotelId(hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomDTO> getAvailableRoomsByHotel(Integer hotelId) {
        return roomRepository.findByHotelHotelIdAndAvailable(hotelId, true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber(room.getRoomNumber());
        dto.setType(room.getType());
        dto.setAvailable(room.getAvailable());
        dto.setHotelId(room.getHotel().getHotelId());
        return dto;
    }
}