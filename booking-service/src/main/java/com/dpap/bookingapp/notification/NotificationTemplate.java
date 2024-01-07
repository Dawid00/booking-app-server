package com.dpap.bookingapp.notification;

import java.time.LocalDateTime;

public record NotificationTemplate(String content, Long reservationId, LocalDateTime from, LocalDateTime to) {
}
