package com.hilton.hotelchain.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationDTO {
    private Integer reservationId;
    private Integer guestId;
    private Integer hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate reservationDate;
    private String status;
}