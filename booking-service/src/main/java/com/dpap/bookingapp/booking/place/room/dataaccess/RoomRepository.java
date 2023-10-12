package com.dpap.bookingapp.booking.place.room.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT r from RoomEntity r WHERE r.id = :id AND r.place.id = :placeId")
    Optional<RoomEntity> findByIdAndPlaceId(Long id, Long placeId);
}
