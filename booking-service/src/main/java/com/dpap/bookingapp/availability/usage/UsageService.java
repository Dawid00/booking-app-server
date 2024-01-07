package com.dpap.bookingapp.availability.usage;

import com.dpap.bookingapp.availability.timeslot.TimeSlot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsageService {

    private final UsageRepository usageRepository;

    public UsageService(UsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    public boolean isObjectAvailable(Long objectId, TimeSlot timeSlot) {
        return usageRepository.
                findAllByObjectIdBetweenDates(objectId, timeSlot.getStart(), timeSlot.getEnd()).isEmpty();
    }

    public void reserveObject(Long objectId, TimeSlot timeSlot, LocalDateTime at) {
        usageRepository.save(
                new Usage(objectId, timeSlot.getStart(), timeSlot.getEnd(), at)
        );
    }

    @Transactional
    public void delete(Long objectId, TimeSlot timeSlot) {
        List<Usage> availabilities = usageRepository
                .findAllByObjectIdBetweenDates(objectId, timeSlot.getStart(), timeSlot.getEnd());
        usageRepository.deleteAll(availabilities);
    }
}
