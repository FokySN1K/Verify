package com.example.verify_backend.UtilService.Notification;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailSender emailSender;

    @Test
    public void sendNotificationTest() {
        System.out.println("КАНАРЕЙКА");
        emailSender.sendNotification("Смена статуса Xml", "Где-то там что-то там сменили", "yaroslav@stetsenko.su");


    }
}
