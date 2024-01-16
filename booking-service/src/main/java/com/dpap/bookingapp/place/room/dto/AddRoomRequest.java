package com.dpap.bookingapp.place.room.dto;

import com.dpap.bookingapp.common.FacilityType;
import com.dpap.bookingapp.place.room.RoomState;

import java.util.List;

public record AddRoomRequest(
        String name,
        String description,
        Integer capacity,
        RoomState state,
        Long placeId,
        Long pricePerNight,
        List<FacilityType> facilities
){}
