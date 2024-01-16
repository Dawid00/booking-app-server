package com.dpap.bookingapp.notification;

public interface NotificationService {
    void sendNotification(String recipientEmail, NotificationTemplate template, String subject);
    void sendNotification(Long userId, NotificationTemplate template, String subject);
}
