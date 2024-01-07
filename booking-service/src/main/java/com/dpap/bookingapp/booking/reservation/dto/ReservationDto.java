package com.dpap.bookingapp.booking.reservation.dto;

import com.dpap.bookingapp.booking.place.room.dto.RoomId;
import com.dpap.bookingapp.booking.reservation.ReservationState;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
