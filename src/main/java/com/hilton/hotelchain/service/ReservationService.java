package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.ReservationDTO;
import java.util.List;

public interface ReservationService {
    ReservationDTO makeReservation(ReservationDTO reservationDTO);
    ReservationDTO cancelReservation(Integer reservationId);
    ReservationDTO getReservation(Integer reservationId);
    List<ReservationDTO> getReservationsByHotel(Integer hotelId);
    List<ReservationDTO> getReservationsByGuest(Integer guestId);
}