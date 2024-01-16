package com.dpap.bookingapp.reservation.dto;

import com.dpap.bookingapp.place.room.dto.RoomId;
import com.dpap.bookingapp.reservation.ReservationState;

import java.math.BigDecimal;

public record ReservationDto(
        Long id,
        RoomId roomId,
        Long placeId,
        String checkIn,
        String checkOut,
        String at,
        Long userId,
        ReservationState state,
        BigDecimal value,
        int freeCancellationDays) {
}
