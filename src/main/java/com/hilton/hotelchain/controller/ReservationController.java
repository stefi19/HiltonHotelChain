package com.hilton.hotelchain.controller;

import com.hilton.hotelchain.dto.ReservationDTO;
import com.hilton.hotelchain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> makeReservation(@RequestBody ReservationDTO reservationDTO) {
        return ResponseEntity.ok(reservationService.makeReservation(reservationDTO));
    }

    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.getReservation(reservationId));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(reservationService.getReservationsByHotel(hotelId));
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByGuest(@PathVariable Integer guestId) {
        return ResponseEntity.ok(reservationService.getReservationsByGuest(guestId));
    }
}