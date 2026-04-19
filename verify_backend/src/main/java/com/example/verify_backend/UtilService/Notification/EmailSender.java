package com.example.verify_backend.UtilService.Notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

@Service("EMAIL_NOTIFICATION")
@RequiredArgsConstructor
public class EmailSender implements NotificationSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Override
    public void sendNotification(String header, String text, String source) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(source);
        message.setSubject(header);
        message.setText(text);
        message.setFrom(fromAddress);

        mailSender.send(message);
    }
}
