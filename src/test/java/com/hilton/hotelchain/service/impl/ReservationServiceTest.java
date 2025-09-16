package com.hilton.hotelchain.service.impl;

import com.hilton.hotelchain.dto.ReservationDTO;
import com.hilton.hotelchain.model.Guest;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.model.Reservation;
import com.hilton.hotelchain.repository.GuestRepository;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Hotel hotel;
    private Guest guest;
    private Reservation reservation;
    private ReservationDTO reservationDTO;

    @BeforeEach
    void setUp() {
        // Set up hotel
        hotel = new Hotel();
        hotel.setHotelId(1);
        hotel.setName("Test Hotel");

        // Set up guest
        guest = new Guest();
        guest.setGuestId(1);
        guest.setName("John Doe");
        guest.setEmail("john.doe@example.com");
        guest.setPhone("+1234567890");
        guest.setHotel(hotel);

        // Set up reservation
        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setHotel(hotel);
        reservation.setGuest(guest);
        reservation.setCheckInDate(LocalDate.now().plusDays(1));
        reservation.setCheckOutDate(LocalDate.now().plusDays(3));
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("CONFIRMED");

        // Set up reservation DTO
        reservationDTO = new ReservationDTO();
        reservationDTO.setHotelId(1);
        reservationDTO.setGuestId(1);
        reservationDTO.setCheckInDate(LocalDate.now().plusDays(1));
        reservationDTO.setCheckOutDate(LocalDate.now().plusDays(3));
    }

    @Test
    void makeReservation_Success() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(guestRepository.findById(1)).thenReturn(Optional.of(guest));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        ReservationDTO result = reservationService.makeReservation(reservationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getReservationId());
        assertEquals(1, result.getHotelId());
        assertEquals(1, result.getGuestId());
        assertEquals("CONFIRMED", result.getStatus());
        assertEquals(reservation.getCheckInDate(), result.getCheckInDate());
        assertEquals(reservation.getCheckOutDate(), result.getCheckOutDate());
    }

    @Test
    void makeReservation_HotelNotFound() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            reservationService.makeReservation(reservationDTO)
        );
    }

    @Test
    void makeReservation_GuestNotFound() {
        // Arrange
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(guestRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            reservationService.makeReservation(reservationDTO)
        );
    }

    @Test
    void cancelReservation_Success() {
        // Arrange
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
        Reservation cancelledReservation = new Reservation();
        cancelledReservation.setReservationId(1);
        cancelledReservation.setHotel(hotel);
        cancelledReservation.setGuest(guest);
        cancelledReservation.setCheckInDate(reservation.getCheckInDate());
        cancelledReservation.setCheckOutDate(reservation.getCheckOutDate());
        cancelledReservation.setReservationDate(reservation.getReservationDate());
        cancelledReservation.setStatus("CANCELLED");
        when(reservationRepository.save(any(Reservation.class))).thenReturn(cancelledReservation);

        // Act
        ReservationDTO result = reservationService.cancelReservation(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getReservationId());
        assertEquals("CANCELLED", result.getStatus());
    }

    @Test
    void cancelReservation_NotFound() {
        // Arrange
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            reservationService.cancelReservation(1)
        );
    }

    @Test
    void getReservation_Success() {
        // Arrange
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));

        // Act
        ReservationDTO result = reservationService.getReservation(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getReservationId());
        assertEquals(1, result.getHotelId());
        assertEquals(1, result.getGuestId());
        assertEquals("CONFIRMED", result.getStatus());
    }

    @Test
    void getReservation_NotFound() {
        // Arrange
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> 
            reservationService.getReservation(1)
        );
    }

    @Test
    void getReservationsByHotel_Success() {
        // Arrange
        Reservation reservation2 = new Reservation();
        reservation2.setReservationId(2);
        reservation2.setHotel(hotel);
        reservation2.setGuest(guest);
        reservation2.setCheckInDate(LocalDate.now().plusDays(4));
        reservation2.setCheckOutDate(LocalDate.now().plusDays(6));
        reservation2.setReservationDate(LocalDate.now());
        reservation2.setStatus("CONFIRMED");

        when(reservationRepository.findByHotelHotelId(1)).thenReturn(Arrays.asList(reservation, reservation2));

        // Act
        List<ReservationDTO> results = reservationService.getReservationsByHotel(1);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(r -> r.getHotelId().equals(1)));
    }

    @Test
    void getReservationsByHotel_EmptyList() {
        // Arrange
        when(reservationRepository.findByHotelHotelId(1)).thenReturn(Arrays.asList());

        // Act
        List<ReservationDTO> results = reservationService.getReservationsByHotel(1);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getReservationsByGuest_Success() {
        // Arrange
        Reservation reservation2 = new Reservation();
        reservation2.setReservationId(2);
        reservation2.setHotel(hotel);
        reservation2.setGuest(guest);
        reservation2.setCheckInDate(LocalDate.now().plusDays(4));
        reservation2.setCheckOutDate(LocalDate.now().plusDays(6));
        reservation2.setReservationDate(LocalDate.now());
        reservation2.setStatus("CONFIRMED");

        when(reservationRepository.findByGuestGuestId(1)).thenReturn(Arrays.asList(reservation, reservation2));

        // Act
        List<ReservationDTO> results = reservationService.getReservationsByGuest(1);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(r -> r.getGuestId().equals(1)));
    }

    @Test
    void getReservationsByGuest_EmptyList() {
        // Arrange
        when(reservationRepository.findByGuestGuestId(1)).thenReturn(Arrays.asList());

        // Act
        List<ReservationDTO> results = reservationService.getReservationsByGuest(1);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
}