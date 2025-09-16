package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.GuestDTO;
import java.util.List;

public interface GuestService {
    GuestDTO addGuest(GuestDTO guestDTO);
    GuestDTO getGuest(Integer guestId);
    List<GuestDTO> getGuestsByHotel(Integer hotelId);
}