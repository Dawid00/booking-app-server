package com.dpap.bookingapp.place.room;

import com.dpap.bookingapp.common.FacilityType;

import java.util.List;

public record UpdateRoomRequest(
        Integer capacity,
        RoomState state,
        Long pricePerNight,
        String description,
        String name,
        List<FacilityType> facilities
) {
}
