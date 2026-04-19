package com.example.verify_backend.UtilService.Notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailSenderUnitTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailSender emailSender;

    @BeforeEach
    void setUp() {
        emailSender = new EmailSender(mailSender);
        ReflectionTestUtils.setField(emailSender, "fromAddress", "noreply@example.com");
    }

    @Test
    void shouldBuildAndSendSimpleEmail() {
        emailSender.sendNotification("header", "body", "target@example.com");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertThat(message.getSubject()).isEqualTo("header");
        assertThat(message.getText()).isEqualTo("body");
        assertThat(message.getFrom()).isEqualTo("noreply@example.com");
        assertThat(message.getTo()).containsExactly("target@example.com");
    }
}
