package com.dpap.bookingapp.place.room.dto;

import com.dpap.bookingapp.common.FacilityType;

import java.util.List;

public record RoomDetailsDTO(
        Long id,
        Long roomId,
        String name,
        String description,
        String imageUrl,
        List<FacilityType> facilities
) {
}
