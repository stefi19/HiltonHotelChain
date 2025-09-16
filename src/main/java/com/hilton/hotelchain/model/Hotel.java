package com.hilton.hotelchain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false, columnDefinition = "TEXT")
    private String location;
}