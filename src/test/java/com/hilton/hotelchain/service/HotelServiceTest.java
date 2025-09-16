package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.HotelDTO;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.service.impl.HotelServiceImpl;
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

class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addHotel_Success() {
        // Arrange
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setName("Test Hotel");
        hotelDTO.setLocation("Test Location");
        
        Hotel savedHotel = new Hotel();
        savedHotel.setHotelId(1);
        savedHotel.setName(hotelDTO.getName());
        savedHotel.setLocation(hotelDTO.getLocation());
        
        when(hotelRepository.save(any(Hotel.class))).thenReturn(savedHotel);

        // Act
        HotelDTO result = hotelService.addHotel(hotelDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Hotel", result.getName());
        assertEquals("Test Location", result.getLocation());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void getHotel_ExistingHotel_ReturnsHotel() {
        // Arrange
        Integer hotelId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        hotel.setName("Test Hotel");
        hotel.setLocation("Test Location");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // Act
        HotelDTO result = hotelService.getHotel(hotelId);

        // Assert
        assertNotNull(result);
        assertEquals(hotelId, result.getHotelId());
        assertEquals("Test Hotel", result.getName());
        assertEquals("Test Location", result.getLocation());
    }

    @Test
    void getHotel_NonExistingHotel_ThrowsException() {
        // Arrange
        Integer hotelId = 1;
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            hotelService.getHotel(hotelId);
        });
    }

    @Test
    void getAllHotels_ReturnsAllHotels() {
        // Arrange
        Hotel hotel1 = new Hotel();
        hotel1.setHotelId(1);
        hotel1.setName("Hotel 1");
        hotel1.setLocation("Location 1");
        
        Hotel hotel2 = new Hotel();
        hotel2.setHotelId(2);
        hotel2.setName("Hotel 2");
        hotel2.setLocation("Location 2");
        
        when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel1, hotel2));

        // Act
        List<HotelDTO> results = hotelService.getAllHotels();

        // Assert
        assertEquals(2, results.size());
        assertEquals("Hotel 1", results.get(0).getName());
        assertEquals("Location 1", results.get(0).getLocation());
        assertEquals("Hotel 2", results.get(1).getName());
        assertEquals("Location 2", results.get(1).getLocation());
    }
}