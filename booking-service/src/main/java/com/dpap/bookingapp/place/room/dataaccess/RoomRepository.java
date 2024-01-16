package com.dpap.bookingapp.place.room.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("SELECT r from RoomEntity r WHERE r.id = :id AND r.place.id = :placeId")
    Optional<RoomEntity> findByIdAndPlaceId(Long id, Long placeId);
    @Modifying
    @Query(value = "DELETE FROM rooms room where room.place_id = :placeId", nativeQuery = true)
    void deleteByPlaceId(Long placeId);
}
