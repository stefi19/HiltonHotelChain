package com.hilton.hotelchain.repository;

import com.hilton.hotelchain.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {
    List<Guest> findByHotelHotelId(Integer hotelId);
    boolean existsByEmail(String email);
}