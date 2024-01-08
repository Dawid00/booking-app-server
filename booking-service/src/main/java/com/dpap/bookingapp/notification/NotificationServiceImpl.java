package com.dpap.bookingapp.notification;

import com.dpap.bookingapp.users.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@Profile("dev")
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    public NotificationServiceImpl(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    public void sendNotification(Long userId, NotificationTemplate template) {
        sendNotification(userService.fetchEmailByUserId(userId), template);
    }

    public void sendNotification(String recipientEmail, NotificationTemplate template) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(createSubject(template.reservationId()));
        message.setText(template.content());

        javaMailSender.send(message);
    }

    private String createSubject(Long reservationId) {
        return "Rezerwacja: " + reservationId + " zaaktualizowana. Sprawdź stan w aplikacji.";
    }
}

