package com.dpap.bookingapp.place.room.dto;

import com.dpap.bookingapp.place.room.RoomState;

import java.util.Optional;

public record RoomFilter(
        Integer capacity,
        Long minPricePerNight,
        Long maxPricePerNight,
        RoomState state,
        Long placeId
){
    public boolean isEmpty() {
        return capacity == null && minPricePerNight == null && maxPricePerNight == null && state == null;
    }
    public Optional<Long> getMaxPricePerNight() {
        return Optional.ofNullable(maxPricePerNight);
    }
    public Optional<Long> getMinPricePerNight() {
        return Optional.ofNullable(minPricePerNight);
    }
    public Optional<Integer> getCapacity() {
        return Optional.ofNullable(capacity);
    }
    public Optional<RoomState> getState() {
        return Optional.ofNullable(state);
    }
    public Optional<Long> getPlaceId() {
        return Optional.ofNullable(placeId);
    }
}
