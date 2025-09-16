package com.hilton.hotelchain.controller;

import com.hilton.hotelchain.dto.HotelDTO;
import com.hilton.hotelchain.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO) {
        return ResponseEntity.ok(hotelService.addHotel(hotelDTO));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(hotelService.getHotel(hotelId));
    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }
}