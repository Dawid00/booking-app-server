package com.dpap.bookingapp.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class NotifciationMockService implements NotificationService{
    @Override
    public void sendNotification(String recipientEmail, NotificationTemplate template) {

    }

    @Override
    public void sendNotification(Long userId, NotificationTemplate template) {
        System.out.println("XDD");
    }
}