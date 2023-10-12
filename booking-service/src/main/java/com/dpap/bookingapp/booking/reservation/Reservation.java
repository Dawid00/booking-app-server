package com.dpap.bookingapp.booking.reservation;


import com.dpap.bookingapp.booking.place.room.dto.RoomId;
import com.dpap.bookingapp.common.TimeSlot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Reservation {
    private Long id;
    private RoomId roomId;
    private Long placeId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
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
        long hours = ChronoUnit.HOURS.between(reservationPeriod.getStart(), reservationPeriod.getEnd());
        return new Reservation(
                RoomId.fromLong(roomId),placeId,
                reservationPeriod.getStart(),
                reservationPeriod.getEnd(),
                at,
                userId,
                ReservationState.WAITING,
                calculateReservationValue(pricePerNight, hours),
                freeCancellationDays);
    }

    private static BigDecimal calculateReservationValue(BigDecimal pricePerNight, Long hours) {
        var nights = hours / 22;
        return pricePerNight.multiply(BigDecimal.valueOf(nights));
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
        this.state = ReservationState.CANCELED;
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

    private boolean isIn24H(LocalDateTime dateTime) {
        return at.plusDays(1).isAfter(dateTime);
    }

    private boolean isInFreeCancellationTimeSlot(LocalDateTime dateTime) {
        return checkIn.isBefore(dateTime.plusDays(freeCancellationDays)) || isIn24H(dateTime);
    }

    private BigDecimal calculateCost(BigDecimal currentValue, LocalDateTime when) {
        if (isInFreeCancellationTimeSlot(when)) {
            return BigDecimal.ZERO;
        }
        return currentValue;
    }
}
