package com.hilton.hotelchain.repository;

import com.hilton.hotelchain.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByHotelHotelId(Integer hotelId);
    List<Room> findByHotelHotelIdAndAvailable(Integer hotelId, Boolean available);
}