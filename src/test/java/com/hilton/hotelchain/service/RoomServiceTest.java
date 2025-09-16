package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.RoomDTO;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.model.Room;
import com.hilton.hotelchain.model.RoomType;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.repository.RoomRepository;
import com.hilton.hotelchain.service.impl.RoomServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRoom_Success() {
        // Arrange
        Integer hotelId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        hotel.setName("Test Hotel");

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setType(RoomType.SINGLE);
        roomDTO.setHotelId(hotelId);

        Room savedRoom = new Room();
        savedRoom.setRoomNumber(101);
        savedRoom.setType(RoomType.SINGLE);
        savedRoom.setAvailable(true);
        savedRoom.setHotel(hotel);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        // Act
        RoomDTO result = roomService.addRoom(roomDTO);

        // Assert
        assertNotNull(result);
        assertEquals(101, result.getRoomNumber());
        assertEquals(RoomType.SINGLE, result.getType());
        assertTrue(result.getAvailable());
        assertEquals(hotelId, result.getHotelId());
        verify(hotelRepository).findById(hotelId);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void addRoom_HotelNotFound_ThrowsException() {
        // Arrange
        Integer hotelId = 1;
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setType(RoomType.SINGLE);
        roomDTO.setHotelId(hotelId);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> roomService.addRoom(roomDTO));
        verify(hotelRepository).findById(hotelId);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void getRoom_ExistingRoom_ReturnsRoom() {
        // Arrange
        Integer roomNumber = 101;
        Hotel hotel = new Hotel();
        hotel.setHotelId(1);

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setType(RoomType.SINGLE);
        room.setAvailable(true);
        room.setHotel(hotel);

        when(roomRepository.findById(roomNumber)).thenReturn(Optional.of(room));

        // Act
        RoomDTO result = roomService.getRoom(roomNumber);

        // Assert
        assertNotNull(result);
        assertEquals(roomNumber, result.getRoomNumber());
        assertEquals(RoomType.SINGLE, result.getType());
        assertTrue(result.getAvailable());
        assertEquals(hotel.getHotelId(), result.getHotelId());
    }

    @Test
    void getRoom_NonExistingRoom_ThrowsException() {
        // Arrange
        Integer roomNumber = 101;
        when(roomRepository.findById(roomNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> roomService.getRoom(roomNumber));
        verify(roomRepository).findById(roomNumber);
    }

    @Test
    void getRoomsByHotel_ReturnsAllRooms() {
        // Arrange
        Integer hotelId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        Room room1 = new Room();
        room1.setRoomNumber(101);
        room1.setType(RoomType.SINGLE);
        room1.setAvailable(true);
        room1.setHotel(hotel);

        Room room2 = new Room();
        room2.setRoomNumber(102);
        room2.setType(RoomType.DOUBLE);
        room2.setAvailable(false);
        room2.setHotel(hotel);

        when(roomRepository.findByHotelHotelId(hotelId)).thenReturn(Arrays.asList(room1, room2));

        // Act
        List<RoomDTO> results = roomService.getRoomsByHotel(hotelId);

        // Assert
        assertEquals(2, results.size());
        
        assertEquals(101, results.get(0).getRoomNumber());
        assertEquals(RoomType.SINGLE, results.get(0).getType());
        assertTrue(results.get(0).getAvailable());
        
        assertEquals(102, results.get(1).getRoomNumber());
        assertEquals(RoomType.DOUBLE, results.get(1).getType());
        assertFalse(results.get(1).getAvailable());
    }

    @Test
    void getAvailableRoomsByHotel_ReturnsOnlyAvailableRooms() {
        // Arrange
        Integer hotelId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        Room availableRoom = new Room();
        availableRoom.setRoomNumber(101);
        availableRoom.setType(RoomType.SINGLE);
        availableRoom.setAvailable(true);
        availableRoom.setHotel(hotel);

        when(roomRepository.findByHotelHotelIdAndAvailable(hotelId, true))
                .thenReturn(Arrays.asList(availableRoom));

        // Act
        List<RoomDTO> results = roomService.getAvailableRoomsByHotel(hotelId);

        // Assert
        assertEquals(1, results.size());
        assertEquals(101, results.get(0).getRoomNumber());
        assertEquals(RoomType.SINGLE, results.get(0).getType());
        assertTrue(results.get(0).getAvailable());
    }
}