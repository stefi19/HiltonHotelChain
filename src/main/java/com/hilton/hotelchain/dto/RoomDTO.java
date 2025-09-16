package com.hilton.hotelchain.dto;

import com.hilton.hotelchain.model.RoomType;
import lombok.Data;

@Data
public class RoomDTO {
    private Integer roomNumber;
    private RoomType type;
    private Boolean available;
    private Integer hotelId;
}