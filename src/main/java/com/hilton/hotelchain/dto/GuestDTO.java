package com.hilton.hotelchain.dto;

import lombok.Data;

@Data
public class GuestDTO {
    private Integer guestId;
    private String name;
    private String email;
    private String phone;
    private Integer hotelId;
}