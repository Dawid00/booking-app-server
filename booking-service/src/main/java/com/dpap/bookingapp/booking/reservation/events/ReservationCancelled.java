package com.dpap.bookingapp.booking.reservation.events;

import com.dpap.bookingapp.booking.reservation.Reservation;
import org.springframework.context.ApplicationEvent;

public class ReservationCancelled extends ApplicationEvent implements ReservationEvent {

    private Reservation reservation;

    public ReservationCancelled(Object source, Reservation reservation) {
        super(source);
        this.reservation = reservation;
    }

    public Reservation getReservation() {
        return reservation;
    }

}