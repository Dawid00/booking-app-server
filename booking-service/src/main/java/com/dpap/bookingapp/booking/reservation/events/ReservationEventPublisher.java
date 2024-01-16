package com.dpap.bookingapp.booking.reservation.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(ReservationEvent reservationEvent) {
        applicationEventPublisher.publishEvent(reservationEvent);
    }
}