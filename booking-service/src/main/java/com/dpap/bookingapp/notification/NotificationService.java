package com.dpap.bookingapp.notification;

public interface NotificationService {
    void sendNotification(String recipientEmail, NotificationTemplate template);
    void sendNotification(Long userId, NotificationTemplate template);
}
