package com.dpap.bookingapp.booking.reservation;

import com.dpap.bookingapp.booking.place.PlaceService;
import com.dpap.bookingapp.notification.NotificationService;
import com.dpap.bookingapp.notification.NotificationTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationHostService {

    private final PlaceService placeService;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    private String createContent(String account, String recipent, int days, BigDecimal value, String reservationId) {
        StringBuilder sb = new StringBuilder();
        sb.append("W celu potwierdzenia rezerwacji prosimy o przelew: ")
                .append(value).append(" zł ").append("w ciągu ").append(days).append("dni").append("\n");
        sb.append("Adresat: ").append(recipent).append("\n");
        sb.append("Numer konta: ").append(account).append("\n");
        sb.append("Tytuł: ").append(reservationId).append("\n");
        return sb.toString();
    }

    public ReservationHostService(PlaceService placeService, ReservationRepository reservationRepository, NotificationService notificationService) {
        this.placeService = placeService;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public void requestReservation(Long userId, Long reservationId) {
        var reservation = findById(reservationId);
        if (!placeService.findPlaceResponseById(reservation.getPlaceId()).userId().equals(userId)) {
            throw new RuntimeException("There is no matched place");
        }
        notificationService.sendNotification(
                reservation.getUserId(),
                new NotificationTemplate(
                        createContent("1231241241", userId.toString(), 7, reservation.getValue(), reservationId.toString()),
                        reservation.getId()
                )
        );
    }

    public void confirmReservation(Long userId, Long reservationId) {
        var reservation = findById(reservationId);
        if (!placeService.findPlaceResponseById(reservation.getPlaceId()).userId().equals(userId)) {
            throw new RuntimeException("There is no matched place");
        }
        reservation.confirm();
        reservationRepository.updateState(reservation.getId(), reservation.getState());
        notificationService.sendNotification(
                reservation.getUserId(),
                new NotificationTemplate(
                        createContent("xd", "XD", 7, reservation.getValue(), reservation.getId().toString()),
                        reservation.getId()
                )
        );
    }

    public List<Reservation> findAllOffers(Long hostId) {
        return reservationRepository.findAllToAccept(hostId);
    }

    public void cancelOffer(Long reservationId, Long userId) {
        var reservation = findAllOffers(userId).stream().filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found reservation with id: " + reservationId));
        reservation.cancel(LocalDateTime.now());
        reservationRepository.updateState(reservation.getId(), reservation.getState());
        String content = """
                Host has rejected your reservation.
                """;
        notificationService.sendNotification(
                reservation.getUserId(),
                new NotificationTemplate(
                        content,
                        reservation.getId()
                )
        );
    }

    Reservation findById(Long id) {
        return reservationRepository.findById(id).getOrElseThrow(() -> new RuntimeException("Not found Reservation " + id));
    }
}
