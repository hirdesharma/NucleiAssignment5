package com.example.user_subscription_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

  @Mock
  private JavaMailSender emailSender;

  @InjectMocks
  private EmailService emailService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSendSubscriptionNotification() {
    // Arrange
    String to = "test@gmail.com";
    String subscriptionDetails = "details";

    ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

    emailService.sendSubscriptionNotification(to, subscriptionDetails);

    verify(emailSender).send(messageCaptor.capture());

    SimpleMailMessage capturedMessage = messageCaptor.getValue();
    assertEquals(to, capturedMessage.getTo()[0]);
    assertEquals("Subscription Confirmation", capturedMessage.getSubject());
    assertEquals("Thank you for subscribing! " + subscriptionDetails, capturedMessage.getText());
  }
}
