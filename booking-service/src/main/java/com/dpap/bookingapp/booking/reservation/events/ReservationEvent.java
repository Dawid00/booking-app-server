package com.dpap.bookingapp.booking.reservation.events;

import com.dpap.bookingapp.booking.reservation.Reservation;

interface ReservationEvent {
    Reservation getReservation();
}
