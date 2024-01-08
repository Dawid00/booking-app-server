package com.dpap.bookingapp.availability.usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageRepository extends JpaRepository<Usage, Long> {

    @Query(
            value =
                    "SELECT * FROM usages usage  WHERE usage.object_id = :objectId" +
                            " AND usage.start <= :finish " +
                            "AND usage.finish >= :start",
            nativeQuery = true
    )
    List<Usage> findAllByObjectIdBetweenDates(Long objectId, LocalDateTime start, LocalDateTime finish);
    List<Usage> findAllByObjectId(Long objectId);
    @Modifying
    @Query(
            value = "DELETE  FROM usages usage WHERE usage.object_id = :objectId",
            nativeQuery = true
    )
    void deleteByObjectId(Long objectId);
}
