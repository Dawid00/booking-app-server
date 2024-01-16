package com.dpap.bookingapp.booking.reservation;


import com.dpap.bookingapp.availability.timeslot.TimeSlot;
import com.dpap.bookingapp.availability.usage.UsageService;
import com.dpap.bookingapp.booking.place.room.RoomService;
import com.dpap.bookingapp.booking.place.room.dto.RoomId;
import com.dpap.bookingapp.booking.reservation.dto.AddReservationRequest;
import com.dpap.bookingapp.booking.reservation.dto.FeeRequest;
import com.dpap.bookingapp.booking.reservation.dto.SearchReservationFilter;
import com.dpap.bookingapp.booking.reservation.events.ReservationEventPublisher;
import com.dpap.bookingapp.booking.reservation.events.ReservationRequested;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationService {

    private final ReservationEventPublisher reservationEventPublisher;
    private final ReservationRepository reservationRepository;
    private final UsageService usageService;
    private final RoomService roomService;

    public ReservationService(UsageService usageService, ReservationRepository reservationRepository, RoomService roomService, ReservationHostService reservationHostService, ReservationEventPublisher reservationEventPublisher) {
        this.usageService = usageService;
        this.reservationRepository = reservationRepository;
        this.roomService = roomService;
        this.reservationEventPublisher = reservationEventPublisher;
    }

    @Transactional
    public void reserveRoom(AddReservationRequest request, Long userId) {
        var room = roomService.findRoomById(RoomId.fromLong(request.roomId()))
                .orElseThrow(() -> new RuntimeException("Not found room with id: " + request.roomId()));
        if (!checkRoomAvailability(
                RoomId.fromLong(request.roomId()),
                new TimeSlot(request.start(), request.finish()).adjustHours(12, 10)
        )) {
            throw new RuntimeException("Can not reserve room");
        }
        var now = LocalDateTime.now();
        Reservation reservation = Reservation.createReservation(
                new TimeSlot(request.start(), request.finish()).adjustHours(12, 10),
                request.roomId(),
                now,
                userId,
                request.placeId(),
                BigDecimal.valueOf(room.getPricePerNight()),
                7
        );
        usageService.reserveObject(request.roomId(), new TimeSlot(request.start(), request.finish()).adjustHours(12, 10), now);
        reservationRepository.save(reservation);
        reservationEventPublisher.publish(new ReservationRequested(this, reservation));
    }

    public void checkInReservation(Long userId, Long reservationId) {
        var reservation = findByIdAndUserId(reservationId, userId);
        reservation.checkIn();
        reservationRepository.update(reservation.getId(), reservation.getValue(), reservation.getState());
    }

    public void checkOutReservation(Long reservationId, Long userId) {
        var reservation = findByIdAndUserId(reservationId, userId);
        reservation.checkOut();
        reservationRepository.update(reservation.getId(), reservation.getValue(), reservation.getState());
    }

    public void cancelReservation(Long reservationId, Long userId) {
        var reservation = findByIdAndUserId(reservationId, userId);
        reservation.cancel(LocalDateTime.now());
        usageService.deleteByObjectId(reservation.getRoomId().getId());
        reservationRepository.update(reservation.getId(), reservation.getValue(), reservation.getState());
    }

    public void addCost(Long reservationId, Long userId, FeeRequest feeRequest) {
        var reservation = findByIdAndUserId(reservationId, userId);
        reservation.addCost(feeRequest.value());
    }

    Reservation findByIdAndUserId(Long reservationId, Long userId) {
        return reservationRepository.findByIdAndUserId(reservationId, userId).getOrElseThrow(() -> new RuntimeException("Not found Reservation " + reservationId));
    }

    Reservation findById(Long id) {
        return reservationRepository.findById(id).getOrElseThrow(() -> new RuntimeException("Not found Reservation " + id));
    }

    public boolean checkRoomAvailability(RoomId id, TimeSlot timeSlot) {
        return usageService.isObjectAvailable(id.getId(), timeSlot);
    }

    public List<Reservation> findReservations(SearchReservationFilter filter) {
        return reservationRepository.findAllByFilters(filter);
    }
}
