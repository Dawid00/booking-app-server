package com.dpap.bookingapp.booking.reservation.events;


import com.dpap.bookingapp.booking.place.PlaceService;
import com.dpap.bookingapp.notification.NotificationService;
import com.dpap.bookingapp.notification.NotificationTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ReservationEventListener {

    private final NotificationService notificationService;
    private final PlaceService placeService;

    public ReservationEventListener(NotificationService notificationService, PlaceService placeService) {
        this.notificationService = notificationService;
        this.placeService = placeService;
    }

    @TransactionalEventListener
    public void onReservationRequested(ReservationRequested event) {

        var reservation = event.getReservation();
        var user = placeService.findPlaceById(reservation.getPlaceId()).getUser();
        notificationService.sendNotification(
                reservation.getUserId(),
                new NotificationTemplate(
                        createContent(
                                user.getAccountNumber(),
                                user.getName(),
                                reservation.getFreeCancellationDays(),
                                reservation.getValue()
                        ),
                        reservation.getId()),
                "Potwierdź rezerwację"
        );
    }

    @EventListener
    public void onReservationCancelled(ReservationCancelled event) {
        var reservation = event.getReservation();
        StringBuilder content = new StringBuilder("Anulowano rezerwację.\n");
        if (BigDecimal.ZERO.equals(reservation.getValue())) {
            content.append("Otrzymasz zwrot pieniędzy.");
        } else {
            content.append("Nie otrzymasz zwrotu pieniędzy.");
        }
        notificationService.sendNotification(
                reservation.getUserId(),
                new NotificationTemplate(
                        content.toString(),
                        reservation.getId()
                ),
                "Rezerwacja " + event.getReservation().getId() + " została anulowana."
        );
    }

    @EventListener
    public void onReservationConfirmed(ReservationConfirmed event) {
        var reservation = event.getReservation();
        String maxFreeCancellationDate = reservation
                .getCheckIn()
                .minusDays(reservation.getFreeCancellationDays())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String content = "Twoja rezerwacja została potwierdzona. " +
                "Pamiętaj, że zwrot pieniędzy przysługuje tylko wtedy, gdy anulujesz rezerwację do dnia: "
                + maxFreeCancellationDate;
        notificationService.sendNotification(
                reservation.getUserId(),
                new NotificationTemplate(
                        content,
                        reservation.getId()
                ), "Rezerwacja " + event.getReservation().getId() + " została potwierdzona."
        );
    }

    private String createContent(String account, String recipent, int days, BigDecimal value) {
        return "W celu potwierdzenia rezerwacji prosimy o przelew: " +
                value + " zł " + "w ciągu " + days + " dni.\n" +
                "Adresat: " + recipent + "\n" +
                "Numer konta: " + account + "\n" +
                "Tytuł: " + UUID.randomUUID() + "\n";
    }
}