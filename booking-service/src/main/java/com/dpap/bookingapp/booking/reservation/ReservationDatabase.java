package com.dpap.bookingapp.booking.reservation;

import io.vavr.control.Option;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationDatabase {
    List<Reservation> findAll();

    Option<Reservation> findById(Long id);

    void save(Reservation reservation);

    void update(Long reservationId, BigDecimal value, ReservationState state);
}
