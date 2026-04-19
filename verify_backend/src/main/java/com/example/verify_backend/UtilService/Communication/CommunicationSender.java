package com.example.verify_backend.UtilService.Communication;

import jakarta.validation.constraints.NotBlank;

public interface CommunicationSender {

    void sendNotification(String text, String source);
}
