package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.RoomDTO;
import java.util.List;

public interface RoomService {
    RoomDTO addRoom(RoomDTO roomDTO);
    RoomDTO getRoom(Integer roomNumber);
    List<RoomDTO> getRoomsByHotel(Integer hotelId);
    List<RoomDTO> getAvailableRoomsByHotel(Integer hotelId);
}