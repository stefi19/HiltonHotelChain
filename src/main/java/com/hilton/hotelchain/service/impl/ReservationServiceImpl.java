package com.hilton.hotelchain.service.impl;

import com.hilton.hotelchain.dto.ReservationDTO;
import com.hilton.hotelchain.model.Guest;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.model.Reservation;
import com.hilton.hotelchain.repository.GuestRepository;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.repository.ReservationRepository;
import com.hilton.hotelchain.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final GuestRepository guestRepository;

    @Override
    public ReservationDTO makeReservation(ReservationDTO reservationDTO) {
        Hotel hotel = hotelRepository.findById(reservationDTO.getHotelId())
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + reservationDTO.getHotelId()));

        Guest guest = guestRepository.findById(reservationDTO.getGuestId())
                .orElseThrow(() -> new EntityNotFoundException("Guest not found with id: " + reservationDTO.getGuestId()));

        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setHotel(hotel);
        reservation.setCheckInDate(reservationDTO.getCheckInDate());
        reservation.setCheckOutDate(reservationDTO.getCheckOutDate());
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("CONFIRMED");

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }

    @Override
    public ReservationDTO cancelReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + reservationId));
        
        reservation.setStatus("CANCELLED");
        Reservation cancelledReservation = reservationRepository.save(reservation);
        return convertToDTO(cancelledReservation);
    }

    @Override
    public ReservationDTO getReservation(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + reservationId));
    }

    @Override
    public List<ReservationDTO> getReservationsByHotel(Integer hotelId) {
        return reservationRepository.findByHotelHotelId(hotelId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getReservationsByGuest(Integer guestId) {
        return reservationRepository.findByGuestGuestId(guestId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setGuestId(reservation.getGuest().getGuestId());
        dto.setHotelId(reservation.getHotel().getHotelId());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setStatus(reservation.getStatus());
        return dto;
    }
}