package com.dpap.bookingapp.availability.usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageRepository extends JpaRepository<Usage, Long> {

    @Query(value = "SELECT * FROM usages a  WHERE a.object_id = :objectId AND a.start <= :finish AND a.finish >= :start", nativeQuery = true)
    List<Usage> findAllByObjectIdBetweenDates(Long objectId, LocalDateTime start, LocalDateTime finish);

    List<Usage> findAllByObjectId(Long objectId);
}
