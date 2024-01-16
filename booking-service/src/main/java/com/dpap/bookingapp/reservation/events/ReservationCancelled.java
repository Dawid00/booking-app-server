package com.dpap.bookingapp.reservation.events;

import com.dpap.bookingapp.reservation.Reservation;
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