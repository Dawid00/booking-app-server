package com.dpap.bookingapp.place.room.dto;

import com.dpap.bookingapp.common.FacilityType;
import com.dpap.bookingapp.place.room.RoomState;

import java.util.List;

public record RoomDTO(
        Long id,
        Integer capacity,
        RoomState state,
        Long placeId,
        Long pricePerNight,
        String description,
        String name,
        List<FacilityType> facilities
) {
}
