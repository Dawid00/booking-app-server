package com.dpap.bookingapp.reservation.events;

import com.dpap.bookingapp.reservation.Reservation;

interface ReservationEvent {
    Reservation getReservation();
}
