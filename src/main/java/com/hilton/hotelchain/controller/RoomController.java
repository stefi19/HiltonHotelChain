package com.hilton.hotelchain.controller;

import com.hilton.hotelchain.dto.RoomDTO;
import com.hilton.hotelchain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.addRoom(roomDTO));
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Integer roomNumber) {
        return ResponseEntity.ok(roomService.getRoom(roomNumber));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDTO>> getRoomsByHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRoomsByHotel(@PathVariable Integer hotelId) {
        return ResponseEntity.ok(roomService.getAvailableRoomsByHotel(hotelId));
    }
}