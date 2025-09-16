package com.hilton.hotelchain.service;

import com.hilton.hotelchain.dto.GuestDTO;
import com.hilton.hotelchain.model.Guest;
import com.hilton.hotelchain.model.Hotel;
import com.hilton.hotelchain.repository.GuestRepository;
import com.hilton.hotelchain.repository.HotelRepository;
import com.hilton.hotelchain.service.impl.GuestServiceImpl;
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

class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addGuest_Success() {
        // Arrange
        Integer hotelId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        hotel.setName("Test Hotel");

        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setName("John Doe");
        guestDTO.setEmail("john@example.com");
        guestDTO.setPhone("1234567890");
        guestDTO.setHotelId(hotelId);

        Guest savedGuest = new Guest();
        savedGuest.setGuestId(1);
        savedGuest.setName(guestDTO.getName());
        savedGuest.setEmail(guestDTO.getEmail());
        savedGuest.setPhone(guestDTO.getPhone());
        savedGuest.setHotel(hotel);

        when(guestRepository.existsByEmail(guestDTO.getEmail())).thenReturn(false);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(guestRepository.save(any(Guest.class))).thenReturn(savedGuest);

        // Act
        GuestDTO result = guestService.addGuest(guestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedGuest.getGuestId(), result.getGuestId());
        assertEquals(guestDTO.getName(), result.getName());
        assertEquals(guestDTO.getEmail(), result.getEmail());
        assertEquals(guestDTO.getPhone(), result.getPhone());
        assertEquals(hotelId, result.getHotelId());

        verify(guestRepository).existsByEmail(guestDTO.getEmail());
        verify(hotelRepository).findById(hotelId);
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void addGuest_ExistingEmail_ThrowsException() {
        // Arrange
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setEmail("existing@example.com");

        when(guestRepository.existsByEmail(guestDTO.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> guestService.addGuest(guestDTO));
        verify(guestRepository).existsByEmail(guestDTO.getEmail());
        verify(hotelRepository, never()).findById(any());
        verify(guestRepository, never()).save(any());
    }

    @Test
    void addGuest_HotelNotFound_ThrowsException() {
        // Arrange
        Integer hotelId = 1;
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setEmail("new@example.com");
        guestDTO.setHotelId(hotelId);

        when(guestRepository.existsByEmail(guestDTO.getEmail())).thenReturn(false);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> guestService.addGuest(guestDTO));
        verify(guestRepository).existsByEmail(guestDTO.getEmail());
        verify(hotelRepository).findById(hotelId);
        verify(guestRepository, never()).save(any());
    }

    @Test
    void getGuest_ExistingGuest_ReturnsGuest() {
        // Arrange
        Integer guestId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(1);

        Guest guest = new Guest();
        guest.setGuestId(guestId);
        guest.setName("John Doe");
        guest.setEmail("john@example.com");
        guest.setPhone("1234567890");
        guest.setHotel(hotel);

        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));

        // Act
        GuestDTO result = guestService.getGuest(guestId);

        // Assert
        assertNotNull(result);
        assertEquals(guestId, result.getGuestId());
        assertEquals(guest.getName(), result.getName());
        assertEquals(guest.getEmail(), result.getEmail());
        assertEquals(guest.getPhone(), result.getPhone());
        assertEquals(hotel.getHotelId(), result.getHotelId());
    }

    @Test
    void getGuest_NonExistingGuest_ThrowsException() {
        // Arrange
        Integer guestId = 1;
        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> guestService.getGuest(guestId));
        verify(guestRepository).findById(guestId);
    }

    @Test
    void getGuestsByHotel_ReturnsAllGuests() {
        // Arrange
        Integer hotelId = 1;
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);

        Guest guest1 = new Guest();
        guest1.setGuestId(1);
        guest1.setName("John Doe");
        guest1.setEmail("john@example.com");
        guest1.setPhone("1234567890");
        guest1.setHotel(hotel);

        Guest guest2 = new Guest();
        guest2.setGuestId(2);
        guest2.setName("Jane Doe");
        guest2.setEmail("jane@example.com");
        guest2.setPhone("0987654321");
        guest2.setHotel(hotel);

        when(guestRepository.findByHotelHotelId(hotelId)).thenReturn(Arrays.asList(guest1, guest2));

        // Act
        List<GuestDTO> results = guestService.getGuestsByHotel(hotelId);

        // Assert
        assertEquals(2, results.size());
        
        assertEquals(guest1.getGuestId(), results.get(0).getGuestId());
        assertEquals(guest1.getName(), results.get(0).getName());
        assertEquals(guest1.getEmail(), results.get(0).getEmail());
        assertEquals(guest1.getPhone(), results.get(0).getPhone());
        
        assertEquals(guest2.getGuestId(), results.get(1).getGuestId());
        assertEquals(guest2.getName(), results.get(1).getName());
        assertEquals(guest2.getEmail(), results.get(1).getEmail());
        assertEquals(guest2.getPhone(), results.get(1).getPhone());
    }
}