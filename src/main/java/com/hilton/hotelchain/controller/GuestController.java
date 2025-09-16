package com.hilton.hotelchain.controller;

import com.hilton.hotelchain.dto.GuestDTO;
import com.hilton.hotelchain.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<GuestDTO> addGuest(@RequestBody GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.addGuest(guestDTO));
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<GuestDTO> getGuest(@PathVariable Integer guestId) {
        return ResponseEntity.ok(guestService.getGuest(guestId));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<GuestDTO>> getGuestsByHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(guestService.getGuestsByHotel(hotelId));
    }
}