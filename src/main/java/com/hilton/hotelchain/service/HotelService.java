package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.HotelDTO;
import java.util.List;

public interface HotelService {
    HotelDTO addHotel(HotelDTO hotelDTO);
    HotelDTO getHotel(Integer hotelId);
    List<HotelDTO> getAllHotels();
}