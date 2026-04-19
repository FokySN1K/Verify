package com.example.verify_backend.UtilService.Notification;

public interface NotificationSender {

    void sendNotification(String header, String text, String source);
}
