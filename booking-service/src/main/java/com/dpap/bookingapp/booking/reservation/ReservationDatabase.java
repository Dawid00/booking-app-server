package com.dpap.bookingapp.booking.reservation;

import io.vavr.control.Option;

import java.util.List;

public interface ReservationDatabase {
    List<Reservation> findAll();

    Option<Reservation> findById(Long id);

    void save(Reservation reservation);

    void updateState(Long reservationId, ReservationState state);
}
