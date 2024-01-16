package com.dpap.bookingapp.place;

import com.dpap.bookingapp.place.dataaccess.Address;
import com.dpap.bookingapp.place.room.dto.AddRoomRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddPlaceRequest(
        @NotBlank(message = "Place name is mandatory")
        String name,
        @NotBlank(message = "Description is mandatory")
        String description,
        Address address,
        @NotNull
        PlaceCategory category,
        List<AddRoomRequest> rooms
) {
}
