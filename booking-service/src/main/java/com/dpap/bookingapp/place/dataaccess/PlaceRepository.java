package com.dpap.bookingapp.place.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {

    @Query("SELECT p FROM PlaceEntity p WHERE p.address.city LIKE :city ")
    List<PlaceEntity> findAllByCity(String city);

    @Modifying
    @Query("DELETE FROM PlaceEntity p WHERE p.id = :id AND p.user.id = :userId ")
    void deleteAllByIdAndUserId(Long id, Long userId);
}
