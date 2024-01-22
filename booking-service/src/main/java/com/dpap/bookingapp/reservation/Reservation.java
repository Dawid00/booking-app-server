package com.dpap.bookingapp.reservation;


import com.dpap.bookingapp.place.room.dto.RoomId;
import com.dpap.bookingapp.availability.timeslot.TimeSlot;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Reservation {
    private Long id;
    private RoomId roomId;
    private Long placeId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime checkOut;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime at;
    private Long userId;
    private ReservationState state;
    private BigDecimal value;
    private int freeCancellationDays;

    public static Reservation createReservation(
            TimeSlot reservationPeriod,
            Long roomId,
            LocalDateTime at,
            Long userId,
            Long placeId,
            BigDecimal pricePerNight, int freeCancellationDays) {
        return new Reservation(
                RoomId.fromLong(roomId), placeId,
                reservationPeriod.getStart(),
                reservationPeriod.getEnd(),
                at,
                userId,
                ReservationState.WAITING,
                calculateReservationValue(pricePerNight,  reservationPeriod.durationInDays()),
                freeCancellationDays
        );
    }

    private static BigDecimal calculateReservationValue(BigDecimal pricePerNight, Long days) {
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }

    public Long getPlaceId() {
        return placeId;
    }

    public Reservation(Long id, RoomId roomId, Long placeId, LocalDateTime checkIn, LocalDateTime checkOut, LocalDateTime at, Long userId, BigDecimal value, int freeCancellationDays, ReservationState state) {
        this.id = id;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.at = at;
        this.userId = userId;
        this.placeId = placeId;
        this.state = state;
        this.value = value;
        this.freeCancellationDays = freeCancellationDays;
    }

    public Reservation(RoomId roomId, Long placeId, LocalDateTime checkIn, LocalDateTime checkOut, LocalDateTime at, Long userId, ReservationState state, BigDecimal value, int freeCancellationDays) {
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.placeId = placeId;
        this.at = at;
        this.userId = userId;
        this.state = state;
        this.value = value;
        this.freeCancellationDays = freeCancellationDays;
    }


    public void cancel(LocalDateTime when) {
        if (state.equals(ReservationState.CHECK_IN) || state.equals(ReservationState.CHECK_OUT)) {
            throw new RuntimeException("You must not cancel reservation");
        }
        this.value = calculateCost(value, when);
        this.state = ReservationState.CANCELLED;
    }

    public void confirm() {
        this.state = ReservationState.CONFIRMED;
    }

    public void addCost(BigDecimal cost) {
        if (cost.compareTo(BigDecimal.ZERO) > 0)
            this.value = this.value.add(cost);
    }

    public void checkIn() {
        this.state = ReservationState.CHECK_IN;
    }

    public void checkOut() {
        this.state = ReservationState.CHECK_OUT;
    }

    public ReservationState getState() {
        return state;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAt() {
        return at;
    }

    public void setRoomId(RoomId roomId) {
        this.roomId = roomId;
    }

    public int getFreeCancellationDays() {
        return freeCancellationDays;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }

    public RoomId getRoomId() {
        return roomId;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }


    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(roomId, that.roomId) && Objects.equals(checkIn, that.checkIn) && Objects.equals(checkOut, that.checkOut) && Objects.equals(at, that.at) && Objects.equals(userId, that.userId) && state == that.state && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, checkIn, checkOut, at, userId, state, value);
    }

    private boolean isInFreeCancellationTimeSlot(LocalDateTime dateTime) {
        return checkIn.isAfter(dateTime.plusDays(freeCancellationDays));
    }

    private BigDecimal calculateCost(BigDecimal currentValue, LocalDateTime when) {
        if (isInFreeCancellationTimeSlot(when)) {
            return BigDecimal.ZERO;
        }
        return currentValue.multiply(BigDecimal.valueOf(1));
    }
}
